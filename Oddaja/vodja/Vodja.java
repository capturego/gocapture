package vodja;

import java.io.Serializable;
import java.util.Map;
import javax.swing.SwingWorker;

import inteligenca.Inteligenca;
import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Tocka;
import splosno.Poteza;

public class Vodja implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	public static Map<Igralec,VrstaIgralca> vrstaIgralca;
	public Map<Igralec,VrstaIgralca> vrstaIgralca_;
	
	public static GlavnoOkno okno;
	
	public static Igra igra = null;
	public Igra igra_;
	
	public static boolean clovekNaVrsti = false;
	public boolean clovekNaVrsti_;
	
	public static int tezavnost;
	public static Inteligenca racunalnikovaInteligenca = new Inteligenca();
		
	public Vodja () {
		this.vrstaIgralca_ = vrstaIgralca;
		this.igra_ = igra;
		this.clovekNaVrsti_ = clovekNaVrsti;
	}
	
	public static void igramoNovoIgro () {
		igra = new Igra ();
		igramo ();
	}
	
	public static void igramoShranjenoIgro (Igra igra_) {
		igra = igra_;
		igramo();
	}
	
	public static void igramo () {
		okno.osveziGUI();
		switch (igra.stanje()) {
		case ZMAGA_CRNI:
		case ZMAGA_BELI: 
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

	public static void igrajRacunalnikovoPotezo() {
		Igra zacetnaIgra = igra;
		SwingWorker<Poteza, Void> worker = new SwingWorker<Poteza, Void>() {
			@Override
			protected Poteza doInBackground() {
				Poteza poteza;
				if (tezavnost > 0) {
					poteza = racunalnikovaInteligenca.izberiPotezo(igra, tezavnost);
				}
				else {
					poteza = racunalnikovaInteligenca.izberiPotezo(igra);
				}
				return poteza;
			}
			@Override
			protected void done() {
				Poteza poteza = null;
				try {
					poteza = get(); 
				}
				catch (Exception e) {}
				if (igra == zacetnaIgra) {
					igra.odigraj(new Tocka(poteza));
					igramo();
				}
			}
		};
		worker.execute();
	}

	public static void igrajClovekovoPotezo(Tocka poteza) {
		if (igra.odigraj(poteza)) {
			clovekNaVrsti = false;
			igramo ();
		}
	}
}


