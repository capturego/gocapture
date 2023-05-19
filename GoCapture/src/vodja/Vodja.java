package vodja;

import java.util.Random;
import java.util.Map;
import java.util.List;

import javax.swing.SwingWorker;
import java.util.concurrent.TimeUnit;

import gui.GlavnoOkno;
import logika.Graf;
import logika.Igra;
import logika.Igralec;
import logika.Koordinati;
import splosno.Poteza;

public class Vodja {	
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
		
	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void igramo () {
		okno.osveziGUI();
		switch (igra.stanje_()) {
		case ZMAGA_O: 
		case ZMAGA_X: 
		case NEODLOCENO: 
			return; // odhajamo iz metode igramo
		case V_TEKU: 
			Igralec igralec = igra.naPotezi();
			VrstaIgralca vrstaNaPotezi = vrstaIgralca.get(igralec);
			switch (vrstaNaPotezi) {
			case C: 
				clovekNaVrsti = true;
				break;
			case R:
				igrajRacunalnikovoPotezo ();
				break;
			}
		}
	}
	
	private static Random random = new Random ();

	public static void igrajRacunalnikovoPotezo() {
//		for (String l_ : igra.LIBS.keySet()) {
//			System.out.println(igra.LIBS.get(l_));
//		}
//		for (Graf g_ : igra.GRAFI.values()) {
//			System.out.println(g_);
//		}
		Igra zacetnaIgra = igra;
		SwingWorker<Koordinati, Void> worker = new SwingWorker<Koordinati, Void>() {
			@Override
			protected Koordinati doInBackground() {
//				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				List<Koordinati> moznePoteze = igra.poteze();
				int randomIndex = random.nextInt(moznePoteze.size());
				return moznePoteze.get(randomIndex);
			}
			@Override
			protected void done() {
				Koordinati poteza = null;
				try {
					poteza = get(); 
				}
//					System.out.println("DOBIL POTEZO");} 
				catch (Exception e) {}
//					System.out.println("NE DOBIL POTEZO");};
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
	}
				
//		List<Poteza> moznePoteze = igra.poteze();
//		try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
//		int randomIndex = random.nextInt(moznePoteze.size());
//		Poteza poteza = moznePoteze.get(randomIndex);
//		igra.odigraj(poteza);
//		igramo ();

	public static void igrajClovekovoPotezo(Koordinati poteza) {
		if (igra.odigraj(poteza)) {
//			for (String l_ : igra.LIBS.keySet()) {
//				System.out.println(igra.LIBS.get(l_));
//			}
//			for (Graf g_ : igra.GRAFI.values()) {
//				System.out.println(g_);
//			}
//			for (Graf g_ : igra.GRAFI.values()) {
//				for (String x : g_.tocke) {
//					System.out.print(x + ", ");
//				}
//			}
			clovekNaVrsti = false;
			igramo ();
		}
	}
}


