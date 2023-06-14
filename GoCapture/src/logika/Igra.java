package logika;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;


public class Igra implements Serializable {

	private static final long serialVersionUID = 1L;	
	// Velikost igralne pološče je N x N.
	public static final int N = 9;

	// Imena grafov so naravna števila
	private int imenovalecGrafov;

	// Liberty ima Set<String> grafi, 
	// kjer so shranjena imena njej pripadajočih grafov
	public Map<Tocka, Liberty> LIBS;
	
	// Graf ima Igralec lastnik, Set<Tocka> tocke in Set<Tocka> libs.
	// Torej lastnika, točke, ki so koordinate pripadajočih žetonov ter
	// libs, ki so njegove svoboščine.
	public Map<String, Graf> GRAFI;
	public Set<Tocka> moznePoteze;
	public Stanje stanje;
	public Set<String> grafi0libs; 
	
	// Igralno polje
	private Polje[][] plosca;
	
		
	// Igralec, ki je trenutno na potezi.
	// Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	public Igralec naPotezi;

	/**
	 * Nova igra, v začetni poziciji je prazna in na potezi je O.
	 */
	public Igra() {
		imenovalecGrafov = 0;
		GRAFI = new HashMap<String, Graf>();
		LIBS = new HashMap<Tocka, Liberty>();
		moznePoteze = new HashSet<Tocka>();
		grafi0libs = new HashSet<String>(); 
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
				moznePoteze.add(new Tocka(i,j));
			}
		}
		naPotezi = Igralec.CRNI;
	}

	public Igra(Igra igra) {
		imenovalecGrafov = igra.imenovalecGrafov;
		GRAFI = new HashMap<String, Graf>();
		grafi0libs = new HashSet<String>(); 
		for (String imeGrafa : igra.GRAFI.keySet()) {
			this.GRAFI.put(imeGrafa, igra.GRAFI.get(imeGrafa).kopiraj());
		}
		this.LIBS = new HashMap<Tocka, Liberty>();
		for (Tocka poteza : igra.LIBS.keySet()) {
			this.LIBS.put(poteza, igra.LIBS.get(poteza).kopiraj());
		}
		moznePoteze = new HashSet<Tocka>();
		for (Tocka poteza : igra.moznePoteze) {
			moznePoteze.add(new Tocka(poteza.x, poteza.y));
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

	private void grafi0libs () {
		grafi0libs.clear();
		for (Graf graf : GRAFI.values()) {
			if (graf.moc() == 0) {
				grafi0libs.add(graf.ime);
			}
		}
	}

	/**
	 * @return trenutno stanje igre
	 */
	public Stanje stanje() {
		// Ali imamo zmagovalca?
		grafi0libs();
		if (!grafi0libs.isEmpty()) {
			for (String nasprotnikovGraf : grafi0libs) {
				Graf ujetiGraf_ = GRAFI.get(nasprotnikovGraf);
				if (ujetiGraf_.lastnik == naPotezi) {
					if (naPotezi == Igralec.BELI) {return Stanje.ZMAGA_CRNI;}
					else {return Stanje.ZMAGA_BELI;}
					}
				}
			if (naPotezi == Igralec.BELI) {
				return Stanje.ZMAGA_BELI;}
			else {
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
	public boolean odigraj(Tocka poteza) {
		int x = poteza.x;
		int y = poteza.y;
		String imeGrafa = imenujGraf();
		Graf bodocGraf = new Graf(imeGrafa, naPotezi);
		Set<Tocka> sosedi = poteza.sosedi(N);
		if (plosca[x][y] == Polje.PRAZNO) {  // => nov graf, preveri nove libertije in jim po potrebi dodaj ta graf
			plosca[x][y] = naPotezi.getPolje();
			bodocGraf.dodajTocko(poteza);
			for (Tocka sosed : sosedi) {
				plosca[sosed.x][sosed.y] = Polje.LIBERTY;
				Liberty sosedLib = (LIBS.containsKey(sosed)) ? LIBS.get(sosed) : new Liberty(sosed.x, sosed.y);
				bodocGraf.dodajLib(sosed);
				sosedLib.dodajGraf(imeGrafa);
				LIBS.put(sosed, sosedLib);
			}
			this.GRAFI.put(imeGrafa, bodocGraf);
			naPotezi = naPotezi.nasprotnik();
			moznePoteze.remove(poteza);
			return true;
		}
		else if (plosca[x][y] == Polje.LIBERTY) {
			plosca[x][y] = naPotezi.getPolje();
			Liberty taLib = LIBS.get(poteza);
			Set<Tocka> libsToUpdate = new HashSet<Tocka>();
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
							libsToUpdate.add(new Tocka(lib.x, lib.y));
							lib.grafi.remove(imeGrafa_);
							lib.dodajGraf(imeGrafa);
						}
					}
				} 
				else {  // to lib nasprotnikovim grafom -> tem grafom samo odvzamemo ta lib
					graf_.libs.remove(new Tocka(taLib.x, taLib.y));
					this.GRAFI.put(imeGrafa_, graf_);
				}
			}
			for (Tocka sosed : sosedi) {  // preverimo vse sosede
				plosca[sosed.x][sosed.y] = Polje.LIBERTY;
				Liberty lib = LIBS.containsKey(sosed) ? LIBS.get(sosed) : new Liberty(sosed.x, sosed.y);
				bodocGraf.dodajLib(sosed);
				lib.dodajGraf(imeGrafa);
				LIBS.put(sosed, lib);
			}
			bodocGraf.dodajTocko(poteza);
			for (Tocka lib_ : libsToUpdate) {
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
