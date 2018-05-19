package gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JPanel;

import logika.Igra;
import logika.Polje;
import logika.Poteza;

@SuppressWarnings("serial")
public class Platno extends JPanel implements MouseListener {
	private GlavnoOkno master;
	
	/**
	 * Relativna sirina crte.
	 */
	private final static double LINE_WIDTH = 0.05; // Relativna sirina crt.
	
	/**
	 * Relativni prostor med plosckom in robom kvadrata.
	 */
	private final static double PADDING = 0.1;
	
	public Platno(GlavnoOkno master) {
		super();
		this.master = master;
		addMouseListener(this);
	}
	
	/**
	 * @return Dolžina stranice kvadrata polja.
	 */
	private double stKvadrata() {
		return Math.min(getWidth(), getHeight()) / Igra.N; // Minimum sirine/visine platna, delimo z stevilom polj igre - platno razdeli na N delov.
	}
	
	@Override
	public Dimension getPreferredSize() { // Velikost platna je nastavljena na 600*600.
		return new Dimension(600, 600);
	}
	
	/**
	 * Na platno narise nov ploscek.
	 * @param g2
	 * @param i
	 * @param j
	 * @param igralec Barva ploscka.
	 */
	private void paintFigure(Graphics2D g2, int i, int j, Color bIgralca) {
		double s = stKvadrata();
		// Premer je stranica kvadrata(se preden so narisane crte) - na vsaki strani polovica sirine crte - na vsaki strani razdalja do roba(PADDING).
		double r = s * (1.0 - LINE_WIDTH - 2.0 * PADDING);
		// X koordinata zgornjega oglisca kvadrata(se preden so narisane crte) je s*i. Treba se popraviti za toliko, kolikor je polmer krajsi:
		// s * polovica sirine crte +  s * padding.
		double x = s * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = s * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(bIgralca);
		g2.setStroke(new BasicStroke((float) (s * LINE_WIDTH)));
		g2.drawOval((int)x, (int)y, (int)r, (int)r);
		g2.fillOval((int)x, (int)y, (int)r, (int)r);
	}
	
	/**
	 * Na polje narise okvir kvadrata - za oznacitev dovoljenih polj.
	 * @param g2
	 * @param i
	 * @param j
	 */
	private void paintPossible(Graphics2D g2, int i, int j) {
		double s = stKvadrata();
		double r = s * (1.0 - LINE_WIDTH - 2.0 * PADDING);
		double x = s * (i + 0.5 * LINE_WIDTH + PADDING);
		double y = s * (j + 0.5 * LINE_WIDTH + PADDING);
		g2.setColor(Color.red);
		g2.setStroke(new BasicStroke((float) (s * LINE_WIDTH / 2)));
		g2.drawRect((int)x, (int)y, (int)r, (int)r);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g; // Povemo javi da je g razreda Graphics2D - vec metod.
		double s = stKvadrata();
		g2.setColor(Color.GREEN);
		g2.fillRect(0, 0, (int)(Igra.N*s), (int)(Igra.N*s)); // Pobarvamo polje zeleno.
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke((float) (s * LINE_WIDTH))); // Debelina crte za risanje, prilagaja se velikosti platna. s * LINE_WIDTH
		for (int i = 0; i < Igra.N + 1; i++) {
			// Navpicne crte. Narisali bomo N+2 crt, 2 dodatni za rob.
			g2.drawLine((int)(i*s), // Vsakic i pomnozimo z s(stranico kvadrata) - ko i naraste za 1, se premaknemo za eno stranico naprej.
				    (int)(0),
				    (int)(i*s), // Vertikala ostane ista.
				    (int)(Igra.N*s)); // Horizontala gre od 0 pa do stevila polj * stranica enega kvadrata polja.
			// Vodoravne crte.
			g2.drawLine((int)(0),
				    (int)(i*s),
				    (int)(Igra.N*s),
				    (int)(i*s));
		}
		Polje[][] plosca = master.getPlosca();
		if (plosca != null) {
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					switch (plosca[i][j]) { // V odvisnosti od barve polja na plosci, narise ploscek na polje na platnu.
					case CRNO: paintFigure(g2, i, j, Color.BLACK); break;
					case BELO: paintFigure(g2, i, j, Color.WHITE); break;
					default: break;
					}
				}
				
			}
		}
		List<Poteza> dovoljene = master.seznamDovoljenih();
		for(Poteza a: dovoljene) { // Za vsako povezo v seznamu dovoljenih, narisemo rdec kvadratek na polje na platnu.
			int potezaX = a.getStolpec();
			int potezaY = a.getVrstica();
			paintPossible(g2, potezaY, potezaX);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int w = (int)stKvadrata(); // Dolzina stranice kvadrata.
		int i = x / w; // Index stolpca kamor smo kliknili (1, 2,...)
		double di = (x % w) / stKvadrata(); // Delez koliko znotraj kvadrata smo kliknili na x koordinati.
		int j = y / w;
		double dj = (y % w) / stKvadrata();
		if (0 <= i && i < Igra.N && // Pogoj, da kliknem znotraj igralnega polja.
			// Moramo klikniti vsaj toliko znotraj kvadrata, da ne kliknemo na crte.
			0.5 * LINE_WIDTH < di && di < 1.0 - 0.5 * LINE_WIDTH &&
			0 <= j && j < Igra.N &&
			0.5 * LINE_WIDTH < dj && dj < 1.0 - 0.5 * LINE_WIDTH) {
			master.klikniPolje(i, j);
		}
		// Poslje sporocilo master-ju, da se je kliknilo na polje (i, j).
		// Master nato glede na argumente i, j izvede metodo klikniPolje(i, j).
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
