package inteligenca;

import java.util.Set;

import logika.Igra;
import logika.Igralec;
import splosno.Poteza;
import logika.Graf;


public class Alphabeta extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	
	private int globina;
	
	public Alphabeta (int globina) {
//		super("alphabeta globina " + globina);
		this.globina = globina;
	}
	
	@Override
	public Poteza izberiPotezo (Igra igra) {
		// Na začetku alpha = ZGUBA in beta = ZMAGA
		return alphabetaPoteze(igra, this.globina, ZGUBA, ZMAGA, igra.naPotezi()).poteza;
	}
	
	public static OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
		if (igra.naPotezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		Set<Poteza> moznePoteze = igra.moznePoteze;
		Poteza kandidat = moznePoteze.iterator().next(); // Možno je, da se ne spremini vrednost kanditata. Zato ne more biti null.
		for (Graf graf : igra.GRAFI.values()) {
			if (graf.moc() == 1) {
				String lib = graf.libs.iterator().next();
				String[] xy = lib.split("-");
				Poteza poteza = new Poteza(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
				if (graf.lastnik == jaz) {
					return new OcenjenaPoteza (poteza, ZGUBA/2);
				}
				else {
					return new OcenjenaPoteza (poteza, ZMAGA);
				}
			}
			else if (graf.moc() == 2 && graf.lastnik != jaz) {
				String lib = graf.libs.iterator().next();
				String[] xy = lib.split("-");
				Poteza poteza = new Poteza(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
				Igra kopijaIgre = new Igra(igra);
				kopijaIgre.odigraj(poteza);
				ocena = alphabetaPoteze (kopijaIgre, globina, alpha, beta, jaz).ocena;
			}
		}
		for (Poteza p: moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O: ocenap = (jaz == Igralec.CR ? ZMAGA : ZGUBA); break;
			case ZMAGA_X: ocenap = (jaz == Igralec.BE ? ZMAGA : ZGUBA); break;
//			case NEODLOCENO: ocenap = NEODLOC; break;
			default:
				// Nekdo je na potezi
				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
			}
			if (igra.naPotezi() == jaz) { // Maksimiramo oceno
				if (ocenap > ocena) { // mora biti > namesto >=
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha,ocena);
				}
			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
				if (ocenap < ocena) { // mora biti < namesto <=
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
				break;
		}
		return new OcenjenaPoteza (kandidat, ocena);
	}

}