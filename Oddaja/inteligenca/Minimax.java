package inteligenca;

import java.util.Set;

import logika.Igra;
import logika.Igralec;
import logika.Tocka;
import splosno.Poteza;

public class Minimax extends Inteligenca {
	
	private static final int ZMAGA = Integer.MAX_VALUE; // vrednost zmage
	private static final int ZGUBA = -ZMAGA;  // vrednost izgube
	
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
		Set<Tocka> moznePoteze = igra.moznePoteze;
		for (Tocka p: moznePoteze) {
			System.out.println(p);
			Igra kopijaIgre = new Igra(igra);
			kopijaIgre.odigraj(p);
			int ocena;
			switch (kopijaIgre.stanje()) {
			case ZMAGA_CRNI: ocena = (jaz == Igralec.CRNI ? ZMAGA : ZGUBA); break;
			case ZMAGA_BELI: ocena = (jaz == Igralec.BELI ? ZMAGA : ZGUBA); break;
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