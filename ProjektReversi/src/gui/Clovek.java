package gui;

import logika.Poteza;

public class Clovek extends Strateg {
	private GlavnoOkno master;
	
	public Clovek(GlavnoOkno master) { // Ko Strateg dela, potrebuje dostop do okna, da izvede potezo.
		this.master = master;
	}

	@Override
	public void naPotezi() { // GlavnoOkno doloca, kdo je na potezi.
	}

	@Override
	public void klik(int x, int y) {
		master.odigraj(new Poteza(x, y)); // Povemo oknu master naj odigra potezo.
	}

	@Override
	public void prekini() {		
	}

}
