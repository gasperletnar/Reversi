package logika;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

public class Igra {
	private Polje[][] plosca;
	private Igralec naPotezi;
	public Stanje stanjeIgre;
	public static final int N = 8;
	private static final int[][] tabelaSmeri = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

	/**
	 * Ustvari igralno plosco; vsa polja prazna, razen sredinska 4.
	 */

	public Igra() {
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		plosca[N/2-1][N/2-1] = Polje.BELO__;
		plosca[N/2][N/2] = Polje.BELO__;
		plosca[N/2-1][N/2] = Polje.CRNO__;
		plosca[N/2][N/2-1] = Polje.CRNO__;
		
		stanjeIgre = Stanje.NA_POTEZI_CRNI;
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
	 * @return Vrne v paru stevilo obarvanih polj za vsakega igralca posebej, za�enj��i z
	 * belim
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
	 * Metoda spremeni stanje igre na ustreznega zmagovalca
	 */
	
	public void koncniIzracun() {
		Pair<Integer, Integer> koncenpar = prestejPolja();
		int crni = koncenpar.getKey();
		int beli = koncenpar.getValue();
		if (crni > beli) {
			stanjeIgre = Stanje.ZMAGA_CRNI;
		}
		else {
			if(beli > crni) {
				stanjeIgre = Stanje.ZMAGA_BELI;
			}
			else {
				stanjeIgre = Stanje.NEODLOCENO;
			}
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
						if (k > 0 && plosca[i+y][j+x] == aktivno) {
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
	 * Izvede potezo, �e je ta le mozna. Spremeni barvo tistih polj, ki jih doloca poteza.
	 * @param Poteza
	 * @return Vrne true, �e se je poteza izvedla, false sicer.
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
		if (naPotezi == Igralec.CRNI) {
			naPotezi = Igralec.BELI;
			stanjeIgre = Stanje.NA_POTEZI_BELI;
			// Reversi ima pravilo, da �e po opravljeni potezi nasprotni igralec nima nobenih
			// mo�nih potez, je na vrsti spet aktivni igralec
			if (seznamDovoljenih().isEmpty() == true) {
				naPotezi = Igralec.CRNI;
				stanjeIgre = Stanje.NA_POTEZI_CRNI;
				// Reversi ima spet pravilo, da �e noben igralec ne more izvesti poteze, se pre�teje polja
				// in se igra zaklju�i, zmaga tisti, ki ima ve� polj obarvanih svoje barve
				if (seznamDovoljenih().isEmpty() == true) {
					koncniIzracun();
				}
			}
		}
		// Podoben postopek kot zgoraj je potrebno tudi opraviti, �e je na potezi beli
		else {
			naPotezi = Igralec.CRNI;
			stanjeIgre = Stanje.NA_POTEZI_CRNI;
			if (seznamDovoljenih().isEmpty() == true) {
				naPotezi = Igralec.BELI;
				stanjeIgre = Stanje.NA_POTEZI_BELI;
				if (seznamDovoljenih().isEmpty() == true) {
					koncniIzracun();
				}
			}
		}
		return true;
	}
}

