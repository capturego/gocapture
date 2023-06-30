package inteligenca;

import logika.Graf;
import logika.Igra;
import logika.Igralec;
import logika.Stanje;
import logika.Tocka;
import splosno.Poteza;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	public Inteligenca () {
		super("skupina_123");
	}

	public Poteza izberiPotezo (Igra igra) {
		Poteza izsiljena = izsiljena(igra);
		if (izsiljena != null) {
			return izsiljena;
		}
		return new Alphabeta(4).izberiPotezo(igra);
	}

	public Poteza izberiPotezo (Igra igra, int tezavnost) {
		Poteza izsiljena = izsiljena(igra);
		if (izsiljena != null) {
			return izsiljena;
		}
		return new Alphabeta(tezavnost).izberiPotezo(igra);
	}

	public Poteza izsiljena (Igra igra) {
		Igralec jaz = igra.naPotezi();
		Poteza p = null;
		for (Graf graf : igra.GRAFI.values()) {
			// Če lahko zmaga s to potezo:
			if (graf.moc() == 1 && graf.lastnik != jaz && igra.naPotezi() == jaz) {
				Tocka lib = graf.libs.iterator().next();
				p = new Poteza (lib.x,lib.y);
			}
		}
		if (p != null) {return p;}
		for (Graf graf : igra.GRAFI.values()) {
			// Če mora odigrati, da nasprotnik ne zmaga v naslednji potezi
			if (graf.moc() == 1 && graf.lastnik == jaz) {
				Igra kopijaIgre = new Igra(igra);
				Tocka lib = graf.libs.iterator().next();
				kopijaIgre.odigraj(lib);
				if ((kopijaIgre.stanje() != Stanje.ZMAGA_BELI && jaz == Igralec.CRNI) ||
					(kopijaIgre.stanje() != Stanje.ZMAGA_CRNI && jaz == Igralec.BELI)) {
					p = new Poteza (lib.x,lib.y);
				}
			}
		}
		return p;
	}
}