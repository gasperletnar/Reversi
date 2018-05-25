package test;

import javafx.util.Pair;
import junit.framework.TestCase;
import logika.Igra;
import logika.Stanje;

/**
 * @author Gasper
 * Testni razred za ugotavljanje pravilnost kode.
 */
public class TestLogikaIgre extends TestCase {
	
	public void testStanje() {
		Igra igra = new Igra();
		
		// Preverimo, da imata oba igralca na zacetku enako stevilo polj in sicer vsak 2 polji.
		Pair<Integer, Integer> stPolj = igra.prestejPolja();
		assertEquals(stPolj.getKey(), stPolj.getValue());
		assertTrue(2 == stPolj.getKey());
		
		// Ko prvi igralec(crni) naredi potezo, mora biti tedaj na potezi beli, imeti mora na voljo 3 poteze, saj so vse 4 poteze crnega simetricne.
		igra.izvediPotezo(igra.seznamDovoljenih().get(0));
		assertEquals(Stanje.NA_POTEZI_BELI, igra.stanje());
		assertTrue(3 == igra.seznamDovoljenih().size());
		igra.izvediPotezo(igra.seznamDovoljenih().get(0));
		igra.izpisDovoljenih();
		igra.izpis();
		
		// Zanka v kateri se vedno izvede prva mozna poteza na seznamu dovoljenih za aktivnega igralca.
		while (igra.stanje() == Stanje.NA_POTEZI_BELI || igra.stanje() == Stanje.NA_POTEZI_CRNI) {
			igra.izvediPotezo(igra.seznamDovoljenih().get(0));

		}
		// Ko se zgornja zanka ustavi, smo v enem izmed koncnih stanj. Na potezi ne sme biti nihce izmed igralcev.
		if (igra.stanje() == Stanje.NEODLOCENO || igra.stanje() == Stanje.ZMAGA_BELI || igra.stanje() == Stanje.ZMAGA_CRNI) {
			igra.izpisDovoljenih();
			igra.izpis();
			assertTrue(igra.naPotezi() == null);
		}
		
		
	}
}
