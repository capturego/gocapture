package logika;
import java.util.Set;
import java.util.HashSet;


public class Liberty extends Tocka {

	static final long serialVersionUID = 1L;

	public Set<String> grafi;
		
	public Liberty (int x, int y) {
		super(x, y);
		grafi = new HashSet<String>();
	}
//	public Liberty (Poteza poteza) {
//		super(poteza);
//		grafi = new HashSet<String>();
//	}
	public void dodajGraf (String ime) {
		grafi.add(ime);
	}
	public Liberty kopiraj () {
		Liberty kopija = new Liberty(this.x, this.y);
		for (String imeGrafa : grafi) {
			kopija.grafi.add(imeGrafa);
		}
		return kopija;
	}
	public String toString () {
		return "(" + x + ", " + y + ")" + " <- liberty";
	}
}