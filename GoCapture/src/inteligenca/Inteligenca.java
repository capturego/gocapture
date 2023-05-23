package inteligenca;

import logika.Igra;

import splosno.Poteza;
import splosno.KdoIgra;

public abstract class Inteligenca extends KdoIgra {
	
	Inteligenca () {
		super("skupina_123");
		
	}
	public abstract Poteza izberiPotezo (Igra igra);

}
