package logika;
//import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
//import logika.Tocka;

//public class Graf implements Serializable {
public class Liberty  { // extends Tocka

//	static final long serialVersionUID = 1L;

//	private int stevecGrafov = 0;
	protected HashSet<String> grafi;
	protected String ime;
//	private Igralec lastnik;
	
	public Liberty (String ime) {
		this.ime = ime;
		grafi = new HashSet<String>();
	}
//	public Graf graf (String ime) {
//		return grafi.get(ime);
//	}
//	public boolean povezava (Tocka a, Tocka b) {
//		return a.sosedi.contains(b);
//	}
	public void dodajGraf (String ime) {
		grafi.add(ime);
	}
//	public Graf dodajGraf (String ime, Igralec lastnik) {
//		if (graf(ime) != null) {
//			return graf(ime);
//		} else {
//			Graf T0 = new Graf(ime, lastnik);
//			grafi.put(ime, T0);
//			return T0;
//		}
//	} 
//	public Tocka dodajTocko () {
//		if (tocka(Integer.toString(stevec)) != null) {
//			stevec++;
//			return dodajTocko();
//		} else {
//			String ime = Integer.toString(stevec);
//			Tocka T0 = new Tocka(ime);
//			tocke.put(ime, T0);
//			return T0;	
//		}
//	}
//	public void dodajPovezavo (Tocka a, Tocka b) {
//		if (a.toString() != b.toString()) {
//			a.sosedi.add(b);
//			b.sosedi.add(a);
//		}
//	}
//	public void odstraniPovezavo (Tocka a, Tocka b) {
//		if (povezava(a, b)) {
//			a.sosedi.remove(b);
//			b.sosedi.remove(a);
//		}
//	}
//	public void izpis () {
//		System.out.println("Tocke:     Povezave:");
//		for (Graf x : grafi.values()) {
//			String tabX = "";
//			for (int i=0; i<(11 - x.toString().length()); i++) {
//				tabX += " ";
//			}
//			System.out.print(x + tabX);
////			for (Graf y : x.sosedi) {
////				String tabY = "";
////				for (int i=0; i<(5 - x.toString().length()); i++) {
////					tabY += " ";
////				}
////				System.out.print(y.toString() + tabY);
////			}
////			System.out.println();
//		}
//	}
	public String toString () {
//		return "liberty return ime?";
		return ime + " <- liberty";
	}
}