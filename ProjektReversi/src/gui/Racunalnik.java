package gui;

import javax.swing.SwingWorker;

import inteligenca.Minimax;
import inteligenca.NakljucnaInteligenca;
import logika.Igralec;
import logika.Poteza;

public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private SwingWorker<Poteza, Object> sw;
	private int tezavnost;

	private Igralec jaz;
	
	public Racunalnik(GlavnoOkno master, Igralec jaz, int tezavnost) {
		super();
		this.jaz = jaz;
		this.master = master;
		this.tezavnost = tezavnost;
	}

	@Override
	public void naPotezi() {
		sw = new Minimax(master, tezavnost, jaz);
		sw.execute();
	}

	@Override
	public void prekini() {
		if (sw != null) {
			sw.cancel(true);
		}
	}

	@Override
	public void klik(int x, int y) {		
	}
	
	
	public int getTezavnost() {
		return tezavnost;
	}

}
