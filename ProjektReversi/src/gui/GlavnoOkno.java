package gui;

import java.awt.Color;

import javax.swing.JFrame;

import logika.Igra;

@SuppressWarnings("serial")
public class GlavnoOkno extends JFrame {
	private Platno platno;
	private Igra igra;
	
	public GlavnoOkno(){
		platno = new Platno(600);
		setTitle("Reversi");
		platno.setBackground(Color.green);
		add(platno);
		
	}
	
}
