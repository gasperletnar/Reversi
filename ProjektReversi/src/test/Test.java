package test;

import logika.Igra;
import logika.Poteza;

public class Test {

	public static void main(String[] args) {
		Poteza proba = new Poteza(4, 2);
		Igra poskus = new Igra();
		poskus.izvediPotezo(proba);
		
		// V metodo izvediPoteza() dodal na koncu naPotezi = Igralec.CRNI; samo za testirati ce deluje.
		
		proba = new Poteza(5, 2);
		poskus.izvediPotezo(proba);
		poskus.seznamDovoljenih();
		poskus.izpis();
	}

}
