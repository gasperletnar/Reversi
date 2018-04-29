package test;

import logika.Igra;
import logika.Poteza;

public class Test {

	public static void main(String[] args) {
		Poteza proba = new Poteza(3, 2);
		Igra poskus = new Igra();
		poskus.izvediPotezo(proba);
		
		proba = new Poteza(2, 2);
		poskus.izvediPotezo(proba);
		proba = new Poteza(2, 3);
		poskus.izvediPotezo(proba);
		poskus.seznamDovoljenih();
		poskus.izpis();
		poskus.koncniIzracun();
		System.out.println(poskus.prestejPolja());
	}
}
