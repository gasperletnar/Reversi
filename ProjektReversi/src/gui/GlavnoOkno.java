package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import logika.Polje;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	private Platno platno;
	private Igra igra;
	private JLabel status;
	private JLabel stevecC;
	private JLabel stevecB;
	
	private Strateg strategC;
	private Strateg strategB;
	
	private JMenuItem nova_igra;
	
	public GlavnoOkno(){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Reversi");
		setLayout(new GridBagLayout());
		
		JMenuBar menu_bar = new JMenuBar();
		setJMenuBar(menu_bar);
		JMenu igra_menu = new JMenu("Igra");
		menu_bar.add(igra_menu);
		nova_igra = new JMenuItem("Nova igra");
		igra_menu.add(nova_igra);
		nova_igra.addActionListener(this);
		
		platno = new Platno(this);
		platno.setBackground(Color.green);
		GridBagConstraints polje_layout = new GridBagConstraints();
		polje_layout.gridx = 0;
		polje_layout.gridy = 0;
		polje_layout.fill = GridBagConstraints.BOTH;
		polje_layout.weightx = 0.9;
		polje_layout.weighty = 0.9;
		getContentPane().add(platno, polje_layout);
		
		status = new JLabel();
		status.setFont(new Font(status.getFont().getName(),
				status.getFont().getStyle(), 20));
		GridBagConstraints status_layout = new GridBagConstraints();
		status_layout.gridx = 0;
		status_layout.gridy = 1;
		status_layout.fill = GridBagConstraints.CENTER;
		status_layout.weightx = 0.1;
		status_layout.weighty = 0.1;
		getContentPane().add(status, status_layout);
		
		stevecC = new JLabel();
		GridBagConstraints stevecC_layout = new GridBagConstraints();
		stevecC_layout.gridx = 1;
		stevecC_layout.gridy = 0;
		stevecC_layout.fill = GridBagConstraints.CENTER;
		stevecC_layout.weightx = 0.3;
		stevecC_layout.weighty = 0.3;
		getContentPane().add(stevecC, stevecC_layout);
		
		stevecB = new JLabel();
		GridBagConstraints stevecB_layout = new GridBagConstraints();
		stevecB_layout.gridx = 1;
		stevecB_layout.gridy = 1;
		stevecB_layout.fill = GridBagConstraints.CENTER;
		stevecB_layout.weightx = 0.3;
		stevecB_layout.weighty = 0.3;
		getContentPane().add(stevecB, stevecB_layout);
		
		
		nova_igra();

		
	}
	
	public Polje[][] getPlosca(){
		return (igra == null ? null : igra.getPlosca());
	}

	public void nova_igra() {
		if (strategC != null) {
			strategC.prekini();
		}
		if (strategB != null) {
			strategB.prekini();
		}
		this.igra = new Igra();
		strategC = new Clovek(this);
		strategB = new Clovek(this);
		osveziGui();
		repaint();
		
	}
	
	public List<Poteza> seznamDovoljenih(){
		return igra.seznamDovoljenih();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == nova_igra) {
			nova_igra();
		}
		// TODO Auto-generated method stub
		
	}
	
	public void odigraj(Poteza p) {
		igra.izvediPotezo(p);
		igra.izpisDovoljenih();
		osveziGui();
		switch (igra.stanje()){
		case NA_POTEZI_BELI: strategB.na_potezi(); break;
		case NA_POTEZI_CRNI: strategC.na_potezi(); break;
		case ZMAGA_CRNI: break;
		case ZMAGA_BELI: break;
		case NEODLOCENO: break;
		}
	}
	
	public void osveziGui() {
		if (igra == null) {
			status.setText("Igra se ne igra");
		}
		else {
			switch (igra.stanje()) {
			case NA_POTEZI_BELI: status.setText("Igra beli"); break;
			case NA_POTEZI_CRNI: status.setText("Igra èrni"); break;
			case ZMAGA_CRNI: status.setText("Zmagal je èrni"); break;
			case ZMAGA_BELI: status.setText("Zmagal je beli"); break;
			case NEODLOCENO: status.setText("Nobeden ni zmagal"); break;
			}
		}
		stevecC.setText("Èrni: " + igra.prestejPolja().getKey());
		stevecB.setText("Beli: " + igra.prestejPolja().getValue());
		platno.repaint();
	}
	
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
	
	public Igra copyIgra() {
		return new Igra(igra);
	}
	
	
}
