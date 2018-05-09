package test;

import javafx.util.Pair;
import junit.framework.TestCase;
import logika.Igra;
import logika.Stanje;

public class TestLogikaIgre extends TestCase {
	
	public void testStanje() {
		Igra igra = new Igra();
		
		// Preverimo, da imata oba igralca na zacetku enako stevilo polj in sicer vsak 2 polji.
		Pair<Integer, Integer> stPolj = igra.prestejPolja();
		assertEquals(stPolj.getKey(), stPolj.getValue());
		assertTrue(2 == stPolj.getKey());
		
		// Ko prvi igralec(crni) naredi potezo, mora biti tedaj na potezi beli, imeti mora na voljo 3 poteze, ker so vse 4 poteze crnega simetricne.
		igra.izvediPotezo(igra.seznamDovoljenih().get(0));
		assertEquals(Stanje.NA_POTEZI_BELI, igra.stanje());
		assertTrue(3 == igra.seznamDovoljenih().size());
		
		// Zanka v kateri se vedno izvede prva mozna poteza na seznamu dovoljenih za aktivnega igralca.
		while (igra.stanje() == Stanje.NA_POTEZI_BELI || igra.stanje() == Stanje.NA_POTEZI_CRNI) {
			igra.izvediPotezo(igra.seznamDovoljenih().get(0));

		}
		
		// Ce je stanje igre v enem izmed koncnih stanje, aktivni igralec ne sme imeti nobene mozne poteze.
		if (igra.stanje() == Stanje.NEODLOCENO || igra.stanje() == Stanje.ZMAGA_BELI || igra.stanje() == Stanje.ZMAGA_CRNI) {
			igra.izpisDovoljenih();
			igra.izpis();
			assertTrue(igra.seznamDovoljenih().isEmpty());
		}
		
		
	}
}
