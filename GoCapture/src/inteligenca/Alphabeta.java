package inteligenca;

import java.util.Set;
import logika.Igra;
import logika.Igralec;
import logika.Tocka;
import logika.Graf;
import splosno.Poteza;


public class Alphabeta extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	
	private int globina;
	
	public Alphabeta (int globina) {
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
//		Set<Tocka> moznePoteze = igra.moznePoteze;
//		Tocka kandidat = moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kanditata. Zato ne more biti null.
		Tocka kandidat = igra.moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kanditata. Zato ne more biti null.
		for (Graf graf : igra.GRAFI.values()) {
			if (graf.moc() == 1) {
				Tocka lib = graf.libs.iterator().next();
				if (graf.lastnik == jaz) {
					return new OcenjenaPoteza (lib, ZGUBA/2);
				}
				else {
					return new OcenjenaPoteza (lib, ZMAGA);
				}
			}
			else if (graf.moc() == 2 && graf.lastnik != jaz) {
				Tocka lib = graf.libs.iterator().next();
				Igra kopijaIgre = new Igra(igra);
				kopijaIgre.odigraj(lib);
				ocena = alphabetaPoteze (kopijaIgre, globina, alpha, beta, jaz).ocena;
			}
		}
		for (Tocka p: igra.moznePoteze) {
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocenap;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_CRNI: ocenap = (jaz == Igralec.CRNI ? ZMAGA : ZGUBA); break;
			case ZMAGA_BELI: ocenap = (jaz == Igralec.BELI ? ZMAGA : ZGUBA); break;
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