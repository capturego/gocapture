package inteligenca;

import logika.Igra;

import splosno.Poteza;
import splosno.KdoIgra;


public class Inteligenca extends KdoIgra {
	
	public Inteligenca () {
		super("skupina_123");
	}
	public Poteza izberiPotezo (Igra igra) {
		return new Alphabeta(2).izberiPotezo(igra);
	}
}