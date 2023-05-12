package logika;

import java.util.LinkedList;
import java.util.List;
import java.util.EnumMap;
import java.util.Map;

import splosno.Poteza;

public class Igra {

	
	// Velikost igralne pološče je N x N.
	public static final int N = 9;

	
	// Pomožen seznam vseh vrstah na plošči.
	private static final List<Vrsta> VRSTE = new LinkedList<Vrsta>();

//	private static final Map<Igralec, LinkedList<Graf>> GRAFI = new EnumMap<Igralec, LinkedList<Graf>>();
	// Igralno polje
	private Polje[][] plosca;
	
		
	// Igralec, ki je trenutno na potezi.
	// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	private Igralec naPotezi;

	/**
	 * Nova igra, v začetni poziciji je prazna in na potezi je O.
	 */
	public Igra() {
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		naPotezi = Igralec.CR;
	}
	
	/**
	 * @return igralca na potezi
	 */
	public Igralec naPotezi () {
		return naPotezi;
	}
	
	/**
	 * @return igralna plosca
	 */
	public Polje[][] getPlosca () {
		return plosca;
	}

	/**
	 * @return seznam možnih potez
	 */
	public List<Poteza> poteze() {
		LinkedList<Poteza> ps = new LinkedList<Poteza>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					ps.add(new Poteza(i, j));
				}
			}
		}
		return ps;
	}

	/**
	 * @param t
	 * @return igralec, ki ima zapolnjeno vrsto @{t}, ali {@null}, če nihče
	 */
	private Igralec cigavaVrsta(Vrsta t) {
		int count_X = 0;
		int count_O = 0;
		for (int k = 0; k < N && (count_X == 0 || count_O == 0); k++) {
			switch (plosca[t.x[k]][t.y[k]]) {
			case CR: count_O += 1; break;
			case BE: count_X += 1; break;
			case PRAZNO: break;
			}
		}
		if (count_O == N) { return Igralec.CR; }
		else if (count_X == N) { return Igralec.BE; }
		else { return null; }
	}

	/**
	 * @return zmagovalna vrsta, ali {@null}, če je ni
	 */
	public Vrsta zmagovalnaVrsta() {
		for (Vrsta t : VRSTE) {
			Igralec lastnik = cigavaVrsta(t);
			if (lastnik != null) return t;
		}
		return null;
	}
	
	/**
	 * @return trenutno stanje igre
	 */
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		Vrsta t = zmagovalnaVrsta();
		if (t != null) {
			switch (plosca[t.x[0]][t.y[0]]) {
			case CR: return Stanje.ZMAGA_O; 
			case BE: return Stanje.ZMAGA_X;
			case PRAZNO: assert false;
			}
		}
		// Ali imamo kakšno prazno polje?
		// Če ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) return Stanje.V_TEKU;
			}
		}
		// Polje je polno, rezultat je neodločen
		return Stanje.NEODLOCENO;
	}

	/**
	 * Odigraj potezo p.
	 * 
	 * @param p
	 * @return true, če je bila poteza uspešno odigrana
	 */
	public boolean odigraj(Poteza p) {
		if (plosca[p.getX()][p.getY()] == Polje.PRAZNO) {
			
			plosca[p.getX()][p.getY()] = naPotezi.getPolje();
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}
}
