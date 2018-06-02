package gui;

import javax.swing.SwingWorker;
import inteligenca.Minimax;
import logika.Igralec;
import logika.Poteza;

/**
 * @author Gasper
 * Objekt Racunalnik vlece najboljse poracunane poteze iz inteligence.
 */
public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private SwingWorker<Poteza, Object> mislec;
	
	/**
	 * Pove kateri igralec je objekt - crni ali beli.
	 */
	private Igralec jaz;
	
	/**
	 * @param master - glavno okno
	 * @param jaz - barva igralca
	 */
	public Racunalnik(GlavnoOkno master, Igralec jaz) {
		super();
		this.jaz = jaz;
		this.master = master;
	}

	@Override
	public void naPotezi() {
		mislec = new Minimax(master, master.getTezavnost(), jaz);
		mislec.execute();
	}

	@Override
	public void prekini() {
		if (mislec != null) {
			mislec.cancel(true);
		}
	}

	@Override
	public void klik(int x, int y) {		
	}
}
