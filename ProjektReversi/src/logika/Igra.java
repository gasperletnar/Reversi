package logika;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

public class Igra {
	private Polje[][] plosca;
	private Igralec naPotezi;
	public static final int N = 8;
	private static final int[][] tabelaSmeri = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

	/**
	 * Ustvari igralno plosco; vsa polja prazna, razen sredinska 4.
	 */

	public Igra() {
		plosca = new Polje[N][N]; // NxN objektov Polje, vrednost null.
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		plosca[N/2-1][N/2-1] = Polje.BELO__;
		plosca[N/2][N/2] = Polje.BELO__;
		plosca[N/2-1][N/2] = Polje.CRNO__;
		plosca[N/2][N/2-1] = Polje.CRNO__;
		
		naPotezi = Igralec.CRNI;
	}

	/**
	 * Izpise tabelo polj.
	 */

	public void izpis() {
		for (int i = 0; i < N; i++) {
			System.out.println(Arrays.deepToString(plosca[i]));
		}
	}

	/**
	 * @return Vrne polje z barvo enako kot je barva aktivnega igralca.
	 */
	
	public Polje aktivniPolja() {
		Polje aktivnaPolja = Polje.PRAZNO;
		if (naPotezi == Igralec.BELI) {
			aktivnaPolja = Polje.BELO__;
		} else {
			aktivnaPolja = Polje.CRNO__;
		}
		return aktivnaPolja;
	}

	/**
	 * @return Vrne polje z barvo nasprotno od aktivnega igralca.
	 */
	
	public Polje nasprotniPolja() {
		Polje nasprotnaPolja = Polje.PRAZNO;
		if (naPotezi == Igralec.BELI) {
			nasprotnaPolja = Polje.CRNO__;
		} else {
			nasprotnaPolja = Polje.BELO__;
		}
		return nasprotnaPolja;
	}
	
	/**
	 * @return Vrne stevilo polj za vsakega igralca - (stevilo crnih, stevilo belih).
	 * 
	 */
	
