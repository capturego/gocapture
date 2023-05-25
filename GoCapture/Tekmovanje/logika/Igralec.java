package logika;

/**
 * Možni igralci.
 */

public enum Igralec {
	BE, CR;

	public Igralec nasprotnik() {
		return (this == BE ? CR : BE);
	}

	public Polje getPolje() {
		return (this == BE ? Polje.BE : Polje.CR);
	}
	
	@Override
	public String toString() {
		return (this == BE ? "beli" : "crni");
	}
}
