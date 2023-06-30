package logika;

public enum Tezavnost {
	SREDNJA,
	LAHKA,
	PRELAHKA;
	
	public static String [] seznam () {
		return new String [] {"Srednja", "Lahka", "Prelahka"};
	}
	
	public static int ostevilci (String s) {
		if (s == "Srednja") {return 3;}
		else if (s == "Lahka") {return 2;}
		else if (s == "Prelahka") {return 1;}
		else {return 0;}
	}
}
