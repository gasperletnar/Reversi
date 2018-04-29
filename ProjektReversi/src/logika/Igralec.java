package logika;

public enum Igralec {
	BELI,
	CRNI;
	
	public Igralec nasprotnik() {
		return (this == BELI ? CRNI : BELI);
	}
}
