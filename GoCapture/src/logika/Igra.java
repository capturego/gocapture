package logika;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import logika.Graf;
import logika.Tocka;

import splosno.Poteza;

public class Igra {

	
	// Velikost igralne pološče je N x N.
	public static final int N = 9;

	private int imenovalecGrafov;
	public static HashMap<String, Liberty> LIBS;
	public static HashMap<String, Graf> GRAFI;
	
	// Pomožen seznam vseh vrstah na plošči.
	private static final List<Vrsta> VRSTE = new LinkedList<Vrsta>();

	// Igralno polje
	private Polje[][] plosca;
	
		
	// Igralec, ki je trenutno na potezi.
	// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	private Igralec naPotezi;

	/**
	 * Nova igra, v začetni poziciji je prazna in na potezi je O.
	 */
	public Igra() {
		imenovalecGrafov = 0;
		GRAFI = new HashMap<String, Graf>();
		LIBS = new HashMap<String, Liberty>();
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
	public List<Koordinati> poteze() {
		LinkedList<Koordinati> ps = new LinkedList<Koordinati>();
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO || plosca[i][j] == Polje.LIBERTY) {
					ps.add(new Koordinati(i, j));
				}
			}
		}
		return ps;
	}
	private String imenujGraf() {
		return Integer.toString(imenovalecGrafov++);
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
	
	private HashSet<String> grafi0libs () {
		HashSet<String> grafi0libs = new HashSet<String>();
//		HashSet<String> grafi0libs = null;
		for (Graf graf : GRAFI.values()) {
			if (graf.moc() == 0) {
				grafi0libs.add(graf.ime);
			}
		}
		return grafi0libs;
	}
	/**
	 * @return trenutno stanje igre
	 */
//	public Stanje stanje_() {
//		// Ali imamo zmagovalca?
//		Vrsta t = zmagovalnaVrsta();
//		if (t != null) {
//			switch (plosca[t.x[0]][t.y[0]]) {
//			case CR: return Stanje.ZMAGA_O; 
//			case BE: return Stanje.ZMAGA_X;
//			case PRAZNO: assert false;
//			}
//		}
//		// Ali imamo kakšno prazno polje?
//		// Če ga imamo, igre ni konec in je nekdo na potezi
//		for (int i = 0; i < N; i++) {
//			for (int j = 0; j < N; j++) {
//				if (plosca[i][j] == Polje.PRAZNO) return Stanje.V_TEKU;
//			}
//		}
//		// Polje je polno, rezultat je neodločen
//		return Stanje.NEODLOCENO;
//	}
	public Stanje stanje_() {
		// Ali imamo zmagovalca?
//		for 
		HashSet<String> grafi0libs = grafi0libs();
//		Vrsta t = zmagovalnaVrsta();
		if (!grafi0libs.isEmpty()) {
			for (String nasprotnikovGraf : grafi0libs) {
				if (GRAFI.get(nasprotnikovGraf).lastnik != naPotezi) {
					switch (naPotezi) {
					case CR: return Stanje.ZMAGA_O; 
					case BE: return Stanje.ZMAGA_X;
//					default: return Stanje.NEODLOCENO;
					}
				}
			}
			for (String lastenGraf : grafi0libs) {
				if (GRAFI.get(lastenGraf).lastnik == naPotezi) {
					switch (naPotezi) {
					case CR: return Stanje.ZMAGA_X; 
					case BE: return Stanje.ZMAGA_O;
//					default: return Stanje.NEODLOCENO;
					}
				}
				else {
					return Stanje.NEODLOCENO;
				}
			}
		}
		// Ali imamo kakšno prazno polje?
		// Če ga imamo, igre ni konec in je nekdo na potezi
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO || plosca[i][j] == Polje.LIBERTY) return Stanje.V_TEKU;
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
	public boolean odigraj(Koordinati p) {
		int x = p.getX();
		int y = p.getY();
		String imeTocke = Tocka.toString(x,y); //xy
		String imeGrafa = imenujGraf();
		Graf bodocGraf = new Graf(imeGrafa, naPotezi);
		HashSet<String> sosedi = Tocka.sosedi(x, y, N);
		if (plosca[x][y] == Polje.PRAZNO) {  // => nov graf, preveri nove libertije in jim po potrebi dodaj ta graf
			plosca[x][y] = naPotezi.getPolje();
			bodocGraf.dodajTocko(imeTocke);
			for (String imeSoseda : sosedi) {
//				System.out.print(ime_s + ", ");
				String[] xy = imeSoseda.split("-");
				plosca[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])] = Polje.LIBERTY;
				Liberty sosedLib = (LIBS.containsKey(imeSoseda)) ? LIBS.get(imeSoseda) : new Liberty(imeSoseda);
				bodocGraf.dodajLib(imeSoseda);
				sosedLib.dodajGraf(imeGrafa);
				LIBS.put(imeSoseda, sosedLib);
			}
			GRAFI.put(imeGrafa, bodocGraf);
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else if (plosca[x][y] == Polje.LIBERTY) {
			plosca[x][y] = naPotezi.getPolje();
			Liberty taLib = LIBS.get(imeTocke);
			HashSet<String> libsToUpdate = new HashSet<String>();
			for (String imeGrafa_ : taLib.grafi) {  // preverimo vse grafe katerim je to liberty
				Graf graf_ = GRAFI.get(imeGrafa_);
				sosedi.removeAll(graf_.tocke);
				if (graf_.lastnik == naPotezi) {  // => bomo zdruzevali grafu/e
					graf_.libs.remove(imeTocke);
					bodocGraf.dodajLibs(graf_.libs);
					bodocGraf.dodajTocke(graf_.tocke);
					GRAFI.remove(imeGrafa_);
					for (Liberty lib : LIBS.values()) {
						if ((!lib.equals(taLib)) && lib.grafi.contains(imeGrafa_)) {
							libsToUpdate.add(lib.ime);
							lib.grafi.remove(imeGrafa_);
							lib.dodajGraf(imeGrafa);
						}
					}
				} 
				else {  // to lib nasprotnikovim grafom -> tem grafom samo odvzamemo ta lib
					graf_.libs.remove(taLib.ime);
					GRAFI.put(imeGrafa_, graf_);
				}
			}
			for (String imeSoseda : sosedi) {  // preverimo vse sosede
				String[] xy = imeSoseda.split("-");
				plosca[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])] = Polje.LIBERTY;
				Liberty lib = LIBS.containsKey(imeSoseda) ? LIBS.get(imeSoseda) : new Liberty(imeSoseda);
				bodocGraf.dodajLib(imeSoseda);
				lib.dodajGraf(imeGrafa);
				LIBS.put(imeSoseda, lib);
			}
			bodocGraf.dodajTocko(imeTocke);
			for (String imeLiba : libsToUpdate) {
				Liberty lib = LIBS.get(imeLiba);
				lib.dodajGraf(imeGrafa);
				LIBS.put(imeLiba, lib);
			}
			GRAFI.put(imeGrafa, bodocGraf);
			LIBS.remove(imeTocke);
			naPotezi = naPotezi.nasprotnik();
			return true;
		}
		else {
			return false;
		}
	}
}
