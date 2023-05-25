package vodja;

//import java.util.Random;
//import java.util.concurrent.TimeUnit;
import java.util.Map;
//import java.util.List;

import javax.swing.SwingWorker;
//import java.util.concurrent.TimeUnit;

import inteligenca.Inteligenca;
import inteligenca.Minimax;
import inteligenca.Alphabeta;
import gui.GlavnoOkno;
//import logika.Graf;
import logika.Igra;
import logika.Igralec;
import splosno.Poteza;

public class Vodja {	
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	
	public static boolean clovekNaVrsti = false;
	
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca();
		
	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void igramo () {
		okno.osveziGUI();
		switch (igra.stanje()) {
		case ZMAGA_O: 
		case ZMAGA_X: 
			return;
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
	
//	private static Random random = new Random ();

//	public static Inteligenca racunalnikovaInteligenca = new Minimax(2);
//	public static Inteligenca racunalnikovaInteligenca = new Alphabeta(2);
//	public static Inteligenca racunalnikovaInteligenca = new Inteligenca(); // = new Alphabeta(3);

	public static void igrajRacunalnikovoPotezo() {
//		for (String l_ : igra.LIBS.keySet()) {
//			System.out.println(igra.LIBS.get(l_));
//		}
//		for (Graf g_ : igra.GRAFI.values()) {
//			System.out.println(g_);
//		}
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void>() {
			@Override
			protected Poteza doInBackground() {
				Poteza poteza = racunalnikovaInteligenca.izberiPotezo(igra);
//				try {TimeUnit.SECONDS.sleep(1);} catch (Exception e) {};
				return poteza;
//				List<Poteza> moznePoteze = igra.poteze();
//				int randomIndex = random.nextInt(moznePoteze.size());
//				return moznePoteze.get(randomIndex);
			}
			@Override
			protected void done() {
				Poteza poteza = null;
				try {
					poteza = get(); 
				}
				catch (Exception e) {}
				if (igra == zacetnaIgra) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
	}

	public static void igrajClovekovoPotezo(Poteza poteza) {
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


