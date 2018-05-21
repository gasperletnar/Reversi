package inteligenca;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Poteza;

public class Minimax extends SwingWorker<Poteza, Object> {
	private GlavnoOkno master;
	
	public Minimax(GlavnoOkno master) {
		super();
		this.master = master;
	}

	@Override
	protected Poteza doInBackground() throws Exception {
		Igra igra = master.copyIgra();
		return null;
	}
	
	public void done() {
		try {
			Poteza p = this.get();
			if (p != null) master.odigraj(p);
		} catch (Exception e) {
		}
	}
}
