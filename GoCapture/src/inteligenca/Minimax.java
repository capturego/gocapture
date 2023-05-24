package inteligenca;

import java.util.List;

import logika.Igra;
import logika.Igralec;

import splosno.Poteza;

//import inteligenca.OceniPozicijo;

public class Minimax extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	private static final int NEODLOC = 0;  // vrednost neodločene igre	
	
	private int globina;
	
	public Minimax (int globina) {
		this.globina = globina;
	}
	
	@Override
	public Poteza izberiPotezo (Igra igra) {
		OcenjenaPoteza najboljsaPoteza = minimax(igra, this.globina, igra.naPotezi());
		return najboljsaPoteza.poteza;	
	}
	
	// vrne najboljso ocenjeno potezo z vidike igralca jaz
	public OcenjenaPoteza minimax(Igra igra, int globina, Igralec jaz) {
		System.out.println(jaz);
		OcenjenaPoteza najboljsaPoteza = null;
		List<Poteza> moznePoteze = igra.poteze();
		for (Poteza p: moznePoteze) {
			System.out.println(p);
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
//			System.out.println(igra.GRAFI);
//			System.out.println(kopijaIgre.GRAFI);
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_O: ocena = (jaz == Igralec.CR ? ZMAGA : ZGUBA); break;
			case ZMAGA_X: ocena = (jaz == Igralec.BE ? ZMAGA : ZGUBA); break;
//			case ZMAGA_O: ocena = (jaz == Igralec.BE ? ZMAGA : ZGUBA); break;
//			case ZMAGA_X: ocena = (jaz == Igralec.CR ? ZMAGA : ZGUBA); break;
			case NEODLOCENO: ocena = NEODLOC; break;
			default:
				// nekdo je na potezi
				if (globina == 1) ocena = OceniPozicijo.oceniPozicijo(kopijaIgre, jaz);
				// globina > 1
				else ocena = minimax(kopijaIgre, globina-1, jaz).ocena;	
			}
			System.out.println("ocena =  " + ocena);
			if (najboljsaPoteza == null 
					// max, če je p moja poteza
					|| jaz == igra.naPotezi() && ocena > najboljsaPoteza.ocena
					// sicer min 
					|| jaz != igra.naPotezi() && ocena < najboljsaPoteza.ocena)
				najboljsaPoteza = new OcenjenaPoteza (p, ocena);		
				System.out.println(najboljsaPoteza);
		}
		return najboljsaPoteza;
	}
}