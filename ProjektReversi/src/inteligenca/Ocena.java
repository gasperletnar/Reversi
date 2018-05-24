package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;

public class Ocena {
	private static final int ZMAGA = (1 << 20); // Vrednost zmage, ki mora biti vecja kot vse ostale vrednosti.
	private static final int ZGUBA = -ZMAGA; // Vrednost izgube, ki mora biti manjša kot vse ostale vrednosti.
	private static final int NEODLOCENO = 0; // Vrednost neodlocenega izida.
	
	private static final int POLJE = 1; // Vrednost posameznega polja.
	private static final int KOT = 50; // Vrednost polja v kotu.
	private static final int ROBNI_SOSED = -10; // Vrednost robnega polja, ki je sosed kota.
	private static final int DIAGONALNI_SOSED = -20; // Vrednost polja, ki je sosed kota in NI roben.
	private static final int ROB = 5; // Vrednost roba.
	public static final int POTEZA = 2; // Vrednost mozne poteze.
	
	private static final int N = Igra.N-1;
	private static final int[][] KOTI = {{0,0}, {0,N}, {N,0}, {N,N}}; // Koordinate vseh 4 kotov.
	private static final int[][] ROBNI_SOSEDI = {{0,1}, {0,N-1}, {1,0}, {1,N}, {N,1}, {N,N-1}, {N-1,0}, {N-1,N}}; // Koordinate robnih polj, ki so sosedi kota.
	private static final int[][] DIAGONALNI_SOSEDI = {{1,1}, {1,N-1}, {N-1,1}, {N-1,N-1}}; // Koordinate polj, ki so sosedna kotu in niso robna.
	
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
			
			// Robna polja. Simetrija - v tabeli robna so 4 simetricna polja.
			for (int i = 2; i < N-1; i++) {
				Polje[] robna = {plosca[i][0], plosca[i][N], plosca[0][i], plosca[N][i]};
				for (Polje polje : robna) {
					switch (polje) {
					case BELO: vrednostBeli += ROB; break;
					case CRNO: vrednostCrni += ROB; break;
					case PRAZNO: break;
					}
				}
			}
			
			// Ce je dani igralec na potezi, se mu za vsako mozno potezo pristeje vrednost POTEZA. Sicer so v seznamuDovoljenih
			// mozne poteze na sprotnega igrala, v tem primeru se mu za vsako mozno potezo odsteje vrednost POTEZA.
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
	
	
	
//	private static void pristej(Igra igra, int i, int j, int beli, int crni, int vrednost) {
//	Polje[][] plosca = igra.getPlosca();
//	switch (plosca[i][j]) {
//	case BELO: beli += vrednost; break;
//	case CRNO: crni += vrednost; break;
//	case PRAZNO: break;
//	}
//}
	
//	private static int[][] koti = {{0,0}, {,0}, {0,}, {,}};
//	private static int[][] sosediKotov = new int[12][];
//	static {
//		int i = 0;
//		for (int[] par : pari) {
//			for (int[] kot : koti) {
//				int a = (kot[0] == 0? kot[0] + par[0] : kot[0] - par[0]);
//				int b = (kot[1] == 0? kot[1] + par[1] : kot[1] - par[1]);
//				sosediKotov[i][0] = a;
//				sosediKotov[i][1] = b;
//				i++;
//			}
//		}
//	}
	
//	public static int oceniPozicijo(Igralec jaz, Igra igra){
//		switch(igra.stanje()){
//		case ZMAGA_BELI:
//			return (jaz == Igralec.BELI ? ZMAGA : ZGUBA);
//		case ZMAGA_CRNI:
//			return (jaz == Igralec.CRNI ? ZMAGA : ZGUBA);
//		case NEODLOCENO:
//			return NEODLOCENO;
//		case NA_POTEZI_BELI:
//		case NA_POTEZI_CRNI:
//			Polje[][] plosca = igra.getPlosca();
//			int vrednostB = 0;
//			int vrednostC = 0;
//			// Preverimo, kateri igralec ima v kotih figuro.
//			// To kodo bi se dalo spravit v spodnjo for zanko, a bi potrebovali
//			// vec if stavkov
//			for (int k = 0; k<Igra.N; k = k+(Igra.N - 1)){
//				for (int h = 0; h<Igra.N; h = h+(Igra.N - 1)){
//					switch(plosca[k][h]){
//					case CRNO: vrednostB+=VREDNOST_KOTA;
//					case BELO: vrednostC+=VREDNOST_KOTA;
//					default: break;
//					}
//				}
//			}
//			// Preverimo, ali so figure na mestih, ki so sosednji kotom.
//			// Podobno, zaradi lazjega zapisa in manj if stavkov, se ta koda piše
//			// izven spodnje glavne for zanke
//			for (int a = 0; a<=1; a++){
//				for (int b = 0; b<=1; b++){
//					// Koda se na tak nacin zelo skrajša, ni potrebno naredit
//					// treh for zank.
//					if (a != 0 && b != 0){
//						for (int k = a; k<Igra.N; k = k+(Igra.N - (1 + 2*a))){
//							for (int h = b; h<Igra.N; h = h+(Igra.N - (1 + 2*b))){
//								switch(plosca[k][h]){
//								case CRNO: vrednostB+=VREDNOST_SOSEDOV_KOTA;
//								case BELO: vrednostC+=VREDNOST_SOSEDOV_KOTA;
//								default: break;
//								}
//							}
//						}
//					}
//				}
//				
//			}
//			// Dolociti je treba vrednost po razlicnih kriterijih, sprehodimo 
//			// se po vseh poljih
//			for (int i = 0; i < Igra.N; i++) {
//				for (int j = 0; j < Igra.N; j++) {
//					// Dodelimo igralcu vrednost roba, ce ima polje na robu,
//					// ne smemo steti sosedov roba
//					// Sprva po horizontalni smeri
//					if (i == 0 || i == (Igra.N - 1)){
//						if (!(j < 2) && !(j > (Igra.N - 2))){
//							switch(plosca[i][j]){
//							case CRNO: vrednostB+=VREDNOST_ROBA;
//							case BELO: vrednostC+=VREDNOST_ROBA;
//							default: break;
//							}
//						}
//					}
//					else{
//						// Se vertikalna smer
//						if (j == 0 || j == (Igra.N - 1)){
//							if (!(i < 2) && !(i > (Igra.N - 2))){
//								switch(plosca[i][j]){
//								case CRNO: vrednostB+=VREDNOST_ROBA;
//								case BELO: vrednostC+=VREDNOST_ROBA;
//								default: break;
//								}
//							}
//						}
//						else{
//							// Vsi preostali bodo za zdaj imeli vrednost 1
//							switch(plosca[i][j]){
//							case CRNO: vrednostB+=VREDNOST_NEROBNEGA;
//							case BELO: vrednostC+=VREDNOST_NEROBNEGA;
//							default: break;
//							}
//						}
//					}
//				}
//			}
//			// Sedaj bomo dodelili se vrednost, glede na to, koliko ima igralec
//			// moznih potez
//			if (jaz == Igralec.BELI){
//				vrednostB+=igra.seznamDovoljenih().size() * VREDNOST_MOZNE_POTEZE;
//			}
//			else{
//				vrednostC+=igra.seznamDovoljenih().size() * VREDNOST_MOZNE_POTEZE;
//			}
//			return (jaz == Igralec.CRNI ? (vrednostC - vrednostB) : (vrednostB - vrednostC));
//		}
//		return 42;
//	}
}



