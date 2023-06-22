package logika;
import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;

/**
 * Grafi predstavljajo množice povezanih točk istega igralca
 */

public class Graf implements Serializable {
	
	static final long serialVersionUID = 1L;

	protected String ime;
	/**
	 * Lastnik grafa je eden od igralcev
	 */
	public Igralec lastnik;
	public Set<Tocka> tocke;
	public Set<Tocka> libs;

	
	public Graf (String ime_, Igralec lastnik_) {
		ime = ime_;
		lastnik = lastnik_;
		tocke = new HashSet<Tocka>();
		libs = new HashSet<Tocka>();
	}
	public void dodajTocko (Tocka tocka) {
		tocke.add(tocka);
	}

	public void dodajTocke (Set<Tocka> tocke_) {
		tocke.addAll(tocke_);
	}

	public void dodajLib (Tocka lib) {
		libs.add(lib);
	}

	public void dodajLibs (Set<Tocka> libs_) {
		libs.addAll(libs_);
	}
	public void odstraniLib (int x, int y) {
		libs.remove(new Tocka(x, y));
	}

	public int moc () {
		return libs.size();
	}

	public Graf kopiraj () {
		Graf kopija = new Graf(this.ime, this.lastnik);
		for (Tocka tocka : tocke) {
			kopija.dodajTocko(tocka);
		}
		for (Tocka lib : libs) {
			kopija.libs.add(lib);
		}
		return kopija;
	}

	public String toString () {
		return ime + ", lastnik: " + lastnik + ", stLibs: " + moc() + " <- graf ";
	}
}