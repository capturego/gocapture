package logika;
//import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
//import logika.Tocka;

//public class Graf implements Serializable {
public class Graf {
	
//	static final long serialVersionUID = 1L;

	protected String ime;
//	protected int stevecLibs = 0;  // stevec -> liberties (stevec)
	public Igralec lastnik;
	public Set<String> tocke;
	public Set<String> libs;

	
	public Graf (String ime, Igralec lastnik) {
		this.ime = ime;
		this.lastnik = lastnik;
		tocke = new HashSet<String>();
		libs = new HashSet<String>();
	}
	public void dodajTocko (String ime) {
		tocke.add(ime);
	}
	public void dodajTocke (Set<String> tocke_) {
		tocke.addAll(tocke_);
	}
	public void dodajLib (String ime) {
		libs.add(ime);
	}
	public void dodajLibs (Set<String> libs_) {
		libs.addAll(libs_);
	}
	public int moc () {
		return libs.size();
	}
	public String toString () {
//		for (String x : libs) {
//			System.out.print(x + ", ");
//		}
		return ime + ", lastnik: " + lastnik + ", stLibs: " + moc() + " <- graf";
	}
}