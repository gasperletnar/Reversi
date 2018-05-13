package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import logika.Igra;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener{
	private GlavnoOkno master;
	int stranica;
	
	// Sirine so relativne
	
	private final static double LINE_WIDTH = 0.1; // Sirina crt.
	private final static double PADDING = 0.03; // Prostor med krogcem in robom kvadrata.
	
	public Platno(GlavnoOkno master) {
		super();
		this.master = master;
		this.addMouseListener(this);
	}
	
	private float stKvadrata() {
		return Math.min(getWidth(), getHeight()) / Igra.N;
	}
	
	/**
	 * @param g2
	 * @param i
	 * @param j
	 * @param igralec Pobarva se polje glede na barvo igralca, ki je podana.
	 */
	private void paint(Graphics2D g2, int i, int j, Color bIgralca) {
		float s = stKvadrata();
		double r = s * (1.0 - LINE_WIDTH - 2.0 * PADDING);
		double x = s * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = s * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(bIgralca);
		g2.drawOval((int)x, (int)y, (int)r, (int)r);
		g2.fillOval((int)x, (int)y, (int)r, (int)r);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g; // Povemo javi da je g razreda Graphics2D - vec metod.
		g2.setColor(Color.BLACK);
		float s = stKvadrata();
		g2.setStroke(new BasicStroke(5)); // Debelina crte za risanje.
		for (int i = 1; i < Igra.N; i++) {
			g2.drawLine((int)(i*s),
					(int)(s*LINE_WIDTH),
					(int)(i*s),
					(int)(s * (Igra.N-LINE_WIDTH)));
			g2.drawLine((int)(s*LINE_WIDTH),
					(int)(i*s),
					(int)(s * (Igra.N-LINE_WIDTH)),
					(int)(i*s));
		}
		paint(g2, 4, 4, Color.black);
		paint(g2, 3, 3, Color.black);
		paint(g2, 4, 3, Color.white);
		paint(g2, 3, 4, Color.white);
	}
	
	public Dimension getPreferredSize() { // Velikost okna ko se odpre je nastavljena na dolzino stranice * dolzino stranice platna.
		return new Dimension(600, 600);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int w = (int)stKvadrata();
		int i = x / w; // stolpec pritisnjenega kvadratka (stetje se zacne z 0)
		int j = y / w; // vrstica pritisnjenega kvadratka
		System.out.println(i);
		System.out.println(j);
		// TODO klik ne sme biti veljaven na èrtah in izven polja, klik je potrebno poslati
		// masterju
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
