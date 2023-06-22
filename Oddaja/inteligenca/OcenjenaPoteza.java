package inteligenca;

import logika.Tocka;
import splosno.Poteza;
	

public class OcenjenaPoteza {

	Poteza poteza;
	int ocena;
	
	public OcenjenaPoteza (Poteza poteza, int ocena) {
		this.poteza = poteza;
		this.ocena = ocena;
	}
	public OcenjenaPoteza (Tocka poteza, int ocena) {
		this.poteza = new Poteza(poteza.x, poteza.y);
		this.ocena = ocena;
	}
	
	public int compareTo (OcenjenaPoteza op) {
		if (this.ocena < op.ocena) return -1;
		else if (this.ocena > op.ocena) return 1;
		else return 0;
	}

}
