package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;

public class Ocena {
	public static final int ZMAGA = (1 << 20); // Vrednost zmage, ki mora biti vecja kot vse ostale vrednosti.
	public static final int ZGUBA = -ZMAGA; // Vrednost izgube, ki mora biti manjša kot vse ostale vrednosti.
	public static final int NEODLOCENO = 0; // Vrednost neodlocenega izida.
	
	private static final int POLJE = 1; // Vrednost posameznega polja.
	private static final int KOT = 50; // Vrednost polja v kotu.
	private static final int ROBNI_SOSED = -10; // Vrednost robnega polja, ki je sosed kota.
	private static final int DIAGONALNI_SOSED = -20; // Vrednost polja, ki je sosed kota in NI roben.
	private static final int ROB = 5; // Vrednost roba.
	private static final int POTEZA = 2; // Vrednost mozne poteze.
	
	private static final int N = Igra.N-1; // Polja imajo indexe od 0 do Igra.N-1 -> N definiran za lazjo preglednost.
	private static final int[][] KOTI = {{0,0}, {0,N}, {N,0}, {N,N}}; // Koordinate vseh 4 kotov.
	private static final int[][] ROBNI_SOSEDI = {{0,1}, {0,N-1}, {1,0}, {1,N}, {N,1}, {N,N-1}, {N-1,0}, {N-1,N}}; // Koordinate robnih polj, ki so sosedi kota.
	private static final int[][] DIAGONALNI_SOSEDI = {{1,1}, {1,N-1}, {N-1,1}, {N-1,N-1}}; // Koordinate polj, ki so sosedna kotu in niso robna.
	private static final int[][] ROBOVI = new int[4*(Igra.N - 4)][2]; // Koordinate robnih polj, ki niso koti/sosedi kota.
	static {
		int j = 0; // Zaradi simetrije lahko dodajamo po 4 robna polja na enkrat, za vsako stranico enega.
		for (int i = 2; i < N-1; i++) {
			ROBOVI[j][0] = i; ROBOVI[j][1] = 0;
			j++;
			ROBOVI[j][0] = i; ROBOVI[j][1] = N;
			j++;
			ROBOVI[j][0] = 0; ROBOVI[j][1] = i;
			j++;
			ROBOVI[j][0] = N; ROBOVI[j][1] = i;
			j++;
		}
	}
	
	/**
	 * Oceni koliko je vredna trenutna pozicije igre za danega igralca. Vrednost ocene je podana glede na polja na plosci,
	 * kjer se vrednosti ocene pristevajo/odstevajo glede na to, cigavo je polje in tip polja. Koti so vredni najvec, nato robovi
	 * ter nazadnje ostala polja. Izjema so sosedi kotov, ki se stejejo negativno, saj so to slaba polja za igralca. Obratno, ce
	 * ima nasprotni igralec neko polje, se vrednosti odstevajo(pristevajo v primeru sosedov kota).
	 * 
	 * @param jaz Igralec, katerega oceno zelimo dobiti.
	 * @param igra Trenutno stanje igre. (ne spreminjaj tega objekta).
	 * @return Ocena vrednosti pozicije za danega igralca(zagotovo pravilna, ce je igre konec).
	 */
	public static int oceniPozicijo(Igralec jaz, Igra igra){
		switch(igra.stanje()){
		case ZMAGA_BELI:
			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
		case ZMAGA_CRNI:
			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
		case NEODLOCENO:
			return NEODLOCENO;
		case NA_POTEZI_BELI:
		case NA_POTEZI_CRNI:
			Polje[][] plosca = igra.getPlosca();
			int vrednostBeli = 0;
			int vrednostCrni = 0;
			
			// Pregledamo notranje polja plosce in pristevamo vrednosti tistemu igralcu, cigar polje je.
			for (int i = 1; i < N; i++) {
				for (int j = 1; j < N; j++) {
					switch (plosca[i][j]) {
					case BELO: vrednostBeli += POLJE; break;
					case CRNO: vrednostCrni += POLJE; break;
					case PRAZNO: break;
					}
				}
			}
			
			// Kot prej, tukaj za kote.
			for (int[] par : KOTI) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += KOT; break;
				case CRNO: vrednostCrni += KOT; break;
				case PRAZNO: break;
				}
			}
			
			// Robni sosedi - vsak kot ima 3 sosede, 2 robna enega diagonalnega.
			for (int[] par : ROBNI_SOSEDI) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += ROBNI_SOSED; break;
				case CRNO: vrednostCrni += ROBNI_SOSED; break;
				case PRAZNO: break;
				}
			}
			
			// Tukaj potrebno v primeru da pristejemo vrednost enemu od igralcev, da leto zmanjsamo za vrednost POLJE,
			// saj smo ga steli ze ko smo sli cez notranjost plosce.
			for (int[] par : DIAGONALNI_SOSEDI) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += DIAGONALNI_SOSED - POLJE; break;
				case CRNO: vrednostCrni += DIAGONALNI_SOSED - POLJE; break;
				case PRAZNO: break;
				}
			}
			
			// Robna polja.
			for (int[] par : ROBOVI) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += ROB; break;
				case CRNO: vrednostCrni += ROB; break;
				case PRAZNO: break;
				}
			}
			
			// Ce je dani igralec na potezi, se mu za vsako mozno potezo pristeje vrednost POTEZA. Sicer so v seznamuDovoljenih
			// mozne poteze na nasprotnega igrala, v tem primeru se mu(danemu igralcu) za vsako mozno potezo odsteje vrednost POTEZA.
			int predznak = (jaz == igra.naPotezi()? 1 : -1);
			switch (jaz) {
			case BELI: vrednostBeli += predznak * igra.seznamDovoljenih().size() * POTEZA; break;
			case CRNI: vrednostCrni += predznak * igra.seznamDovoljenih().size() * POTEZA; break;
			}
			return (jaz == Igralec.CRNI ? (vrednostCrni - vrednostBeli) : (vrednostBeli - vrednostCrni));
		}
		assert false;
		return 9;
	}
}
