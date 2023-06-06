package inteligenca;

import logika.Igra;

import splosno.Poteza;
import splosno.KdoIgra;

//public abstract class Inteligenca extends KdoIgra {
//	
//	Inteligenca () {
//		super("skupina_123");
//		
//	}
//	public abstract Poteza izberiPotezo (Igra igra);
//
//}
//
public class Inteligenca extends KdoIgra {
	
	public Inteligenca () {
		super("skupina_123");
	}
	public Poteza izberiPotezo (Igra igra) {
		return new Alphabeta(3).izberiPotezo(igra);
	}
}