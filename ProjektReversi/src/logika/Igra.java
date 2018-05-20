package logika;

import java.util.Collections;
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
		plosca = new Polje[N][N]; // Nova matrika NxN, elementi so tipa Polje, ni se doloceno katero izmed vrednosti konstant bodo zavzeli.
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = Polje.PRAZNO;
			}
		}
		plosca[N/2-1][N/2-1] = Polje.BELO;
		plosca[N/2][N/2] = Polje.BELO;
		plosca[N/2-1][N/2] = Polje.CRNO;
		plosca[N/2][N/2-1] = Polje.CRNO;
		naPotezi = Igralec.CRNI;
	}
	
	/**
	 * Prekopiramo podatke iz igre podane v argumentu.
	 * @param igra
	 */
	public Igra(Igra igra) {
		plosca = new Polje[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				plosca[i][j] = igra.plosca[i][j];
			}
		}
		naPotezi = igra.naPotezi;
	}
	
	/**
	 * @return Plosca igre.
	 */
	public Polje[][] getPlosca(){
		return this.plosca;
	}

	/**
	 * Izpise tabelo polj.
	 */
	public void izpis() {
		for (int i = 0; i < N; i++) {
			System.out.print("|");
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.CRNO) {
					System.out.print("B");
				} else if (plosca[i][j] == Polje.BELO) {
					System.out.print("C");
			    } else {
			    	System.out.print(".");
			    }
				if (j<N-1) System.out.print("  ");
			}
			System.out.println("|");
		}
	}
	
	/**
	 * @return Vrne stevilo polj za vsakega igralca - (stevilo crnih, stevilo belih).
	 */
	public Pair<Integer, Integer> prestejPolja() {
		int crni = 0;
		int beli = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.CRNO) {
					crni++;
				}
				if (plosca[i][j] == Polje.BELO) {
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
	 * @return Stanje igre.
	 */
	public Stanje stanje() {
		if (naPotezi == Igralec.BELI) {
			Stanje stanjeIgre = Stanje.NA_POTEZI_BELI;
			return stanjeIgre;
		} else if (naPotezi == Igralec.CRNI){
			Stanje stanjeIgre = Stanje.NA_POTEZI_CRNI;
			return stanjeIgre;
		} else {
			return koncniIzracun();
		}
	}

	/**
	 * @return Seznam moznih potez aktivnega igralca.
	 */
	public List<Poteza> seznamDovoljenih() {
		LinkedList<Poteza>  dovoljene = new LinkedList<Poteza>();
		
		// V primeru, da ni aktivnega igralca funkcija vrne prazen seznam.
		if (naPotezi == null) {
			return Collections.emptyList();
		}
		
		Polje aktivno = naPotezi.dobiPolje(); // Doloci polje aktivnega igralca.
		Polje nasprotno = naPotezi.nasprotnik().dobiPolje();
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
		return dovoljene;
	}
	
	/**
	 * Izpise dovoljene poteze aktivnega igralca.
	 */
	public void izpisDovoljenih() {
		if (seznamDovoljenih().isEmpty()) {
			System.out.println("Nobena poteza ni vec mozna.");
		} else {
			List<Poteza> s = seznamDovoljenih();
			for (Poteza mozna : s) {
				System.out.println((mozna.vrstica + 1) + ", " + (mozna.stolpec + 1));
			}
		}
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
		
		Polje aktivno = naPotezi.dobiPolje(); // Doloci polje aktivnega igralca.
		Polje nasprotno = naPotezi.nasprotnik().dobiPolje();
		int l = 0; // Belezi v koliko razlicnih smeri, iz polja kamor naj bi se izvedla poteza, spremenimo nasprotnikova polja v nasa.
		for (int[] smer : tabelaSmeri) { 
			int k = 0;
			int x = smer[0];
			int y = smer[1];
			while ((0 <= i+y) && (0 <= j+x) && (i+y < N) && (j+x < N) && (plosca[i+y][j+x] == nasprotno)) {
				k++;
				x += smer[0];
				y += smer[1];
			}
			// Pazi, vedno potrebni tudi robni pogoji se za polje aktivnega igralca, sicer lahko uidemo ven iz plosce.
			if (k > 0 && (0 <= i+y) && (0 <= j+x) && (i+y < N) && (j+x < N) &&  plosca[i+y][j+x] == aktivno) {
				l++;
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
		
		naPotezi = naPotezi.nasprotnik();
		if (seznamDovoljenih().isEmpty()) { // V primeru da nasprotni igralec nima moznosti narediti nobene poteze, je na potezi se enkrat aktivni igralec.
			naPotezi = naPotezi.nasprotnik();
			if (seznamDovoljenih().isEmpty()) { // Ce tudi on ne more narediti nobene poteze, je igre konec.
				naPotezi = null;
			}
		}
		
		return true;
	}
}