package logika;

/**
 * Možni igralci.
 */

public enum Igralec {
	BELI, CRNI;

	public Igralec nasprotnik() {
		return (this == BELI ? CRNI : BELI);
	}

	public Polje getPolje() {
		return (this == BELI ? Polje.BELO : Polje.CRNO);
	}
	
	@Override
	public String toString() {
		return (this == BELI ? "beli" : "črni");
	}
}
