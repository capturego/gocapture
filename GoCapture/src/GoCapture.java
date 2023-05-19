import gui.GlavnoOkno;
import logika.Tocka;
import vodja.Vodja;

public class GoCapture {
	
	Tocka T;
	public static void main(String[] args) {
		GlavnoOkno glavno_okno = new GlavnoOkno();
		glavno_okno.pack();
		glavno_okno.setVisible(true);
		Vodja.okno = glavno_okno;
//		int [] c = {1,2};
//		Tocka T = new Tocka(c);
//		System.out.println(T);
	}

}
