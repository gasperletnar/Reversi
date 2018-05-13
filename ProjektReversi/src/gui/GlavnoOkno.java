package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import logika.Igra;
import logika.Poteza;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame implements ActionListener {
	private Platno platno;
	private Igra igra;
	private JLabel status;
	
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
		
		nova_igra();

		
	}

	public void nova_igra() {
		this.igra = new Igra();
		repaint();
		
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
		switch (igra.stanje()) {

		}
	}
	
	public Igra copyIgra() {
		return new Igra(igra);
	}
	
	
}
