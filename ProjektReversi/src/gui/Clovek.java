package gui;

import logika.Igralec;
import logika.Poteza;

/**
 * @author Gasper
 * Objekt Clovek vlece poteze glede na uporabnikovo izbiro moznih potez na plosci.
 */
public class Clovek extends Strateg {
	private GlavnoOkno master;
	
	public Clovek(GlavnoOkno master, Igralec jaz) {
		this.master = master;
	}

	@Override
	public void naPotezi() {
	}

	@Override
	public void klik(int x, int y) {
		master.odigraj(new Poteza(x, y));
	}

	@Override
	public void prekini() {		
	}
}
