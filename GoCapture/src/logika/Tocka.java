package logika;
//import java.io.Serializable;
import java.util.HashSet;
import splosno.Poteza;

//public class Tocka implements Serializable {
public class Tocka {
	
//	static final long serialVersionUID = 1L;
	
	protected String ime;
	protected int x;
	protected int y;
	
	public Tocka (int x, int y) {
		this.x = x; this.y = y;
	}

	public Tocka (Poteza poteza) {
		this.x = poteza.x(); this.y = poteza.y();
	}

	public HashSet<Poteza> sosedi (int velikostPlosce) {
		HashSet<Poteza> sosedi = new HashSet<Poteza>();
		if (x == 0) {
			sosedi.add(new Poteza(1, y));
		}
		else if (x == velikostPlosce-1) {
			sosedi.add(new Poteza(velikostPlosce-2, y));
		}
		else {
			sosedi.add(new Poteza(x-1, y));
			sosedi.add(new Poteza(x+1, y));
		}
		if (y == 0) {
			sosedi.add(new Poteza(x, 1));
		}
		else if (y == velikostPlosce-1) {
			sosedi.add(new Poteza(x, velikostPlosce-2));
		}
		else {
			sosedi.add(new Poteza(x, y-1));
			sosedi.add(new Poteza(x, y+1));
		}
		return sosedi;
	}
//	@Override  //  //??
	public String toString () {
		return Integer.toString(x) + "-" + Integer.toString(y); //xy
	}
}