package logika;

public enum Igralec {
	BELI,
	CRNI;
	
	public Igralec nasprotnik() {
		return (this == BELI ? CRNI : BELI);
	}
	
	/**
	 * @return Polje enako barvi igralca.
	 */
	
	public Polje dobiPolje() {
		return(this == BELI? Polje.BELO : Polje.CRNO);
	}
	
}
