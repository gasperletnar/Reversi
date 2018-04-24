package logika;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
		plosca[N/2-1][N/2-1] = Polje.B_POLJE;
		plosca[N/2][N/2] = Polje.B_POLJE;
		plosca[N/2-1][N/2] = Polje.C_POLJE;
		plosca[N/2][N/2-1] = Polje.C_POLJE;
		
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
	 * Izracuna vse mozne poteze, ki jih ima igralec, ki je trenutno na potezi na voljo.
	 * 
	 * @return 
	 */
	
	public Map<Poteza, Polje> slovarDovoljenih() {
		HashMap<Poteza, Polje> potezeInPolja = new HashMap<Poteza, Polje>();
		
		// Ideja: Ce na potezi beli, se pogleda na katero prazno polje lahko postavi zeton tako, da se v vseh 8 smeri pogleda
		//        ce je tam postavljen kak crni zeton. Ce je, se nadaljuje v isti smeri, dokler so v liniji crni zetoni,
		//        koncati pa z belim zetonom. Poteza se shrani v kljuc slovarja, v vrednosti pa za vsako smer koordinate
		//        belega zetona, ki je za linijo crnih zetonov, ki se zacne od poteze naprej. Tako se bo nato ob primeru izvedbe
		//        poteze vsem poljem med potezo in koordinatami belih polj zamenjalo enum type iz C_POLJE na B_POLJE.
		
		// Povpraviti: Verjetno boljše, èe imamo v vrednostih slovarja seznam koordinat belih polj od dane poteze preko crnih, kakor
		//             seznam polj.
		
		// Za enkrat privzela, da na potezi beli.
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (plosca[i][j] == Polje.PRAZNO) {
					Poteza moznaPoteza = new Poteza(i, j);
					for (int[] smer : tabelaSmeri) {
						int k = 0; // Stevec k, ki bo belezil koliko polj nasprotnega igralca je v dani smeri od moznaPoteza naprej.
						int x = smer[0];
						int y = smer[1];
						// Robni pogoji + polje mora biti nasprotno od trenutnega igralca.
						while ((0 <= i+x) && (0 <= j+y) && (i+x < N) && (j+y < N) && (plosca[i+x][j+y] == Polje.C_POLJE)) {
							k++;
							x += smer[0];
							y += smer[1];
						}
						// Vsaj eno polje nasprotnega igralca + naslednje polje takoj za linijo nasprotnikovih mora biti od igralca na potezi.
						if (k > 0 && plosca[i+x][j+y] == Polje.B_POLJE) {
							potezeInPolja.put(moznaPoteza, plosca[i+x][j+y]);
						}
					}
				}
			}
		}
		
		for (Map.Entry<Poteza, Polje> dvojec : potezeInPolja.entrySet()) {
			System.out.println((dvojec.getKey().getX() + 1) + "," + (dvojec.getKey().getY() + 1) + ": " + dvojec.getValue());
		}
		return potezeInPolja;
		
	}
}
