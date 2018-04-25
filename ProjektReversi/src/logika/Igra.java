package logika;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Igra {
	private Polje[][] plosca;
	private Igralec naPotezi;
	public static final int N = 8;
	private static final int[][] tabelaSmeri = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

	/**
	 * Ustvari igralno plosco, vsa polja prazna, razen sredinska 4.
	 */

	public Igra() {
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		plosca[N/2-1][N/2-1] = Polje.BELO;
		plosca[N/2][N/2] = Polje.BELO;
		plosca[N/2-1][N/2] = Polje.CRNO;
		plosca[N/2][N/2-1] = Polje.CRNO;

		naPotezi = Igralec.BELI;
	}

	/**
	 * Izpise tabelo polj in cigavo je trenutno polje - prazno, od crnega, od belega.
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
			aktivnaPolja = Polje.BELO;
		} else {
			aktivnaPolja = Polje.CRNO;
		}
		return aktivnaPolja;
	}

	/**
	 * @return Vrne polje z barvo nasprotno od aktivnega igralca.
	 */
	public Polje nasprotniPolja() {
		Polje nasprotnaPolja = Polje.PRAZNO;
		if (naPotezi == Igralec.BELI) {
			nasprotnaPolja = Polje.CRNO;
		} else {
			nasprotnaPolja = Polje.BELO;
		}
		return nasprotnaPolja;
	}

	/**
	 * @return Seznam moznih potez aktivnega igralca.
	 */

	public List<Poteza> seznamDovoljenih() {
		LinkedList<Poteza>  dovoljene = new LinkedList<Poteza>();	
		Polje aktivno = aktivniPolja();
		Polje nasprotno = nasprotniPolja();	
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					Poteza moznaPoteza = new Poteza(i, j);
					for (int[] smer : tabelaSmeri) {
						int k = 0; // Stevec k, ki bo belezil koliko polj nasprotnega igralca je v dani smeri od moznaPoteza naprej.
						int x = smer[0];
						int y = smer[1];
						// Robni pogoji + polje mora biti nasprotno od trenutnega igralca.
						while ((0 <= i+x) && (0 <= j+y) && (i+x < N) && (j+y < N) && (plosca[i+x][j+y] == nasprotno)) {
							k++;
							x += smer[0];
							y += smer[1];
						}
						// Vsaj eno polje nasprotnega igralca + naslednje polje takoj za linijo nasprotnikovih mora biti od igralca na potezi.
						if (k > 0 && plosca[i+x][j+y] == aktivno) {
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
	
	// Še ni dokonèano.
	
	public boolean izvediPotezo(Poteza p) {
		Polje aktivno = aktivniPolja();
		Polje nasprotno = nasprotniPolja();
		int i = p.vrstica;
		int j = p.stolpec;
		if (plosca[i][j] != Polje.PRAZNO) {
			return false;
		}
		int l = 0;
		for (int[] smer : tabelaSmeri) { 
			int k = 0;
			int x = smer[0];
			int y = smer[1];
			while ((0 <= i+x) && (0 <= j+y) && (i+x < N) && (j+y < N) && (plosca[i+x][j+y] == nasprotno)) {
				k++;
				l++;
				x += smer[0];
				y += smer[1];
			}
			if (k > 0 && plosca[i+x][j+y] == aktivnaPolja) {
				dovoljene.add(moznaPoteza);
			}
		}
	}
}

