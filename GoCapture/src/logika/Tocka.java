package logika;
import java.io.Serializable;
import java.util.HashSet;
import splosno.Poteza;


public class Tocka implements Serializable {
	
	static final long serialVersionUID = 1L;
	
	public int x;
	public int y;
	
	public Tocka (int x, int y) {
		this.x = x; this.y = y;
	}

	public Tocka (Poteza poteza) {
		this.x = poteza.x(); this.y = poteza.y();
	}

	public boolean equals (Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Tocka t = (Tocka) o;
		return this.x == t.x && this.y == t.y;
	}

	public int hashCode () {
		int x = this.x; int y = this.y;
		return (x+y) * (x+y+1) / 2 + y;
	}

	public HashSet<Tocka> sosedi (int velikostPlosce) {
		HashSet<Tocka> sosedi = new HashSet<Tocka>();
		if (x == 0) {
			sosedi.add(new Tocka(1, y));
		}
		else if (x == velikostPlosce-1) {
			sosedi.add(new Tocka(velikostPlosce-2, y));
		}
		else {
			sosedi.add(new Tocka(x-1, y));
			sosedi.add(new Tocka(x+1, y));
		}
		if (y == 0) {
			sosedi.add(new Tocka(x, 1));
		}
		else if (y == velikostPlosce-1) {
			sosedi.add(new Tocka(x, velikostPlosce-2));
		}
		else {
			sosedi.add(new Tocka(x, y-1));
			sosedi.add(new Tocka(x, y+1));
		}
		return sosedi;
	}

	public String toString () {
		return Integer.toString(x) + "-" + Integer.toString(y); //xy
	}
}