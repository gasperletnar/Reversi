package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextArea;

import inteligenca.Ocena;
import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Poteza;

/**
 * @author Gasper
 * Glavno okno preko katerega poteka vse v zvezi z igro.
 */
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
	 * Strateg, ki vlece poteze crnega igralca.
	 */
	private Strateg strategC;

	/**
	 * Strateg, ki vlece poteze belega igralca.
	 */
	private Strateg strategB;
	
	/**
	 * Stopnja tezavnosti, od 1 do 4.
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
	private JMenuItem pravila;
	private JInternalFrame frame;
	private JButton exit;
	
	public GlavnoOkno() throws IOException{
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Reversi");
		setLayout(new GridBagLayout());
		
		// Meni.
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu igraMeni = new JMenu("Možnosti");
		menuBar.add(igraMeni);
		JMenu igraTezavnost = new JMenu("Težavnost");
		menuBar.add(igraTezavnost);
		JMenu pomoc = new JMenu("Pravila igre");
		menuBar.add(pomoc);
		
		// Gumbi za tezavnost.
		ButtonGroup grupaGumbov = new ButtonGroup();
		lahko = new JRadioButtonMenuItem("Lahko");
		grupaGumbov.add(lahko);
		igraTezavnost.add(lahko);
		lahko.addActionListener(this);
		srednje = new JRadioButtonMenuItem("Srednje");
		grupaGumbov.add(srednje);
		igraTezavnost.add(srednje);
		srednje.addActionListener(this);
		tezko = new JRadioButtonMenuItem("Težko");
		grupaGumbov.add(tezko);
		igraTezavnost.add(tezko);
		tezko.addActionListener(this);
		zeloTezko = new JRadioButtonMenuItem("Zelo težko");
		grupaGumbov.add(zeloTezko);
		igraTezavnost.add(zeloTezko);
		zeloTezko.addActionListener(this);
		
		// Gumbi v meniju za izbiro vrste igre.
		iVsI = new JMenuItem("Nova igra: Igralec proti igralcu");
		igraMeni.add(iVsI);
		iVsI.addActionListener(this);
		iVsR = new JMenuItem("Nova igra: Igralec proti računalniku");
		igraMeni.add(iVsR);
		iVsR.addActionListener(this);
		rVsI = new JMenuItem("Nova igra: Računalnik proti igralcu");
		igraMeni.add(rVsI);
		rVsI.addActionListener(this);
		rVsR = new JMenuItem("Nova igra: Računalnik proti računalniku");
		igraMeni.add(rVsR);
		rVsR.addActionListener(this);
		
		igraMeni.addSeparator();
		izhod = new JMenuItem("Izhod");
		igraMeni.add(izhod);
		izhod.addActionListener(this);
		
		pravila = new JMenuItem("Pravila igre");
		pomoc.add(pravila);
		pravila.addActionListener(this);
		
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
		
		// Privzeta tezavnost srednje in igra clovek proti racunalniku.
		this.tezavnost = 3;
		nova_igra(true, false);
		
		// Polje za izpis pravil.
		JTextArea text = new JTextArea("Pravila");
		BufferedReader br = new BufferedReader(new FileReader("Pravila.txt"));
		String file;
		try {
		    StringBuilder sb = new StringBuilder();
		    while (br.ready()) {
		    	sb.append(br.readLine() + "\n");
		    }
		    file = sb.toString();
		} finally {
		    br.close();
		}
		text.setText(file);
		
		frame = new JInternalFrame("Pravila");
		frame.add(text);
		JMenuBar menu = new JMenuBar();
		frame.setJMenuBar(menu);
		exit = new JButton("Zapri");
		exit.addActionListener(this);
		menu.add(exit);
		GridBagConstraints p = new GridBagConstraints();
		p.gridx = 0;
		p.gridy = 1;
		p.fill = GridBagConstraints.BOTH;
		p.weightx = 1;
		p.weighty = 1;
		getContentPane().add(frame, p);
	    frame.setVisible(false);
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
	public void nova_igra(boolean prvi, boolean drugi) {
		if (strategC != null) {strategC.prekini(); } // Da strateg od prejsnje igre ne naredi poteze v novi igri.
		if (strategB != null) {strategB.prekini(); } // To pride v postev, ce igramo proti racunalniku.
		igra = new Igra();
		strategC = (prvi? new Clovek(this, Igralec.CRNI) : new Racunalnik(this, Igralec.CRNI));
		strategC.naPotezi();
		strategB = (drugi? new Clovek(this, Igralec.BELI) : new Racunalnik(this, Igralec.BELI));
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
			nova_igra(true, true);
		}
		if (e.getSource() == iVsR) {
			nova_igra(true, false);
		}
		if (e.getSource() == rVsI) {
			nova_igra(false, true);
		}
		if (e.getSource() == rVsR) {
			nova_igra(false, false);
		}
		if (e.getSource() == pravila) {
			platno.setVisible(false);
			frame.setVisible(true);
		}
		if (e.getSource() == exit) {
			platno.setVisible(true);
			frame.setVisible(false);
		}
		if (e.getSource() == izhod) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
		if (e.getSource() == lahko){
			tezavnost = 1;
			Ocena.nastaviVrednosti (1, 3, -1, -1, 1, 1);
		}
		if (e.getSource() == srednje){
			tezavnost = 2;
			Ocena.nastaviVrednosti (1, 10, -2, -3, 2, 1);
		}
		if (e.getSource() == tezko){
			tezavnost = 3;
			Ocena.nastaviVrednosti (1, 20, -5, -10, 3, 2);
		}
		if (e.getSource() == zeloTezko){
			tezavnost = 4;
			Ocena.nastaviVrednosti (1, 50, -10, -20, 5, 2);
		}
	}
	
	/**
	 * @return trenutna tezavnost igre
	 */
	public int getTezavnost() {
		return tezavnost;
	}

	/**
	 * Izvede se poteza v igri. Ce je v novem stanju igre na potezi eden izmed strategov, na njem poklice metodo naPotezi().
	 * @param p - poteza
	 */
	public void odigraj(Poteza p) {
		igra.izvediPotezo(p);
		osveziGui();
		switch (igra.stanje()){
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
			switch (igra.stanje()) {
			case NA_POTEZI_BELI: status.setText("Na potezi beli."); break;
			case NA_POTEZI_CRNI: status.setText("Na potezi črni."); break;
			case ZMAGA_CRNI: status.setText("Konec igre, zmagal je črni!"); break;
			case ZMAGA_BELI: status.setText("Konec igre, zmagal je beli!"); break;
			case NEODLOCENO: status.setText("Konec igre, nihče ni zmagal!"); break;
			}
			stevec.setText("Črni: " + igra.prestejPolja().getKey() + " - Beli: " + igra.prestejPolja().getValue());
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