package inteligenca;

import java.util.List;
import java.util.Random;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Poteza;

/**
 * @author Gasper
 * Poteze so na vsakem koraku izbrane nakljucno.
 */
public class NakljucnaInteligenca extends SwingWorker<Poteza, Object> {
	private GlavnoOkno master;
	
	public NakljucnaInteligenca(GlavnoOkno master) {
		super();
		this.master = master;
	}

	@Override
	protected Poteza doInBackground() {
		Igra igra = master.copyIgra();
		for (int i = 0; i < 5; i++) {
			System.out.println("Razmisljam.");
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				if (this.isCancelled()) {
					System.out.println("Prekinjeno!");
					return null;
				}
			}
		}
		System.out.println("Igram.");
		List<Poteza> poteze = igra.seznamDovoljenih();
		Random r = new Random();
		Poteza p = poteze.get(r.nextInt(poteze.size()));
		return p;
	}
	
	public void done() {
		try {
			Poteza p = this.get();
			if (p != null) master.odigraj(p);
		} catch (Exception e) {
		}
	}
}
