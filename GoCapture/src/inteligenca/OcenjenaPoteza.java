package inteligenca;

import splosno.Poteza;
	
//	private static final int ZMAGA = Integer.MAX_VALUE;  // vrednost zmage
//	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
//	private static final int NEODLOC = 0;   // vrednost neodlocene igre

public class OcenjenaPoteza {

	Poteza poteza;
	int ocena;
	
	public OcenjenaPoteza (Poteza poteza, int ocena) {
		this.poteza = poteza;
		this.ocena = ocena;
	}
	
	public int compareTo (OcenjenaPoteza op) {
		if (this.ocena < op.ocena) return -1;
		else if (this.ocena > op.ocena) return 1;
		else return 0;
	}

}
