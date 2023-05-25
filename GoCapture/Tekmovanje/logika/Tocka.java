package logika;
//import java.io.Serializable;
import java.util.HashSet;

//public class Tocka implements Serializable {
public class Tocka {
	
//	static final long serialVersionUID = 1L;
	
	protected String ime;
//	protected HashSet<Tocka> sosedi;
	protected int x;
	protected int y;
	
	public Tocka (String ime) {
		this.ime = ime;
//		this.sosedi = new HashSet<Tocka>();
//		x = 0;
//		y = 0;
	}
	public static HashSet<String> sosedi (int x, int y, int velikostPlosce) {
		HashSet<String> sosedi = new HashSet<String>();
//		x = Integer.parseInt(xy[0]);
//		y = Integer.parseInt(xy[1]);
//		String x_ = Integer.toString(x);
//		String y_ = Integer.toString(y);
		if (x == 0) {
			sosedi.add("1-" + y);
		}
		else if (x == velikostPlosce-1) {
			sosedi.add((velikostPlosce-2) + "-" + y);
		}
		else {
			sosedi.add((x-1) + "-" + y);
			sosedi.add((x+1) + "-" + y);
		}
		if (y == 0) {
			sosedi.add(x + "-1");
		}
		else if (y == velikostPlosce-1) {
			sosedi.add(x + "-" + (velikostPlosce-2));
		}
		else {
			sosedi.add(x + "-" + (y-1));
			sosedi.add(x + "-" + (y+1));
		}
		return sosedi;
	}
//		if (x_.equals("0")) {
//			sosedi.add("1-" + y_);
//		}
//		else if (x_.equals(Integer.toString(velikostPlosce-1))) {
//			sosedi.add(Integer.toString(velikostPlosce-2) + "-" + y_);
//		}
//		else {
//			sosedi.add(Integer.toString(x-1) + "-" + y_);
//			sosedi.add(Integer.toString(x+1) + "-" + y_);
//		}
//		if (y_.equals("0")) {
//			sosedi.add(x_ + "-1");
//		}
//		else if (y_.equals(Integer.toString(velikostPlosce-1))) {
//			sosedi.add(x_ + "-" + Integer.toString(velikostPlosce-2));
//		}
//		else {
//			int y = Integer.parseInt(xy[1]);
//			sosedi.add(x_ + "-" + Integer.toString(y-1));
//			sosedi.add(x_ + "-" + Integer.toString(y+1));
//		}
//		return sosedi;
//	}
	
//	public 
//	@Override  //  //??
	public static String toString (int x, int y) {
//		String imeTocke = Integer.toString(x) + "-" + Integer.toString(y); //xy
//		return imeTocke;
		return Integer.toString(x) + "-" + Integer.toString(y); //xy
	}
}