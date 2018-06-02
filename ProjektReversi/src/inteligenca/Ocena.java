package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;

/**
 * @author Gasper
 * Ocena stanja igre za igralca odvisna od tockovanje razlicnih tipov polj in poteze.
 */
public class Ocena {
	public static final int ZMAGA = (1 << 20); // Vrednost zmage, ki mora biti vecja kot vse ostale vrednosti.
	public static final int ZGUBA = -ZMAGA; // Vrednost izgube, ki mora biti manjsa kot vse ostale vrednosti.
	public static final int NEODLOCENO = 0; // Vrednost neodlocenega izida.
	
	// Privzete vrednosti srednje tezavnosti.
	private static int polje = 1; // Vrednost posameznega polja.
	private static int kot = 3; // Vrednost polja v kotu.
	private static int robniSosed = -1; // Vrednost robnega polja, ki je sosed kota.
	private static int diagonalniSosed = -2; // Vrednost polja, ki je sosed kota in NI roben.
	private static int rob = 2; // Vrednost roba.
	private static int poteza = 1; // Vrednost mozne poteze.
	
	private static final int N = Igra.N-1; // Polja imajo indexe od 0 do Igra.N-1 -> N definiran za boljso preglednost.
	private static final int[][] koti = {{0,0}, {0,N}, {N,0}, {N,N}}; // Koordinate vseh 4 kotov.
	private static final int[][] robniSosedi = {{0,1}, {0,N-1}, {1,0}, {1,N}, {N,1}, {N,N-1}, {N-1,0}, {N-1,N}}; // Koordinate robnih polj, ki so sosedi kota.
	private static final int[][] diagonalniSosedi = {{1,1}, {1,N-1}, {N-1,1}, {N-1,N-1}}; // Koordinate polj, ki so sosedna kotu in niso robna.
	private static final int[][] robovi = new int[4*(Igra.N - 4)][2]; // Koordinate robnih polj, ki niso koti/sosedi kota.
	static {
		int j = 0; // Zaradi simetrije lahko dodajamo po 4 robna polja na enkrat, za vsako stranico enega.
		for (int i = 2; i < N-1; i++) {
			robovi[j][0] = i; robovi[j][1] = 0;
			j++;
			robovi[j][0] = i; robovi[j][1] = N;
			j++;
			robovi[j][0] = 0; robovi[j][1] = i;
			j++;
			robovi[j][0] = N; robovi[j][1] = i;
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
			
			// Pregledamo notranja polja plosce in pristevamo vrednosti tistemu igralcu, cigar polje je.
			for (int i = 1; i < N; i++) {
				for (int j = 1; j < N; j++) {
					switch (plosca[i][j]) {
					case BELO: vrednostBeli += polje; break;
					case CRNO: vrednostCrni += polje; break;
					case PRAZNO: break;
					}
				}
			}
			
			// Kot prej, tukaj za kote.
			for (int[] par : koti) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += kot; break;
				case CRNO: vrednostCrni += kot; break;
				case PRAZNO: break;
				}
			}
			
			// Robni sosedi - vsak kot ima 3 sosede, 2 robna enega diagonalnega.
			for (int[] par : robniSosedi) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += robniSosed; break;
				case CRNO: vrednostCrni += robniSosed; break;
				case PRAZNO: break;
				}
			}
			
			// Tukaj potrebno v primeru da pristejemo vrednost enemu od igralcev, da leto zmanjsamo za vrednost POLJE,
			// saj smo ga steli ze ko smo sli cez notranjost plosce.
			for (int[] par : diagonalniSosedi) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += diagonalniSosed - polje; break;
				case CRNO: vrednostCrni += diagonalniSosed - polje; break;
				case PRAZNO: break;
				}
			}
			
			// Robna polja.
			for (int[] par : robovi) {
				switch (plosca[par[0]][par[1]]) {
				case BELO: vrednostBeli += rob; break;
				case CRNO: vrednostCrni += rob; break;
				case PRAZNO: break;
				}
			}
			
			// Ce je dani igralec na potezi, se mu za vsako mozno potezo pristeje vrednost POTEZA. Sicer so v seznamuDovoljenih
			// mozne poteze nasprotnega igralca, v tem primeru se mu(danemu igralcu) za vsako mozno potezo odsteje vrednost POTEZA.
			int predznak = (jaz == igra.naPotezi()? 1 : -1);
			switch (jaz) {
			case BELI: vrednostBeli += predznak * igra.seznamDovoljenih().size() * poteza; break;
			case CRNI: vrednostCrni += predznak * igra.seznamDovoljenih().size() * poteza; break;
			}
			return (jaz == Igralec.CRNI ? (vrednostCrni - vrednostBeli) : (vrednostBeli - vrednostCrni));
		}
		assert false;
		return 9;
	}
	
	/**
	 * Nastavi vrednosti tockovanja vsem razlicnim tipom polj in potezi.
	 * @param vrPolje - navadno polje
	 * @param vrKot - kot
	 * @param robniS - robni sosed kota
	 * @param diagonalniS - diagonalni sosed kota
	 * @param vrRob - rob, ki ni sosed kota
	 * @param vrPoteza - poteza
	 */
	public static void nastaviVrednosti (int vrPolje, int vrKot, int robniS, int diagonalniS, int vrRob, int vrPoteza) {
		polje = vrPolje;
		kot = vrKot;
		robniSosed = robniS;
		diagonalniSosed = diagonalniS;
		rob = vrRob;
		poteza = vrPoteza;
	}
}
