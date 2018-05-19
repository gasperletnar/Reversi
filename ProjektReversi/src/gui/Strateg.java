package gui;

public abstract class Strateg { // Abstrakten razred, ima dva konkretna podrazreda: Racunalnik in Clovek.
	
	/**
	 * GlavnoOkno klice to metodo na objektu razreda Strateg, ko je na potezi.
	 */
	public abstract void na_potezi();
	
	/**
	 * Strateg neha igrati.
	 */
	public abstract void prekini();
	
	/**
	 * GlavnoOkno klice to metodo, ko uporabnik klikne na mesto (x, y) na polju.
	 * @param x - x koordinata klika miske.
	 * @param y - y koordinata klika miske.
	 */
	public abstract void klik(int x, int y);
}
