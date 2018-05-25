package logika;

/**
 * @author Gasper
 * Objekt razreda Poteza hrani dve celostevilski vrednosti, ki predstavljata koordinati na igralni plosci.
 */
public class Poteza {
	protected int vrstica;
	protected int stolpec;
	
	/**
	 * Ustvari se nova poteza in nastavi vrednosti koordinat.
	 * @param vr vrstica
	 * @param st stolpec
	 */
	public Poteza(int vr, int st) {
		this.vrstica = vr;
		this.stolpec = st;
	}
	
	public int getVrstica() {
		return vrstica;
	}

	public int getStolpec() {
		return stolpec;
	}
}
