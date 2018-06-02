package inteligenca;

import javax.swing.SwingWorker;

import gui.GlavnoOkno;
import logika.Igra;
import logika.Igralec;
import logika.Poteza;
import logika.Stanje;

/**
 * @author Gasper
 * Poteze so izbrane po principu Minimax.
 */
public class Minimax extends SwingWorker<Poteza, Object> {
	private GlavnoOkno master;
	
	/**
	 * Kako globoko algoritem pregleduje.
	 */
	private int globina;
	
	/**
	 * Barva, ki jo igra racunalnik.
	 */
	private Igralec jaz;
	
	public Minimax(GlavnoOkno master, int globina, Igralec jaz) {
		super();
		this.master = master;
		this.globina = globina;
		this.jaz = jaz;
	}

	@Override
	protected Poteza doInBackground() throws Exception {
		Igra igra = master.copyIgra();
		OcenjenaPoteza p = minimax(0, igra);
		assert (p.poteza != null);
		return p.poteza;
	}
	
	@Override
	public void done() {
		try {
			Poteza p = this.get();
			if (p != null) master.odigraj(p);
		} catch (Exception e) {
		}
	}
	
	/**
	 * Z uporabo metode minimax vrne najboljso potezo v igri.
	 * @param k - stevec globine
	 * @param igra
	 * @return najboljse ocenjena poteza; null ce je ni.
	 */
	private OcenjenaPoteza minimax(int k, Igra igra) {
		Igralec naPotezi = null;
		switch (igra.stanje()){
		// Pogledali bomo, ali je kdo na potezi, ali je igre konec
		case NA_POTEZI_CRNI: naPotezi = Igralec.CRNI; break;
		case NA_POTEZI_BELI: naPotezi = Igralec.BELI; break;
		case ZMAGA_CRNI:
			return new OcenjenaPoteza(null, (jaz == Igralec.CRNI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case ZMAGA_BELI:
			return new OcenjenaPoteza(null, (jaz == Igralec.BELI ? Ocena.ZMAGA : Ocena.ZGUBA));
		case NEODLOCENO:
			return new OcenjenaPoteza(null, Ocena.NEODLOCENO);
		default: break;
		}
		assert (naPotezi != null);
		if (k >= globina) {
			// Ce dosezemo maksimalno mozno globino, vrnemo le oceno pozicije
			return new OcenjenaPoteza(null, Ocena.oceniPozicijo(jaz, igra));
		}
		Poteza najboljsa = null;
		int ocenaNajboljse = 0;
		for (Poteza p: igra.seznamDovoljenih()) {
			// Naredimo kopijo, da lahko na njej naredimo korak, brez da bi to vplivalo na naso igro
			Igra kopijaIgre = new Igra(igra);
			Stanje trenutnoStanje = kopijaIgre.stanje();
			kopijaIgre.izvediPotezo(p);
			Stanje naslednjeStanje = kopijaIgre.stanje();
			int ocenaP = 0;
			// Preverimo, ali se je stanje spremenilo po izvedbi poteze, ce se je, globino povecamo
			if (trenutnoStanje != naslednjeStanje){
				ocenaP = minimax(k+1, kopijaIgre).vrednost;
			}
			else{
				ocenaP = minimax(k, kopijaIgre).vrednost;
			}
			// Preverimo, ali je ta vrednost res najboljsa
			if (najboljsa == null || (naPotezi == jaz && ocenaP > ocenaNajboljse)
			|| (naPotezi != jaz && ocenaP < ocenaNajboljse)){
				najboljsa = p;
				ocenaNajboljse = ocenaP;
			}
		}
		assert (najboljsa != null);
		return new OcenjenaPoteza(najboljsa, ocenaNajboljse);
	}
}
