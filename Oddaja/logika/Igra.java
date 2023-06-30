package logika;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;


public class Igra implements Serializable {

	private static final long serialVersionUID = 1L;	

	/**
	 * Imena grafov so naravna števila
	 */
	private int imenovalecGrafov;

	/**
	 * Slovar igri pripadajočih svoboščin 
	 */
	public Map<Tocka, Liberty> LIBS;
	
	/**
	 * Slovar igri pripadajočih grafov 
	 */
	public Map<String, Graf> GRAFI;
	
	// Grafi brez svoboščin
	public Set<String> grafi0libs; 

	// Grafi z eno svoboščino 
	public Set<String> grafi1libs; 

	// Velikost igralne pološče je N x N.
	public static final int N = 9;
	
	// Igralno polje
	private Polje[][] plosca;
	
	public Set<Tocka> moznePoteze;
	public Stanje stanje;
		
	/** 
	 * Igralec, ki je trenutno na potezi.
	 * Vrednost je poljubna, če je igre konec (se pravi, lahko je napačna).
	 */
	public Igralec naPotezi;

	/**
	 * Nova igra, v začetni poziciji je prazna in na potezi je črni.
	 */
	public Igra() {
		imenovalecGrafov = 0;
		GRAFI = new HashMap<String, Graf>();
		LIBS = new HashMap<Tocka, Liberty>();
		moznePoteze = new HashSet<Tocka>();
		grafi0libs = new HashSet<String>(); 
		grafi1libs = new HashSet<String>(); 
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
				moznePoteze.add(new Tocka(i,j));
			}
		}
		naPotezi = Igralec.CRNI;
	}

	/**
	 * Kopija igre, za inteligenco.
	 * @param igra
	 */
	public Igra(Igra igra) {
		imenovalecGrafov = igra.imenovalecGrafov;
		GRAFI = new HashMap<String, Graf>();
		grafi0libs = new HashSet<String>(); 
		grafi1libs = new HashSet<String>(); 
		for (String imeGrafa : igra.GRAFI.keySet()) {
			GRAFI.put(imeGrafa, igra.GRAFI.get(imeGrafa).kopiraj());
		}
		this.LIBS = new HashMap<Tocka, Liberty>();
		for (Tocka koordinati : igra.LIBS.keySet()) {
			LIBS.put(koordinati, igra.LIBS.get(koordinati).kopiraj());
		}
		moznePoteze = new HashSet<Tocka>();
		for (Tocka koordinati : igra.moznePoteze) {
			moznePoteze.add(new Tocka(koordinati.x, koordinati.y));
		}
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = igra.plosca[i][j];
			}
		}
		naPotezi = igra.naPotezi;
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
	 * @return novo ime grafa
	 */
	private String imenujGraf() {
		return Integer.toString(imenovalecGrafov++);
	}
	/**
	 * Pregleda svoboščine grafov.
	 */
	private void grafi0libs () {
		grafi0libs.clear();
		for (Graf graf : GRAFI.values()) {
			if (graf.moc() == 0) {
				grafi0libs.add(graf.ime);
			}
		}
	}
	private void grafi1libs () {
		grafi1libs.clear();
		for (Graf graf : GRAFI.values()) {
			if (graf.moc() == 1) {
				grafi1libs.add(graf.ime);
			}
		}
	}

	/**
	 * @return trenutno stanje igre
	 */
	public Stanje stanje() {
		// Ali imamo poraženca?
		grafi0libs();
		grafi1libs();
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
	 * @param poteza
	 * @return true, če je bila poteza uspešno odigrana
	 */
	public boolean odigraj(Tocka poteza) {
		int x = poteza.x; int y = poteza.y;
		// ob vsaki odigrani potezi se ustvari nov ali spremeni najmanj en obstoječ graf
		String imeGrafa = imenujGraf();
		Graf bodocGraf = new Graf(imeGrafa, naPotezi);
		Set<Tocka> sosedi = poteza.sosedi(N);
		if (plosca[x][y] == Polje.PRAZNO) {
			odigrajPrazno(x, y, imeGrafa, bodocGraf, sosedi);
			plosca[x][y] = naPotezi.getPolje();
			naPotezi = naPotezi.nasprotnik();
			moznePoteze.remove(poteza);
			return true;
		}
		else if (plosca[x][y] == Polje.LIBERTY) {
			odigrajLiberty(x, y, imeGrafa, bodocGraf, sosedi);
			plosca[x][y] = naPotezi.getPolje();
			naPotezi = naPotezi.nasprotnik();
			moznePoteze.remove(poteza);
			return true;
		}
		else {
			return false;
		}
	}

	private void odigrajPrazno (int x, int y, String imeGrafa, Graf bodocGraf, Set<Tocka> sosedi) {
			// bodocGraf bo zagotovo vseboval trenutno igrano potezo:
			bodocGraf.dodajTocko(new Tocka(x, y));
			for (Tocka sosed : sosedi) {
				// vse sosednje točke bodo postale svoboščine (če že niso):
				bodocGraf.dodajLib(sosed);
				plosca[sosed.x][sosed.y] = Polje.LIBERTY;
				// preverimo ali ustvarimo novo svoboščino ali bomo dodali graf že obstoječi svoboščini:
				Liberty sosedLib = (LIBS.containsKey(sosed)) ? LIBS.get(sosed) : new Liberty(sosed.x, sosed.y);
				sosedLib.dodajGraf(imeGrafa);
				LIBS.put(sosed, sosedLib);
			}
			GRAFI.put(imeGrafa, bodocGraf);
		}

	private void odigrajLiberty (int x, int y, String imeGrafa, Graf bodocGraf, Set<Tocka> sosedi) {
		Tocka poteza = new Tocka(x, y);
		Liberty taLib = LIBS.get(poteza);
		// preverimo grafe, katerim je Tocka poteza svoboščina
		for (String imeObstojecega : taLib.grafi) {  
			Graf obstojecGraf = GRAFI.get(imeObstojecega);
			obstojecGraf.odstraniLib(x, y);
			// množici sosedov te svoboščine odstranimo vse točke, ki so vsebovane v grafih
			sosedi.removeAll(obstojecGraf.tocke);
			// če je graf od igralca, ki je odigral potezo
			if (obstojecGraf.lastnik == naPotezi) {
				// vse igralčeve grafe, katerim je to svoboščina, združimo v enega - bodocGraf
				bodocGraf.dodajLibs(obstojecGraf.libs);
				bodocGraf.dodajTocke(obstojecGraf.tocke);
				GRAFI.remove(imeObstojecega);
				// vse svoboščine obstoječega grafa (razen Tocka poteza) 
				for (Liberty lib : LIBS.values()) {
					if ((!lib.equals(taLib)) && lib.vsebujeGraf(imeObstojecega)) {
						lib.odstraniGraf(imeObstojecega);
						lib.dodajGraf(imeGrafa);
					}
				}
			}
			else { // če je graf od nasprotnika 
				GRAFI.put(imeObstojecega, obstojecGraf);
			}
		}
		for (Tocka sosed : sosedi) {  // iz sosedi smo že odstranili odigrane poteze
			plosca[sosed.x][sosed.y] = Polje.LIBERTY;
			// sosed je lahko že svoboščina, to preverimo
			Liberty lib = LIBS.containsKey(sosed) ? LIBS.get(sosed) : new Liberty(sosed.x, sosed.y);
			// graf in svoboščina morata vedeti en za drugega
			bodocGraf.dodajLib(sosed);
			lib.dodajGraf(imeGrafa);
			// igra mora vedeti za te nove svoboščine
			LIBS.put(sosed, lib);
		}
		// Končamo beleženje spremembe v igri
		bodocGraf.dodajTocko(poteza);
		GRAFI.put(imeGrafa, bodocGraf);
		LIBS.remove(poteza);
		}
}