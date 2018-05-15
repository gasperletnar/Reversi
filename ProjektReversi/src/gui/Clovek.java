package gui;

import logika.Poteza;

public class Clovek extends Strateg{
	
	private GlavnoOkno master;
	
	public Clovek(GlavnoOkno master) {
		this.master = master;
	}

	@Override
	public void na_potezi() {	
	}

	@Override
	public void klik(int x, int y) {
		master.odigraj(new Poteza(x, y));
		
	}

	@Override
	public void prekini() {
		// TODO Auto-generated method stub
		
	}

}
