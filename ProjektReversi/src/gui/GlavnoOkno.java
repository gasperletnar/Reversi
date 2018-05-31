package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;

import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	
	/**
	 * JPanel, sem se narise igralna plosca z vsem kar vsebuje.
	 */
	private Platno platno;
	
	/**
	 * Logika igre; null ce se igra trenutno ne igra.
	 */
	private Igra igra;
	
	/**
	 * Informacija v oknu, pove kdo je na potezi.
	 */
	private JLabel status;
	
	/**
	 * Informacija v oknu, pove koliko ima vsak igralec plosckov.
	 */
	private JLabel stevec;
	
	/**
	 * Informacija v oknu, pove trenutno tezavnost
	 */
	private JLabel tezavnostTekst;
	
	/**
	 * Strateg, ki vlece poteze crnega igralca.
	 */
	private Strateg strategC;
	
	/**
	 * Strateg, ki vlece poteze belega igralca.
	 */
	private Strateg strategB;
	
	/**
	 * Stopnja tezavnosti, od 2 do 5
	 */
	private int tezavnost;
	
	private JMenuItem iVsI; // Nova igra igralec proti igralcu.
	private JMenuItem iVsR; // Nova igra igralec proti racunalniku.
	private JMenuItem rVsI; // Nova igra racunalniku proti igralcu.
	private JMenuItem rVsR; // Nova igra racunalniku proti racunalniku.
	
	// Tezavnost
	
	private JMenuItem lahko;
	private JMenuItem srednje;
	private JMenuItem tezko;
	private JMenuItem zeloTezko;
	
	private JMenuItem izhod;
	
	public GlavnoOkno(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Reversi");
		setLayout(new GridBagLayout());
		
		// Meni.
		JMenuBar menu_bar = new JMenuBar();
		setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Meni");
		menu_bar.add(igra_menu);
		JMenu igra_tezavnost = new JMenu("Tezavnost");
		menu_bar.add(igra_tezavnost);
		
		ButtonGroup grupaGumbov = new ButtonGroup();
		lahko = new JRadioButtonMenuItem("Lahko");
		grupaGumbov.add(lahko);
		igra_tezavnost.add(lahko);
		lahko.addActionListener(this);
		srednje = new JRadioButtonMenuItem("Srednje");
		grupaGumbov.add(srednje);
		igra_tezavnost.add(srednje);
		srednje.addActionListener(this);
		tezko = new JRadioButtonMenuItem("Tezko");
		grupaGumbov.add(tezko);
		igra_tezavnost.add(tezko);
		tezko.addActionListener(this);
		zeloTezko = new JRadioButtonMenuItem("Zelo tezko");
		grupaGumbov.add(zeloTezko);
		igra_tezavnost.add(zeloTezko);
		zeloTezko.addActionListener(this);
		
		iVsI = new JMenuItem("Nova igra: Igralec proti igralcu");
		igra_menu.add(iVsI);
		iVsI.addActionListener(this);
		iVsR = new JMenuItem("Nova igra: Igralec proti racunalniku");
		igra_menu.add(iVsR);
		iVsR.addActionListener(this);
		rVsI = new JMenuItem("Nova igra: Racunalnik proti igralcu");
		igra_menu.add(rVsI);
		rVsI.addActionListener(this);
		rVsR = new JMenuItem("Nova igra: Racunalnik proti racunalniku");
		igra_menu.add(rVsR);
		rVsR.addActionListener(this);
		
		igra_menu.addSeparator();
		izhod = new JMenuItem("Izhod");
		igra_menu.add(izhod);
		izhod.addActionListener(this);
		
		// Nasa plosca.
		platno = new Platno(this);
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 1;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 1;
		polje_layout.weighty = 1;
		getContentPane().add(platno, polje_layout);
		
		// Prostor pod plosco, kjer bo izpisano stanje igre.
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(), status.getFont().getStyle(), 20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 2;
		status_layout.fill = GridBagConstraints.CENTER;
		getContentPane().add(status, status_layout);
		
		// Prostor nad plosco, kjer bo izpisano trenutno stevilo zetonov obeh igralcev.
		stevec = new JLabel();
		stevec.setFont(new Font(stevec.getFont().getName(), stevec.getFont().getStyle(), 20));
		GridBagConstraints stevec_layout = new GridBagConstraints();
		stevec_layout.gridx = 0;
		stevec_layout.gridy = 0;
		stevec_layout.fill = GridBagConstraints.CENTER;
		getContentPane().add(stevec, stevec_layout);
		
		tezavnostTekst = new JLabel();
		tezavnostTekst.setFont(new Font(tezavnostTekst.getFont().getName(), tezavnostTekst.getFont().getStyle(), 20));
		GridBagConstraints tezavnostTekst_layout = new GridBagConstraints();
		tezavnostTekst_layout.gridx = 0;
		tezavnostTekst_layout.gridy = 3;
		tezavnostTekst_layout.fill = GridBagConstraints.CENTER;
		getContentPane().add(tezavnostTekst, tezavnostTekst_layout);
		
		this.tezavnost = 3;
		nova_igra(true, false, tezavnost);
	}
	
	/**
	 * @return plosca aktivne igre; null ce igre ni
	 */
	public Polje[][] getPlosca(){
		return (igra == null ? null : igra.getPlosca());
	}
	
	/**
	 * Vrne false tudi v primeru, ko ni nihce na potezi.
	 * @return true, ce je aktivni igralec clovek; sicer false
	 */
	public boolean aktivniClovek() {
		if (igra == null) return false;
		if (igra.naPotezi() == Igralec.BELI) {
			return aliBClovek();
		} else if (igra.naPotezi() == Igralec.CRNI) {
			return aliCClovek();
		} else {
			return false;
		}
	}
	
	/**
	 * @return true, ce je crni igralec clovek; false ce racunalnik
	 */
	public boolean aliCClovek() {
		Strateg c = strategC;
		return c instanceof Clovek;
	}
	
	/**
	 * @return true, ce je beli igralec clovek; false ce racunalnik
	 */
	public boolean aliBClovek() {
		Strateg b = strategB;
		return b instanceof Clovek;
	}

	/**
	 * @param prvi - true, ce je prvi igralec clovek; false ce racunalnik
	 * @param drugi - true, ce je drugi igralec clovek; false ce racunalnik
	 */
	public void nova_igra(boolean prvi, boolean drugi, int tezavnost) {
		if (strategC != null) {strategC.prekini(); } // Da strateg od prejsnje igre ne naredi poteze v novi igri.
		if (strategB != null) {strategB.prekini(); } // To pride v postev, ce igramo proti racunalniku.
		igra = new Igra();
		strategC = (prvi? new Clovek(this, Igralec.CRNI) : new Racunalnik(this, Igralec.CRNI, tezavnost));
		strategC.naPotezi();
		strategB = (drugi? new Clovek(this, Igralec.BELI) : new Racunalnik(this, Igralec.BELI, tezavnost));
		osveziGui();
		repaint();
	}
	
	/**
	 * @return seznam dovoljenih; null, ce igra ni v teku
	 */
	public List<Poteza> seznamDovoljenih(){
		return(igra == null? Collections.emptyList() : igra.seznamDovoljenih());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == iVsI) {
			nova_igra(true, true, tezavnost);
		}
		if (e.getSource() == iVsR) {
			nova_igra(true, false, tezavnost);
		}
		if (e.getSource() == rVsI) {
			nova_igra(false, true, tezavnost);
		}
		if (e.getSource() == rVsR) {
			nova_igra(false, false, tezavnost);
		}
		if (e.getSource() == izhod) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		if (e.getSource() == lahko){
			tezavnost = 2;
		}
		if (e.getSource() == srednje){
			tezavnost = 3;
		}
		if (e.getSource() == tezko){
			tezavnost = 4;
		}
		if (e.getSource() == zeloTezko){
			tezavnost = 5;
		}
	}
	
	/**
	 * Izvede se poteza v igri. Ce je v novem stanju igre na potezi eden izmed strategov, na njem poklice metodo naPotezi().
	 * @param p - poteza
	 */
	public void odigraj(Poteza p) {
		igra.izvediPotezo(p);
		osveziGui(); // Na novo narisemo polje; se pravi polje po izvedeni zadnji potezi.
		switch (igra.stanje()){ // Glede na stanje igre iz logike igre, izvedemo eno izmed spodnjih vrstic.
		case NA_POTEZI_BELI: strategB.naPotezi(); break;
		case NA_POTEZI_CRNI: strategC.naPotezi(); break;
		default: break;
		}
	}
	
	/**
	 * Spremeni text layoutov status in stevec, na novo narise na platno.
	 */
	public void osveziGui() {
		if (igra == null) {
			status.setText("Igra ni v teku.");
		}
		else {
			switch (igra.stanje()) { // Ovrednoti dano izjavo, nato izvede vse izraze, ki zadostujejo izjavi.
			case NA_POTEZI_BELI: status.setText("Na potezi beli."); break;
			case NA_POTEZI_CRNI: status.setText("Na potezi èrni."); break;
			case ZMAGA_CRNI: status.setText("Konec igre, zmagal je èrni!"); break;
			case ZMAGA_BELI: status.setText("Konec igre, zmagal je beli!"); break;
			case NEODLOCENO: status.setText("Konec igre, nihèe ni zmagal!"); break;
			}
			stevec.setText("Èrni: " + igra.prestejPolja().getKey() + " - Beli: " + igra.prestejPolja().getValue());
			if (aliCClovek()){
				tezavnostTekst.setText(Integer.toString(strategB.getTezavnost()));
			}
			if (aliBClovek()){
				tezavnostTekst.setText(Integer.toString(strategC.getTezavnost()));
			}
		}
		platno.repaint();
	}
	
	/**
	 * Ce igramo igro in je na potezi crni(beli), naj strategC(strategB) izvede metodo klik(x, y).
	 * @param x - x koordinata polja
	 * @param y - y koordinata polja
	 */
	public void klikniPolje(int x, int y) {
		if (igra != null) {
			switch (igra.stanje()) {
			case NA_POTEZI_BELI:
				strategB.klik(x, y);
				break;
			case NA_POTEZI_CRNI:
				strategC.klik(x, y);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * @return kopija trenune igre
	 */
	public Igra copyIgra() {
		return new Igra(igra);
	}
}