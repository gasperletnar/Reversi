package logika;

/**
 * @author Gasper
 * Mozni igralci.
 */
public enum Igralec {
	BELI,
	CRNI;
	
	/**
	 * @return nasprotni igralec
	 */
	public Igralec nasprotnik() {
		return (this == BELI ? CRNI : BELI);
	}
	
	/**
	 * @return polje enako barvi igralca
	 */
	public Polje dobiPolje() {
		return(this == BELI? Polje.BELO : Polje.CRNO);
	}
	
}
