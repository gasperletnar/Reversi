package logika;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.util.Pair;

/**
 * @author Gasper
 * Vsak objekt razreda Igra predstavlja eno, posamezno igro Reversija. V sebi hrani vse informacije o igri - igralno plosco
 * in z njo trenutno pozicijo; ter kdo je na potezi. Igralca plosca in igralec na potezi se spreminjata, ko se izvajajo poteze.
 */
public class Igra {
	
	/**
	 * Igralna plosca.
	 */
	private Polje[][] plosca;
	
	/**
	 * Igralec, ki je na potezi; null, ce je igre konec.
	 */
	private Igralec naPotezi;
	
	/**
	 * Velikost igralne plosce je NxN.
	 */
	public static final int N = 8;
	
	/**
	 * Tabela vseh 8ih smeri na plosci.
	 */
	private static final int[][] tabelaSmeri = {{1,1}, {1,0}, {1,-1}, {0,-1}, {-1,-1}, {-1,0}, {-1,1}, {0,1}};

	/**
	 * Ustvari igralno plosco. Vsa polja prazna, razen sredinska 4. Na potezi je crni.
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
		naPotezi = Igralec.CRNI;
	}
	
	/**
	 * Preslika podatke iz igre podane v argumentu.
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
	 * @return plosca igre
	 */
	public Polje[][] getPlosca(){
		return this.plosca;
	}
	
	/**
	 * @return igralec na potezi
	 */
	public Igralec naPotezi() {
		return naPotezi;
	}

	/**
	 * Izpis tabele polj.
	 */
	public void izpis() {
		for (int i = 0; i < N; i++) {
			System.out.print("|");
			for (int j = 0; j < N; j++) {
				switch (plosca[i][j]) {
				case CRNO: System.out.print("B"); break;
				case BELO: System.out.print("C"); break;
				case PRAZNO: System.out.print("."); break;
				}
				if (j<N-1) System.out.print("  ");
			}
			System.out.println("|");
		}
	}
	
	/**
	 * @return stevilo polj za vsakega igralca - (stevilo crnih, stevilo belih)
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
	 * @return stanje ob koncu igre
	 */
	public Stanje koncniIzracun() {
		Pair<Integer, Integer> koncenpar = prestejPolja();
		int crni = koncenpar.getKey();
		int beli = koncenpar.getValue();
		if (crni > beli) {
			return Stanje.ZMAGA_CRNI;
		} else if (beli > crni) {
			return Stanje.ZMAGA_BELI;
		} else {
			return Stanje.NEODLOCENO;
			}
		}
	
	/**
	 * @return stanje igre
	 */
	public Stanje stanje() {
		if (naPotezi == Igralec.BELI) {
			return Stanje.NA_POTEZI_BELI;
		} else if (naPotezi == Igralec.CRNI) {
			return Stanje.NA_POTEZI_CRNI;
		} else {
			return koncniIzracun();
		}
	}

	/**
	 * @return seznam moznih potez aktivnega igralca
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
							break; // Dovolj je, da poteza izpolnjuje pogoje za samo eno smer. Ustavimo in jo dodamo v mozne.
							// Kasneje, ko se bo poteza izvedla, se bo se enkrat poracunalo in pa v VSE smeri.
						}
					}
				}
			}
		}
		return dovoljene;
	}
	
	/**
	 * Izpis dovoljenih potez aktivnega igralca.
	 */
	public void izpisDovoljenih() {
		if (seznamDovoljenih().isEmpty()) {
			System.out.println("Nobena poteza ni vec mozna.");
		} else {
			System.out.print("Polja, kjer lahko igramo potezo(x, y): ");
			List<Poteza> s = seznamDovoljenih();
			for (Poteza mozna : s) {
				System.out.print("(" + (mozna.vrstica + 1) + ", " + (mozna.stolpec + 1) + ")");
			}
			System.out.println();
		}
	}
	
	/**
	 * Izvede potezo, ce je ta le mozna. Spremeni barvo tistih polj, ki jih doloca poteza.
	 * @param poteza
	 * @return true, ce se je poteza izvedla; false sicer
	 */
	public boolean izvediPotezo(Poteza p) {
		int i = p.vrstica;
		int j = p.stolpec;
		if (plosca[i][j] != Polje.PRAZNO) { // Poteza se mora izvesti na prazno polje.
			return false;
		}
		
		Polje aktivno = naPotezi.dobiPolje();
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