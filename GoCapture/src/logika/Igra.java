package logika;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import splosno.Poteza;


public class Igra {

	
	// Velikost igralne pološče je N x N.
	public static final int N = 9;

	private int imenovalecGrafov;
	public Map<Poteza, Liberty> LIBS;
	public Map<String, Graf> GRAFI;
	public Set<Poteza> moznePoteze;
	public Stanje stanje_;
	public Graf ujetiGraf;
//	public Stanje stanje_ = Stanje.V_TEKU;
	
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
		LIBS = new HashMap<Poteza, Liberty>();
		moznePoteze = new HashSet<Poteza>();
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
				moznePoteze.add(new Poteza(i,j));
			}
		}
		naPotezi = Igralec.CRNI;
	}

	public Igra(Igra igra) {
		imenovalecGrafov = igra.imenovalecGrafov;
		GRAFI = new HashMap<String, Graf>();
		for (String imeGrafa : igra.GRAFI.keySet()) {
			this.GRAFI.put(imeGrafa, igra.GRAFI.get(imeGrafa).kopiraj());
		}
		this.LIBS = new HashMap<Poteza, Liberty>();
		for (Poteza poteza : igra.LIBS.keySet()) {
			this.LIBS.put(poteza, igra.LIBS.get(poteza).kopiraj());
		}
		moznePoteze = new HashSet<Poteza>();
		for (Poteza poteza : igra.moznePoteze) {
			moznePoteze.add(new Poteza(poteza.x(), poteza.y()));
		}
		this.plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				this.plosca[i][j] = igra.plosca[i][j];
			}
		}
		this.naPotezi = igra.naPotezi;
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

	private String imenujGraf() {
		return Integer.toString(imenovalecGrafov++);
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
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		HashSet<String> grafi0libs = grafi0libs();
		if (!grafi0libs.isEmpty()) {
			for (String nasprotnikovGraf : grafi0libs) {
				Graf ujetiGraf_ = GRAFI.get(nasprotnikovGraf);
				if (ujetiGraf_.lastnik == naPotezi) {
					ujetiGraf = ujetiGraf_;
					if (naPotezi == Igralec.BELI) {return Stanje.ZMAGA_CRNI;}
//					else if (naPotezi == Igralec.CR) {return Stanje.ZMAGA_X;}
					else {return Stanje.ZMAGA_BELI;}
					}
				}
			if (naPotezi == Igralec.BELI) {
//				ujetiGraf = ujetiGraf_;
				return Stanje.ZMAGA_BELI;}
			else {
//				ujetiGraf = ujetiGraf_;
				return Stanje.ZMAGA_CRNI;}
		}
		else {
			return Stanje.V_TEKU;
		}
	}

	/**
	 * Odigraj potezo p.
	 * 
	 * @param p
	 * @return true, če je bila poteza uspešno odigrana
	 */
	public boolean odigraj(Poteza poteza) {
		int x = poteza.x();
		int y = poteza.y();
		Tocka tocka = new Tocka(poteza);
		String imeGrafa = imenujGraf();
		Graf bodocGraf = new Graf(imeGrafa, naPotezi);
		Set<Poteza> sosedi = tocka.sosedi(N);
		if (plosca[x][y] == Polje.PRAZNO) {  // => nov graf, preveri nove libertije in jim po potrebi dodaj ta graf
			plosca[x][y] = naPotezi.getPolje();
			bodocGraf.dodajTocko(poteza);
			for (Poteza sosed_ : sosedi) {
				Tocka sosed = new Tocka(sosed_);
				plosca[sosed.x][sosed.y] = Polje.LIBERTY;
				Liberty sosedLib = (LIBS.containsKey(sosed_)) ? LIBS.get(sosed_) : new Liberty(sosed_);
				bodocGraf.dodajLib(sosed_);
				sosedLib.dodajGraf(imeGrafa);
				LIBS.put(sosed_, sosedLib);
			}
			this.GRAFI.put(imeGrafa, bodocGraf);
			naPotezi = naPotezi.nasprotnik();
			moznePoteze.remove(poteza);
			return true;
		}
		else if (plosca[x][y] == Polje.LIBERTY) {
			plosca[x][y] = naPotezi.getPolje();
			Liberty taLib = LIBS.get(poteza);
			Set<Poteza> libsToUpdate = new HashSet<Poteza>();
			for (String imeGrafa_ : taLib.grafi) {  // preverimo vse grafe katerim je to liberty
				Graf graf_ = this.GRAFI.get(imeGrafa_);
				sosedi.removeAll(graf_.tocke);
				if (graf_.lastnik == naPotezi) {  // => bomo zdruzevali grafu/e
					graf_.libs.remove(poteza);
					bodocGraf.dodajLibs(graf_.libs);
					bodocGraf.dodajTocke(graf_.tocke);
					this.GRAFI.remove(imeGrafa_);
					for (Liberty lib : LIBS.values()) {
						if ((!lib.equals(taLib)) && lib.grafi.contains(imeGrafa_)) {
							libsToUpdate.add(new Poteza(lib.x, lib.y));
							lib.grafi.remove(imeGrafa_);
							lib.dodajGraf(imeGrafa);
						}
					}
				} 
				else {  // to lib nasprotnikovim grafom -> tem grafom samo odvzamemo ta lib
					graf_.libs.remove(new Poteza(taLib.x, taLib.y));
					this.GRAFI.put(imeGrafa_, graf_);
				}
			}
			for (Poteza sosed_ : sosedi) {  // preverimo vse sosede
				Tocka sosed = new Tocka(sosed_);
				plosca[sosed.x][sosed.y] = Polje.LIBERTY;
				Liberty lib = LIBS.containsKey(sosed_) ? LIBS.get(sosed_) : new Liberty(sosed_);
				bodocGraf.dodajLib(sosed_);
				lib.dodajGraf(imeGrafa);
				LIBS.put(sosed_, lib);
			}
			bodocGraf.dodajTocko(poteza);
			for (Poteza lib_ : libsToUpdate) {
				Liberty lib = LIBS.get(lib_);
				lib.dodajGraf(imeGrafa);
				LIBS.put(lib_, lib);
			}
			this.GRAFI.put(imeGrafa, bodocGraf);
			LIBS.remove(poteza);
			naPotezi = naPotezi.nasprotnik();
			moznePoteze.remove(poteza);
			return true;
		}
		else {
			return false;
		}
	}
}
