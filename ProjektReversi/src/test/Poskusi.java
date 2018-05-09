package test;

import logika.Igra;
import logika.Poteza;

public class Poskusi {

	public static void main(String[] args) {
		Igra poskus2 = new Igra();
		Poteza proba2 = new Poteza(2, 3);
		System.out.println(poskus2.izvediPotezo(proba2));
		poskus2.izpis();
		System.out.println(poskus2.stanje());
	}
}
