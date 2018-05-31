package gui;

import logika.Igralec;
import logika.Poteza;

public class Clovek extends Strateg {
	private GlavnoOkno master;
	private Igralec jaz;
	
	public Clovek(GlavnoOkno master, Igralec jaz) { // Ko Strateg dela, potrebuje dostop do okna, da izvede potezo.
		this.master = master;
		this.jaz = jaz;
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

	@Override
	public int getTezavnost() {
		// TODO Auto-generated method stub
		return 0;
	}

}
