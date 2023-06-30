package inteligenca;

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
		OcenjenaPoteza p = alphabetaPoteze(igra, this.globina, ZGUBA, ZMAGA, igra.naPotezi());
		return p.poteza;
	}

	public static OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
		if (igra.naPotezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
		for (String imeGrafa : igra.grafi1libs) {
			Graf g = igra.GRAFI.get(imeGrafa);
			Tocka q = g.libs.iterator().next();
			if (g.lastnik == jaz) {
				Igra kopijaIgre = new Igra(igra);
				kopijaIgre.odigraj(q);
				ocena = alphabetaPoteze (kopijaIgre, globina, alpha, beta, jaz).ocena;
			}
			else {
				ocena = ZMAGA;
			}
		}
		Tocka kandidat = igra.moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kandidatata. Zato ne more biti null.
		for (Tocka p: igra.moznePoteze) {
			int ocenap;
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
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
	
//	public static OcenjenaPoteza alphabetaPoteze__(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
//		int ocena;
//		int ocenap;
//		boolean preskoci;
//		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
//		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
//		if (igra.naPotezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
//		Tocka kandidat = igra.moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kandidatata. Zato ne more biti null.
//		Set<Tocka> libs1 = new HashSet<Tocka>();
//		for (String imeGrafa : igra.grafi1libs) {
//			Graf g = igra.GRAFI.get(imeGrafa);
//			if (g.lastnik == jaz) {
//				libs1.addAll(g.libs);
//			}
//		}
//		if (!libs1.isEmpty()) {
//			Tocka q = libs1.iterator().next();
//			System.out.println("notri");
//			Igra kopijaIgre = new Igra(igra);
//			kopijaIgre.odigraj(q);
//			switch (kopijaIgre.stanje()) {
//			case ZMAGA_CRNI: ocenap = (jaz == Igralec.CRNI ? ZMAGA : ZGUBA); break;
//			case ZMAGA_BELI: ocenap = (jaz == Igralec.BELI ? ZMAGA : ZGUBA); break;
//			default:
//				ocenap = alphabetaPoteze (kopijaIgre, globina, alpha, beta, jaz).ocena;
//			}
//				if (igra.naPotezi() == jaz) { // Maksimiramo oceno
//					if (ocenap > ocena) { // mora biti > namesto >=
//						ocena = ocenap;
//						kandidat = q;
//						alpha = Math.max(alpha,ocena);
//					}
//				} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
//					if (ocenap < ocena) { // mora biti < namesto <=
//						ocena = ocenap;
//						kandidat = q;
//						beta = Math.min(beta, ocena);					
//					}	
//				}
////				if (alpha >= beta) {// Izstopimo iz "for loop", saj ostale poteze ne pomagajo
////					break;}
//	//		System.out.println(kandidat + ", " + ocena);
//			return new OcenjenaPoteza (kandidat, ocena);
//			}
//		else {
//			for (Tocka p: igra.moznePoteze) {
//				Igra kopijaIgre = new Igra(igra);
//				kopijaIgre.odigraj(p);
//				switch (kopijaIgre.stanje()) {
//				case ZMAGA_CRNI: ocenap = (jaz == Igralec.CRNI ? ZMAGA : ZGUBA); break;
//				case ZMAGA_BELI: ocenap = (jaz == Igralec.BELI ? ZMAGA : ZGUBA); break;
//				default:
//					// Nekdo je na potezi
//					if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
//					else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
//				}
//				if (igra.naPotezi() == jaz) { // Maksimiramo oceno
//					if (ocenap > ocena) { // mora biti > namesto >=
//						ocena = ocenap;
//						kandidat = p;
//						alpha = Math.max(alpha,ocena);
//					}
//				} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
//					if (ocenap < ocena) { // mora biti < namesto <=
//						ocena = ocenap;
//						kandidat = p;
//						beta = Math.min(beta, ocena);					
//					}	
//				}
//				if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
//					break;
//			}
//	//		System.out.println(kandidat + ", " + ocena);
//			return new OcenjenaPoteza (kandidat, ocena);
//		}
//	}
//
//	public static OcenjenaPoteza alphabetaPoteze_(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
//		int ocena;
//		int ocenap;
//		boolean preskoci;
//		// Če sem računalnik, maksimiramo oceno z začetno oceno ZGUBA
//		// Če sem pa človek, minimiziramo oceno z začetno oceno ZMAGA
//		if (igra.naPotezi() == jaz) {ocena = ZGUBA;} else {ocena = ZMAGA;}
////		Set<Tocka> moznePoteze = igra.moznePoteze;
////		for (Graf graf : igra.GRAFI.values()) {
//////		for (Graf graf : kopijaIgre.GRAFI.values()) {
////			if (graf.moc() == 1) {
////				preskoci = true;
//////				Tocka lib = graf.libs.iterator().next();
////				Tocka kandidat = graf.libs.iterator().next();
////				if (graf.lastnik == jaz) {
////					Igra kopijaIgre2 = new Igra(igra);
////					kopijaIgre2.odigraj(kandidat);
////					ocenap = alphabetaPoteze (kopijaIgre2, globina, alpha, beta, jaz).ocena;
////		//				return alphabetaPoteze (kopijaIgre, globina, alpha, beta, jaz);
////				}
////				else {
////					ocenap = ZMAGA;
////				}
////			}
////		}
//		Tocka kandidat = igra.moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kandidatata. Zato ne more biti null.
////		if (!preskoci) {
//		for (Tocka p: igra.moznePoteze) {
////			Tocka kandidat = igra.moznePoteze.iterator().next(); // Možno je, da se ne spremni vrednost kandidatata. Zato ne more biti null.
//			Igra kopijaIgre = new Igra(igra);
//			kopijaIgre.odigraj(p);
////			int ocenap;
//			switch (kopijaIgre.stanje()) {
//			case ZMAGA_CRNI: ocenap = (jaz == Igralec.CRNI ? ZMAGA : ZGUBA); break;
//			case ZMAGA_BELI: ocenap = (jaz == Igralec.BELI ? ZMAGA : ZGUBA); break;
//			default:
//				Set<Tocka> libs1 = new HashSet<Tocka>();
//				for (String imeGrafa : kopijaIgre.grafi1libs) {
//					Graf g = kopijaIgre.GRAFI.get(imeGrafa);
//					if (g.lastnik == jaz) {
//						libs1.addAll(g.libs);
//					}
//				}
//				// Nekdo je na potezi
//				if (globina == 1) ocenap = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
//				else if (!libs1.isEmpty()) {
//					Tocka q = libs1.iterator().next();
//					System.out.println("notri");
//					Igra kopijaIgre2 = new Igra(kopijaIgre);
//					kopijaIgre2.odigraj(q);
//					ocenap = alphabetaPoteze (kopijaIgre2, globina, alpha, beta, jaz).ocena;
//				}
//				else ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;
//			}
//			if (igra.naPotezi() == jaz) { // Maksimiramo oceno
//				if (ocenap > ocena) { // mora biti > namesto >=
//					ocena = ocenap;
//					kandidat = p;
//					alpha = Math.max(alpha,ocena);
//				}
//			} else { // igra.naPotezi() != jaz, torej minimiziramo oceno
//				if (ocenap < ocena) { // mora biti < namesto <=
//					ocena = ocenap;
//					kandidat = p;
//					beta = Math.min(beta, ocena);					
//				}	
//			}
//			if (alpha >= beta) // Izstopimo iz "for loop", saj ostale poteze ne pomagajo
//				break;
//		}
////		System.out.println(kandidat + ", " + ocena);
//		return new OcenjenaPoteza (kandidat, ocena);
//	}
}