	public Pair<Integer, Integer> prestejPolja() {
		int crni = 0;
		int beli = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.CRNO__) {
					crni++;
				}
				if (plosca[i][j] == Polje.BELO__) {
					beli++;
			    }
			}
		}
		Pair<Integer, Integer> par = new Pair<>(crni, beli);
		return par;
	}
	
	/**
	 * @return Vrne stanje ob koncu igre.
	 */
	
	public Stanje koncniIzracun() {
		Pair<Integer, Integer> koncenpar = prestejPolja();
		int crni = koncenpar.getKey();
		int beli = koncenpar.getValue();
		
		if (crni > beli) {
			Stanje stanjeIgre = Stanje.ZMAGA_CRNI;
			return stanjeIgre;
		} else if (beli > crni) {
			Stanje stanjeIgre = Stanje.ZMAGA_BELI;
			return stanjeIgre;
		} else {
			Stanje stanjeIgre = Stanje.NEODLOCENO;
			return stanjeIgre;
			}
		}
	
	
	/**
	 * Fja pove trenutno stanje igre. Ce aktivni igralec nima moznosti izvesti nobene poteze, zamenja aktivnega igralca.
	 * @return Stanje igre.
	 */
	
	public Stanje stanje() {
		if (seznamDovoljenih().isEmpty() == true) { // V primeri da aktivni igralec nima moznosti narediti nobene poteze, je na potezi nasprotni igralec.
			naPotezi = naPotezi.nasprotnik();
			if (seznamDovoljenih().isEmpty() == true) { // Ce tudi on ne more narediti nobene poteze, je igre konec.
				return koncniIzracun();
			}
		}
		if (naPotezi == Igralec.BELI) {
			Stanje stanjeIgre = Stanje.NA_POTEZI_BELI;
			return stanjeIgre;
		} else { // Vedno velja: ce ni na potezi beli, je na potezi crni, ker ima enum Igralec dva stevca: BELI, CRNI.
			
			// Morda bi moral biti tu se kak test?
			
			Stanje stanjeIgre = Stanje.NA_POTEZI_CRNI;
			return stanjeIgre;
		}
	}

	/**
	 * @return Seznam moznih potez aktivnega igralca.
	 */

	public List<Poteza> seznamDovoljenih() {
		LinkedList<Poteza>  dovoljene = new LinkedList<Poteza>();	
		Polje aktivno = aktivniPolja(); // Doloci polje aktivnega igralca.
		Polje nasprotno = nasprotniPolja();	
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) { // Lega praznega polja na plosci nam da koordinate mozne poteze. Potrebno pa je se preveriti, da izpolnjuje vse pogoje.
					Poteza moznaPoteza = new Poteza(i, j);
					for (int[] smer : tabelaSmeri) {
						int k = 0; // Stevec k, ki bo belezil koliko polj nasprotnega igralca je v dani smeri od moznaPoteza naprej.
						int x = smer[0];
						int y = smer[1];
						// Robni pogoji + polje mora biti nasprotno od trenutnega igralca.
						// Bodi pozoren: x narasca v smeri narascanja j, y v smeri i!
						while ((0 <= i+y) && (0 <= j+x) && (i+y < N) && (j+x < N) && (plosca[i+y][j+x] == nasprotno)) {
							k++;
							x += smer[0];
							y += smer[1];
						}
						// Vsaj eno polje nasprotnega igralca + naslednje polje takoj za linijo nasprotnikovih mora biti od igralca na potezi.
						if (k > 0 && (0 <= i+y) && (0 <= j+x) && (i+y < N) && (j+x < N) &&  plosca[i+y][j+x] == aktivno) {
							dovoljene.add(moznaPoteza);
							break; // Ustavimo, ko prvic ugotovimo, da je poteza mozna.
							// Kasneje, ko se bo poteza izvedla, se bo se enkrat poracunalo in pa v VSE smeri.
						}
					}
				}
			}
		}

		for (Poteza mozna : dovoljene) {
			System.out.println((mozna.vrstica + 1) + ", " + (mozna.stolpec + 1));
		}

		return dovoljene;
	}
	
	/**
	 * Izvede potezo, ce je ta le mozna. Spremeni barvo tistih polj, ki jih doloca poteza.
	 * @param Poteza
	 * @return Vrne true, ce se je poteza izvedla, false sicer.
	 */
	
	public boolean izvediPotezo(Poteza p) {
		int i = p.vrstica;
		int j = p.stolpec;
		if (plosca[i][j] != Polje.PRAZNO) { // Poteza se mora izvesti na prazno polje.
			return false;
		}
		
		Polje aktivno = aktivniPolja(); // Doloci polje aktivnega igralca.
		Polje nasprotno = nasprotniPolja();
		int l = 0; // Belezi koliko vseh skupaj nasprotnikovih polj se zamenja v polja aktivnega igralca.
		for (int[] smer : tabelaSmeri) { 
			int k = 0;
			int x = smer[0];
			int y = smer[1];
			while ((0 <= i+y) && (0 <= j+x) && (i+y < N) && (j+x < N) && (plosca[i+y][j+x] == nasprotno)) {
				k++;
				x += smer[0];
				y += smer[1];
			}
			if (k > 0 && plosca[i+y][j+x] == aktivno) {
				l += k;
				while (k > 0) { // V obratnem vrstnem redu spreminjamo polja nasprotnikovega igralca v polja aktivnega igralca.
					k--;
					x -= smer[0];
					y -= smer[1];
					plosca[i+y][j+x] = aktivno;
				}
			}
		}
		
		if (l == 0) return false; // Ce se ni niti eno polje nasprotnikovega igralca spremenilo v polje aktivnega je poteza neveljavna.
		plosca[i][j] = aktivno; // Nastavimo polje kamor se izvede poteza na polje aktivnega igralca.
		naPotezi = naPotezi.nasprotnik(); // Zamenjamo igralca na potezi.
		return true;
	}
}

