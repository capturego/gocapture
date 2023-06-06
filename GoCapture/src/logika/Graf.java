package logika;
//import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
//import logika.Tocka;
import splosno.Poteza;

//public class Graf implements Serializable {
public class Graf {
	
//	static final long serialVersionUID = 1L;

	protected String ime;
	public Igralec lastnik;
	public Set<Poteza> tocke;
	public Set<Poteza> libs;

	
	public Graf (String ime, Igralec lastnik) {
		this.ime = ime;
		this.lastnik = lastnik;
		tocke = new HashSet<Poteza>();
		libs = new HashSet<Poteza>();
	}
	public void dodajTocko (Poteza tocka) {
		tocke.add(tocka);
	}
	public void dodajTocke (Set<Poteza> tocke_) {
		tocke.addAll(tocke_);
	}
	public void dodajLib (Poteza lib) {
		libs.add(lib);
	}
	public void dodajLibs (Set<Poteza> libs_) {
		libs.addAll(libs_);
	}
	public int moc () {
		return libs.size();
	}
	public Graf kopiraj () {
		Graf kopija = new Graf(this.ime, this.lastnik);
		for (Poteza tocka : tocke) {
			kopija.dodajTocko(tocka);
		}
		for (Poteza lib : libs) {
			kopija.libs.add(lib);
		}
		return kopija;
	}
	public String toString () {
		return ime + ", lastnik: " + lastnik + ", stLibs: " + moc() + " <- graf ";
	}
}