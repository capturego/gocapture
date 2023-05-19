package logika;
//import java.io.Serializable;
import java.util.HashSet;
//import logika.Tocka;

//public class Graf implements Serializable {
public class Graf {
	
//	static final long serialVersionUID = 1L;

	protected String ime;
//	protected int stevecLibs = 0;  // stevec -> liberties (stevec)
	protected Igralec lastnik;
	public HashSet<String> tocke;
	public HashSet<String> libs;

	
	public Graf (String ime, Igralec lastnik) {
		this.ime = ime;
		this.lastnik = lastnik;
		tocke = new HashSet<String>();
		libs = new HashSet<String>();
	}
	public void dodajTocko (String ime) {
		tocke.add(ime);
	}
	public void dodajTocke (HashSet<String> tocke_) {
		tocke.addAll(tocke_);
	}
	public void dodajLib (String ime) {
		libs.add(ime);
	}
	public void dodajLibs (HashSet<String> libs_) {
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