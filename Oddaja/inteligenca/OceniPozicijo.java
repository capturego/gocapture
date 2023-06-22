package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Graf;
import logika.Liberty;


public class OceniPozicijo {
	
	// Metoda oceniPozicijo za igro TicTacToe
	
	public static int oceniPozicijo(Igra igra, Igralec jaz) {
		int ocena = 0;
		for (Liberty lib : igra.LIBS.values()) {
			for (String imeGrafa : lib.grafi) {
				Graf graf = igra.GRAFI.get(imeGrafa);
				ocena = (graf.lastnik == jaz) ? ocena+1 : ocena-1;
			}
		}
		return ocena;
	}
}


