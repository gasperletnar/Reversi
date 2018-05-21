package gui;

import javax.swing.SwingWorker;

import inteligenca.NakljucnaInteligenca;
import logika.Poteza;

public class Racunalnik extends Strateg {
	private GlavnoOkno master;
	private SwingWorker<Poteza, Object> sw;
	
	public Racunalnik(GlavnoOkno master) {
		super();
		this.master = master;
	}

	@Override
	public void na_potezi() {
		sw = new NakljucnaInteligenca(master);
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

}
