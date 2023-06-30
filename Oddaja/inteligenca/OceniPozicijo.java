package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Graf;
import logika.Liberty;
import logika.Tocka;


public class OceniPozicijo {
	
	// Metoda oceniPozicijo za igro Capture Go
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (String imeGrafa : igra.grafi1libs) {
			Graf graf = igra.GRAFI.get(imeGrafa);
			if (graf.lastnik == jaz) {ocena -= Integer.MAX_VALUE/20;}
		}
		for (Graf graf :igra.GRAFI.values()) {
			int oddaljenost = 0;
			for (Tocka lib_ : graf.libs) {
				Liberty lib = igra.LIBS.get(lib_);
				oddaljenost += Math.abs(lib.x - 4) + Math.abs(lib.y - 4);
			int moc = 10*graf.moc();
			ocena = (graf.lastnik == jaz) ? ocena+moc-oddaljenost : ocena-moc+oddaljenost;
			}
		}
		return ocena;
	}
}


