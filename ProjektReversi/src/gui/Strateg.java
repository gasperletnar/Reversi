package gui;

/**
 * @author Gasper
 * Strateg je objekt, ki zna odigrati potezo. Lahko je clovek ali racunalnik.
 */
public abstract class Strateg {
	
	/**
	 * GlavnoOkno klice to metodo na objektu razreda Strateg, ko je na potezi.
	 */
	public abstract void naPotezi();
	
	/**
	 * Strateg neha igrati.
	 */
	public abstract void prekini();
	
	/**
	 * GlavnoOkno klice to metodo, ko uporabnik klikne na polje (x, y) na igralni plosci - polj je NxN.
	 * @param x - 1. index polja klika miske
	 * @param y - 2. index polja klika miske
	 */
	public abstract void klik(int x, int y);
}
