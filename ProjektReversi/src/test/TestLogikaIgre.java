package test;

import junit.framework.TestCase;
import logika.Igra;
import logika.Stanje;

public class TestLogikaIgre extends TestCase {
	
	public void testStanje() {
		Igra igra = new Igra();
		// Ce je stanje igre v enem izmed koncnih stanje, aktivni igralec nima nobene mozne poteze.
		if (igra.stanje() == Stanje.NEODLOCENO || igra.stanje() == Stanje.ZMAGA_BELI || igra.stanje() == Stanje.ZMAGA_CRNI) {
			assertTrue(igra.seznamDovoljenih().isEmpty());
		}
	}
}
