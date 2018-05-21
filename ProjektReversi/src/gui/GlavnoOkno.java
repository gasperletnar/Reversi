package gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import logika.Igra;
import logika.Igralec;
import logika.Polje;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	
	/**
	 * JPanel, sem se narise polje z vsem kar vsebuje.
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
	
	private JMenuItem nova_igra_IvsI; // JMenuItemi v atributih, da kasneje lahko dolocimo akcijo, ki naj se izvede ob kliku na dolocen gumb.
	private JMenuItem nova_igra_IvsR;
	private JMenuItem nova_igra_RvsR;
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
		
		nova_igra_IvsI = new JMenuItem("Nova igra: Igralec proti igralcu");
		igra_menu.add(nova_igra_IvsI);
		nova_igra_IvsI.addActionListener(this);
		nova_igra_IvsR = new JMenuItem("Nova igra: Igralec proti racunalniku");
		igra_menu.add(nova_igra_IvsR);
		nova_igra_IvsR.addActionListener(this);
		nova_igra_RvsR = new JMenuItem("Nova igra: Racunalnik proti racunalniku");
		igra_menu.add(nova_igra_RvsR);
		nova_igra_RvsR.addActionListener(this);
		igra_menu.addSeparator();
		izhod = new JMenuItem("Izhod");
		igra_menu.add(izhod);
		izhod.addActionListener(this);
		
		// Nasa plosca.
		platno = new Platno(this);
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0; // Kje stoji.
		polje_layout.gridy = 1;
		polje_layout.fill = GridBagConstraints.BOTH; // Kam se razsiri.
		polje_layout.weightx = 1; // Kako si razdeli odveèen prostor v horizontali z ostalimi.
		polje_layout.weighty = 1;
		getContentPane().add(platno, polje_layout);
		
		// Prostor pod plosco, kjer bo izpisano stanje igre.
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(), status.getFont().getStyle(), 20)); // Nastavimo ime fonta, stil in velikost.
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 2;
		status_layout.fill = GridBagConstraints.CENTER; // Velikost prostora informacij je "konstantna", zato ni weightx in weighty.
		getContentPane().add(status, status_layout);
		
		// Prostor nad plosco, kjer bo izpisano trenutno stevilo zetonov obeh igralcev.
		stevec = new JLabel();
		stevec.setFont(new Font(status.getFont().getName(), status.getFont().getStyle(), 20));
		GridBagConstraints stevec_layout = new GridBagConstraints();
		stevec_layout.gridx = 0;
		stevec_layout.gridy = 0;
		stevec_layout.fill = GridBagConstraints.CENTER;
		getContentPane().add(stevec, stevec_layout);
		
		nova_igra(true, false);
	}
	
	/**
	 * @return Plosca aktivne igre; null ce igre ni.
	 */
	public Polje[][] getPlosca(){
		return (igra == null ? null : igra.getPlosca());
	}
	
	/**
	 * @return True, ce je aktivni igralec clovek.
	 */
	public boolean aktivniClovek() {
		if (igra.naPotezi() == Igralec.BELI) {
			return aliBClovek();
		} else if (igra.naPotezi() == Igralec.CRNI) {
			return aliCClovek();
		} else {
			return false;
		}
	}
	
	/**
	 * @return True, ce je crni igralec clovek.
	 */
	public boolean aliCClovek() {
		Strateg c = strategC;
		return c instanceof Clovek;
	}
	
	/**
	 * @return True, ce je beli igralec clovek.
	 */
	public boolean aliBClovek() {
		Strateg b = strategB;
		return b instanceof Clovek;
	}

	/**
	 * @param prvi True, ce je prvi igralec clovek.
	 * @param drugi True, ce je drugi igralec clovek.
	 */
	public void nova_igra(boolean prvi, boolean drugi) {
		if (strategC != null) {strategC.prekini(); } // Da strateg od prejsnje igre ne naredi poteze v novi igri.
		if (strategB != null) {strategB.prekini(); } // To pride v postev, ce igramo proti racunalniku.
		igra = new Igra();
		strategC = (prvi? new Clovek(this) : new Racunalnik(this));
		strategC.na_potezi();
		strategB = (drugi? new Clovek(this) : new Racunalnik(this));
		osveziGui();
		repaint();
	}
	
	/**
	 * @return Seznam dovoljenih; null, ce igra ni v teku.
	 */
	public List<Poteza> seznamDovoljenih(){
		return(igra == null? Collections.emptyList() : igra.seznamDovoljenih());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nova_igra_IvsI) {
			nova_igra(true, true);
		}
		if (e.getSource() == nova_igra_IvsR) {
			nova_igra(true, false);
		}
		if (e.getSource() == nova_igra_RvsR) {
			nova_igra(false, false);
		}
		if (e.getSource() == izhod) {
			dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		}
	}
	
	/**
	 * Izvede se poteza v igri nato poklice na aktivnem strategu metodo naPotezi().
	 * @param p Poteza.
	 */
	public void odigraj(Poteza p) {
		igra.izvediPotezo(p);
		osveziGui(); // Na novo narisemo polje; se pravi polje po izvedeni zadnji potezi.
		switch (igra.stanje()){ // Glede na stanje igre iz logike igre, izvedemo eno izmed spodnjih vrstic.
		case NA_POTEZI_BELI: strategB.na_potezi(); break;
		case NA_POTEZI_CRNI: strategC.na_potezi(); break;
		default: break;
		}
	}
	
	/**
	 * Spremeni text layoutov status in stevec, na novo nariše na platno.
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
		}
		platno.repaint();
	}
	
	/**
	 * Ce igramo igro in je na potezi crni(beli), naj strategC(strategB) izvede metodo klik(x, y).
	 * @param x - X koordinata na polju.
	 * @param y - Y koordinata na polju.
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
	 * @return Kopija trenune igre. 
	 */
	public Igra copyIgra() { // Namenjeno racunalniku.
		return new Igra(igra);
	}
}