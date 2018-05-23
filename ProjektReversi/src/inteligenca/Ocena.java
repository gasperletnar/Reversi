package inteligenca;

import logika.Igra;
import logika.Igralec;
import logika.Polje;

public class Ocena {
	// Vrednost zmage, ki mora biti veliko vecja kot vse ostale vrednosti
	public static final int ZMAGA = (1 << 20);
	// Vrednost izgube, ki mora biti veliko manjša kot vse ostale vrednosti
	public static final int ZGUBA = -ZMAGA;
	// Vrednost neodlocenega izida
	public static final int NEODLOCENO = 0;
	// Dolocimo vrednost polj v kotu, ki je pri reversiju zelo pomemben
	public static final int VREDNOST_KOTA = 50;
	// Vrednost sosedov kota je negativna, saj kadar imamo figure na teh pozicijah,
	// lahko nasprotnik postavi figuro v kot
	public static final int VREDNOST_SOSEDOV_KOTA = -10;
	// Dolocimo vrednost polj na robu, ki sicer nisto pomembni kot kot, so pa bolj
	// kot nerobna polja
	public static final int VREDNOST_ROBA = 5;
	public static final int VREDNOST_NEROBNEGA = 1;
	public static final int VREDNOST_MOZNE_POTEZE = 2;
	
	private static int[][] pari = {{0,1}, {1,0}, {1,1}};
	private static int n = Igra.N-1;
	private static int[][] koti = {{0,0}, {n,0}, {0,n}, {n,n}};
	private static int[][] sosediKotov = new int[12][];
	static {
		int i = 0;
		for (int[] par : pari) {
			for (int[] kot : koti) {
				int a = (kot[0] == 0? kot[0] + par[0] : kot[0] - par[0]);
				int b = (kot[1] == 0? kot[1] + par[1] : kot[1] - par[1]);
				sosediKotov[i][0] = a;
				sosediKotov[i][1] = b;
				i++;
			}
		}
	}
	
	public boolean aliJeKot(int a, int b) {
		for (int[] kot : koti) {
			if (a == kot[0] && b == kot[1]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliJeSosedKota(int a, int b) {
		for (int[] sosedi : sosediKotov) {
			if (a == sosedi[0] && b == sosedi[1]) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aliJeRob(int a, int b) {
		return (a == 0 || b == 0 || a == n || b == n);
	}
	
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
			int vrednostB = 0;
			int vrednostC = 0;
			// Preverimo, kateri igralec ima v kotih figuro.
			// To kodo bi se dalo spravit v spodnjo for zanko, a bi potrebovali
			// vec if stavkov
			for (int k = 0; k<Igra.N; k = k+(Igra.N - 1)){
				for (int h = 0; h<Igra.N; h = h+(Igra.N - 1)){
					switch(plosca[k][h]){
					case CRNO: vrednostB+=VREDNOST_KOTA;
					case BELO: vrednostC+=VREDNOST_KOTA;
					default: break;
					}
				}
			}
			// Preverimo, ali so figure na mestih, ki so sosednji kotom.
			// Podobno, zaradi lazjega zapisa in manj if stavkov, se ta koda piše
			// izven spodnje glavne for zanke
			for (int a = 0; a<=1; a++){
				for (int b = 0; b<=1; b++){
					// Koda se na tak nacin zelo skrajša, ni potrebno naredit
					// treh for zank.
					if (a != 0 && b != 0){
						for (int k = a; k<Igra.N; k = k+(Igra.N - (1 + 2*a))){
							for (int h = b; h<Igra.N; h = h+(Igra.N - (1 + 2*b))){
								switch(plosca[k][h]){
								case CRNO: vrednostB+=VREDNOST_SOSEDOV_KOTA;
								case BELO: vrednostC+=VREDNOST_SOSEDOV_KOTA;
								default: break;
								}
							}
						}
					}
				}
				
			}
			// Dolociti je treba vrednost po razlicnih kriterijih, sprehodimo 
			// se po vseh poljih
			for (int i = 0; i < Igra.N; i++) {
				for (int j = 0; j < Igra.N; j++) {
					// Dodelimo igralcu vrednost roba, ce ima polje na robu,
					// ne smemo steti sosedov roba
					// Sprva po horizontalni smeri
					if (i == 0 || i == (Igra.N - 1)){
						if (!(j < 2) && !(j > (Igra.N - 2))){
							switch(plosca[i][j]){
							case CRNO: vrednostB+=VREDNOST_ROBA;
							case BELO: vrednostC+=VREDNOST_ROBA;
							default: break;
							}
						}
					}
					else{
						// Se vertikalna smer
						if (j == 0 || j == (Igra.N - 1)){
							if (!(i < 2) && !(i > (Igra.N - 2))){
								switch(plosca[i][j]){
								case CRNO: vrednostB+=VREDNOST_ROBA;
								case BELO: vrednostC+=VREDNOST_ROBA;
								default: break;
								}
							}
						}
						else{
							// Vsi preostali bodo za zdaj imeli vrednost 1
							switch(plosca[i][j]){
							case CRNO: vrednostB+=VREDNOST_NEROBNEGA;
							case BELO: vrednostC+=VREDNOST_NEROBNEGA;
							default: break;
							}
						}
					}
				}
			}
			// Sedaj bomo dodelili se vrednost, glede na to, koliko ima igralec
			// moznih potez
			if (jaz == Igralec.BELI){
				vrednostB+=igra.seznamDovoljenih().size() * VREDNOST_MOZNE_POTEZE;
			}
			else{
				vrednostC+=igra.seznamDovoljenih().size() * VREDNOST_MOZNE_POTEZE;
			}
			return (jaz == Igralec.CRNI ? (vrednostC - vrednostB) : (vrednostB - vrednostC));
		}
		return 42;
	}
}



