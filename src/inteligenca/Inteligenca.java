	package inteligenca;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
	
import splosno.KdoIgra;

import splosno.Koordinati;
import logika.Igra;
import logika.Igralec;
import logika.Plosca;
import logika.Plosca.Most;
import logika.Plosca.NajkrajsaPot;

public class Inteligenca extends KdoIgra {
	public static final int LAHKO = 0;
	public static final int SREDNJE = 1;
	public static final int TEZKO = 2;
	public static final int TEKMOVANJE = 3;
	
	private int scoreMax = Integer.MAX_VALUE;
	private int scoreMin = -scoreMax;
	
	public static int steviloPotez = 0;
	public static int steviloRacunanihPoti;
	
	private ArrayList<Koordinati> pot;
	
	// da se nastavi, ali je racunalnik prvi ali drugi igralec
	private int racunalnik;
	
	private int globina;
	private int tip;
	
	public Plosca plosca;
	
	public Inteligenca(String ime) {
		super(ime);
	}

	public Inteligenca() {
		super("Hex'n'Šus");
		// možna imena so še
		// - ekipaBrogrammer
		// - bitsPlease
		// - Algebros
		// - (Math.PI)zza
		// - Epsilon < 0
		this.tip = TEKMOVANJE;
		this.globina = 5;
	}
	
	public Inteligenca(int tip) {
		super("Racunalnik");
		this.tip = tip;
		if(tip == LAHKO) {
			this.globina = 2;
		} else if ( tip == SREDNJE) {
			this.globina = 3;
		} else if (tip == TEZKO) {
			this.globina = 5;
		}
	}
	
	public Koordinati izberiPotezo(Igra igra) {
		if(igra.poteze.size() == 0) {
			// prva poteza:
			return nakljucnaSredinska(igra);
		} else if(igra.poteze.size()== 1) {
			return drugaPoteza(igra);
		} else if( tip == LAHKO){
			ponastaviVrednosti();
			OcenjenaPoteza poteza = alphabetaPoteza(igra, 1, scoreMin, scoreMax, igra.igralecNaPotezi);
			printVrednosti();
			System.out.println("IGRAM: " + poteza.koordinati + "Z OCENO: " + poteza.ocena);
			return poteza.koordinati;
		} else if (tip == SREDNJE ){
			ponastaviVrednosti();
			OcenjenaPoteza poteza = alphabetaPoteza(igra, 3, scoreMin, scoreMax, igra.igralecNaPotezi);
			printVrednosti();
			System.out.println(igra.igralecNaPotezi.toString() + "IGRA: " + poteza.koordinati + "Z OCENO: " + poteza.ocena);
			return poteza.koordinati;
		} else if (tip == TEZKO ){
			ponastaviVrednosti();
			OcenjenaPoteza poteza = alphabetaPoteza(igra, 4, scoreMin, scoreMax, igra.igralecNaPotezi);
			printVrednosti();
			System.out.println(igra.igralecNaPotezi.toString() + " IGRA: " + poteza.koordinati + "Z OCENO: " + poteza.ocena);
			return poteza.koordinati;
		}
		return null;
	}
	
	public void ponastaviVrednosti() {
		this.steviloPotez = 0;
		this.steviloRacunanihPoti = 0;
	}
	public void printVrednosti() {
		System.out.println("STEVILO RACUNANIH: " + steviloPotez);
		System.out.println("STEVILO RACUNANIH POTI: " + steviloRacunanihPoti);
	}
	
	// Funkcija, ki jo uporabimo za določitev prve poteze. Izmed vseh sredinskih ploščic
	// naključno izberemo tisto, na kateri odigramo prvo potezo.
	public Koordinati nakljucnaSredinska(Igra igra) {
		ArrayList<Koordinati> sredinske = igra.plosca.getSredinske();
		Random random = new Random();
		return sredinske.get(random.nextInt(sredinske.size()));
	}
	
	
	// Funkcija, ki glede na odigrano prvo potezo prvega igralca, odigra potezo drugega igralca temu primerno.
	public Koordinati drugaPoteza(Igra igra) {
		Koordinati zacetnaPoteza = igra.poteze.get(0).koordinati;
		if(igra.plosca.jeSredinska(zacetnaPoteza)) {
			if (igra.plosca.velikost > 5) {
				return new Koordinati(zacetnaPoteza.getX() + 2, zacetnaPoteza.getY() + 3);
			} else {
				return new Koordinati(zacetnaPoteza.getX() + 1, zacetnaPoteza.getY() + 1);
			}
		} else {
			for (Koordinati t: igra.plosca.getSredinske()) {
				if (igra.plosca.razdalja(zacetnaPoteza, t) > 2){
					return t;
				}
			}
		}
		ArrayList<Koordinati> poteze = igra.veljavnePoteze();
		for(Koordinati t:poteze) {
			if(igra.plosca.razdalja(zacetnaPoteza,t) > 1) {
				return t;
			}
		}
		Random random = new Random();
		return poteze.get(random.nextInt(poteze.size()));
	}
	
	// prvi izmed seznamov je seznam tistih sosedov, ki jih je položil igralec na potezi
	// drugi seznam je seznam sosedov, ki jih je položil nasprotnik igralca na potezi
	// tretji seznam predstavljajo sosedi, ki so še nezasedena polja
	public ArrayList<ArrayList<Koordinati>> vrniSeznameSosedov(Igra igra, Koordinati k) {
		ArrayList<Koordinati> točkeNaPotezi = new ArrayList<Koordinati>();
		ArrayList<Koordinati> točkeNasprotnika = new ArrayList<Koordinati>();
		ArrayList<Koordinati> točkePrazne = new ArrayList<Koordinati>();
		
		Igralec naPotezi = igra.igralecNaPotezi;
		Igralec nasprotnik = igra.nasprotnik(naPotezi);
		
		Color barvaNaPotezi = naPotezi.getBarva();
		System.out.println("barva na potezi je " + barvaNaPotezi);
		Color barvaNasprotnik = nasprotnik.getBarva();
		System.out.println("nasprotnikova barva je " + barvaNasprotnik);
		
		ArrayList<Koordinati> sosedi = igra.plosca.sosednje(k.getX(), k.getY());
		int[][] matrika = igra.plosca.getMatrika();
		
		if (sosedi.size() > 0) {
			// za vsako točko izmed sosedov ugotovimo, kam spada
			for (Koordinati sosed : sosedi) {
				int x = sosed.getX();
				int y = sosed.getY();
				Koordinati trenutnaKoordinata = new Koordinati (x, y);
				System.out.println(trenutnaKoordinata);
				if (matrika[y][x] == igra.getIgralecIndex(naPotezi)) {
					točkeNaPotezi.add(trenutnaKoordinata);
					System.out.println("dodali smo v točkeNaPotezi");
				} else if (matrika[y][x] == igra.getIgralecIndex(nasprotnik)) {
					točkeNasprotnika.add(trenutnaKoordinata);
					System.out.println("dodali smo v točkeNasprotnika");
				} else {
					točkePrazne.add(trenutnaKoordinata);
					System.out.println("dodali smo v točkePrazne");
				}
			}
		}
		ArrayList<ArrayList<Koordinati>> končenSeznamSosedov = new ArrayList<ArrayList<Koordinati>>();
		končenSeznamSosedov.add(točkeNaPotezi);
		System.out.println("sosedi točke na potezi so " +točkeNaPotezi);
		končenSeznamSosedov.add(točkeNasprotnika);
		System.out.println("sosedi nasprotnika točke na potezi so " +točkeNasprotnika);
		končenSeznamSosedov.add(točkePrazne);
		System.out.println("sosedi, ki so prazne točke, so " +točkePrazne);
		return končenSeznamSosedov;
	}
	
	public int[][] matrikaSosedov (Igra igra, Koordinati k) {
		int x = k.getX();
		int y = k.getY();
		int velikostX = 3;
		int velikostY = 3;
		int velikost = igra.velikost;
		if (x == velikost - 1) {
			velikostX = 2;
		}
		if (x == 0) {
			velikostX = 2;
		}
		if (y == velikost - 1) {
			velikostY = 2;
		}
		if (y == 0) {
			velikostY = 2;
		}
		int[][] matrikaSosedov = new int[velikostY][velikostX];
		return matrikaSosedov;
	}
	
	// --minimax algoritem 1.0 (ne dela čisto v redu in ni ključen za delovanje kode)
	public OcenjenaPoteza minimax(Igra igra, int globina, Igralec jaz) {
		int ocena;
		ArrayList<Koordinati> veljavne = igra.veljavnePoteze();
		OcenjenaPoteza najboljsaPoteza = null;
		ArrayList<OcenjenaPoteza> najboljsePoteze = new ArrayList<OcenjenaPoteza>();
		
		steviloPotez ++;
		for(Koordinati poteza: veljavne) {
			// za vsako potezo odigramo svojo igro in potem pogledamo, kako daleč smo prišli
			Igra kopijaIgre = igra.kopirajIgro();
			kopijaIgre.odigraj(poteza);
			if(igra.konecIgre() | globina == 1) {
				ocena = oceniPozicijo(kopijaIgre,jaz);
			} else {
				ocena = minimax(kopijaIgre, globina - 1,jaz).ocena;
			}

			if(najboljsaPoteza == null) {
				najboljsaPoteza = new OcenjenaPoteza(poteza,ocena);
				najboljsePoteze.add(najboljsaPoteza);
			} else {
				if(ocena == najboljsaPoteza.ocena) {
					najboljsePoteze.add(new OcenjenaPoteza(poteza,ocena));
				} else {
					najboljsePoteze.clear();
					if (igra.igralecNaPotezi == jaz && ocena > najboljsaPoteza.ocena) {
						najboljsaPoteza = new OcenjenaPoteza(poteza,ocena);

					} else if(igra.igralecNaPotezi != jaz && ocena < najboljsaPoteza.ocena){
						najboljsaPoteza = new OcenjenaPoteza(poteza,ocena);
					}
					najboljsePoteze.add(najboljsaPoteza);
				}
				
			}
		}
		Random random = new Random();
		return najboljsePoteze.get(random.nextInt(najboljsePoteze.size()));
	}
	
	
	// -- alpha-beta algoritem, s katerim računalnik izbere naslednjo najbolj smiselno potezo
	public OcenjenaPoteza alphabetaPoteze(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		
		if (igra.igralecNaPotezi == jaz) {ocena = scoreMin;} else {ocena = scoreMax;}

		List<Koordinati> moznePoteze = igra.urejeneMoznePoteze();
		NajboljsePoteze najboljsePoteze = new NajboljsePoteze();
			
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremini vrednost kanditata. Zato ne more biti null.
		
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = igra.kopirajIgro();

			kopijaIgre.odigraj(p);

			int ocenap;

			if(kopijaIgre.konecIgre() | globina == 1) {
				ocenap = oceniPozicijo(kopijaIgre,jaz);
			} else {
				ocenap = alphabetaPoteze (kopijaIgre, globina-1, alpha, beta, jaz).ocena;;
			}
			steviloPotez ++;
			if (igra.igralecNaPotezi == jaz) { // Maksimiramo oceno
				if (ocenap >= ocena) { // mora biti > namesto >=
					ocena = ocenap;
					OcenjenaPoteza poteza = new OcenjenaPoteza(p,ocena);
					najboljsePoteze.addIfBest(poteza);
					alpha = Math.max(alpha,ocena);

				}
			} else { 
				if (ocenap <= ocena) {
					ocena = ocenap;
					OcenjenaPoteza poteza = new OcenjenaPoteza(p,ocena);
					najboljsePoteze.addIfBest(poteza);
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) {
				return najboljsePoteze.getBest();
			}
		}
		return najboljsePoteze.getBest();
	}
	
	// vsako potezo ocenimo in shrani v class OcenjenaPoteza
	public OcenjenaPoteza alphabetaPoteza(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;

		if (igra.igralecNaPotezi == jaz) {ocena = scoreMin;} else {ocena = scoreMax;}

		List<Koordinati> moznePoteze = igra.urejeneMoznePoteze();
		
		Koordinati kandidat = moznePoteze.get(0); // Možno je, da se ne spremini vrednost kanditata. Zato ne more biti null.
		
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = igra.kopirajIgro();

			kopijaIgre.odigraj(p);

			int ocenap;
			
			if(kopijaIgre.konecIgre() | globina == 1) {
				ocenap = oceniPozicijo(kopijaIgre,jaz);
			} else {
				ocenap = alphabetaPoteza (kopijaIgre, globina-1, alpha, beta, jaz).ocena;;
			}
			steviloPotez ++;
			if (igra.igralecNaPotezi == jaz) {
				if (ocenap > ocena) {
					ocena = ocenap;
					kandidat = p;
					alpha = Math.max(alpha,ocena);

				}
			} else { 
				if (ocenap < ocena) {
					ocena = ocenap;
					kandidat = p;
					beta = Math.min(beta, ocena);					
				}	
			}
			if (alpha >= beta) {
				return new OcenjenaPoteza(kandidat,ocena);
			}
		}
		return new OcenjenaPoteza(kandidat,ocena);
	}
	
//	public OcenjenaPoteza alfabeta (Igra igra, Koordinati k, int globina, int alfa, int beta, Igralec jaz) {
//		System.out.println("zagnali alfabeta");
//		if (globina == 0 || igra.konecIgre()) {
//			int trenutnaOcena;
//			Igra kopijaIgre = igra.kopirajIgro();
//			kopijaIgre.odigraj(k);
//			trenutnaOcena = oceniPozicijo(kopijaIgre, jaz);
//			OcenjenaPoteza poteza = new OcenjenaPoteza(k, trenutnaOcena);
//		}
//		
//		ArrayList<Koordinati> možnosti = igra.veljavnePoteze();
//		
//		// prvi igralec želi največji score, drugi najmanjšega
//		if (jaz == igra.igralec1) {
//			int najboljšaOcena = Integer.MIN_VALUE;
//			Koordinati zadnjaKoordinata = new Koordinati (0,0);
//			for (Koordinati trenutnaKoordinata : možnosti) {
//				int trenutnaOcena = alfabeta(igra, trenutnaKoordinata, globina - 1, alfa, beta, igra.igralec1).ocena;
//				najboljšaOcena = Math.max(najboljšaOcena, trenutnaOcena);
//				alfa = Math.max(alfa, trenutnaOcena);
//				zadnjaKoordinata = trenutnaKoordinata;
//				// ta del je tisto, kar v bistvu skrajša postopek
//				if (beta <= alfa) break;
//			}
//			OcenjenaPoteza poteza = new OcenjenaPoteza(zadnjaKoordinata, najboljšaOcena);
//			return poteza;
//		}
//		else if (jaz == igra.igralec2) {
//			int najboljšaOcena = Integer.MAX_VALUE;
//			Koordinati zadnjaKoordinata = new Koordinati (0,0);
//			for (Koordinati trenutnaKoordinata : možnosti) {
//				int trenutnaOcena = alfabeta(igra, trenutnaKoordinata, globina - 1, alfa, beta, igra.igralec2).ocena;
//				najboljšaOcena = Math.min(najboljšaOcena, trenutnaOcena);
//				beta = Math.min(najboljšaOcena, trenutnaOcena);
//				zadnjaKoordinata = trenutnaKoordinata;
//				if (beta <= alfa) break;
//			}
//			OcenjenaPoteza poteza = new OcenjenaPoteza(zadnjaKoordinata, najboljšaOcena);
//			return poteza;
//		}
//		// če slučajno pademo ven iz loopa
//		System.out.println("ven iz loopa!");
//		int score = 0;
//		Random random = new Random();
//		int i = random.nextInt(igra.velikost);
//		Koordinati zasilno = new Koordinati(0,0);
//		OcenjenaPoteza poteza = new OcenjenaPoteza(zasilno, score);
//		return poteza;
//	}
	
	public class NajboljsePoteze extends ArrayList<OcenjenaPoteza>{
		private static final long serialVersionUID = 1L;

		public void addIfBest(OcenjenaPoteza poteza) {
			if (this.size() == 0){
				add(poteza);
			} else {
				OcenjenaPoteza p = this.get(0);
				if (poteza.ocena > p.ocena) {
					this.clear();
					this.add(poteza);
				} else if(poteza.ocena == p.ocena) {
					this.add(poteza);
				}
			}
		}
		
		
		public OcenjenaPoteza getBest() {
			Random random = new Random();
			return this.get(random.nextInt(this.size()));
		}
		
		
	}
	
	public int oceniPozicijo(Igra igra, Igralec jaz) {
		int index = igra.getIgralecIndex(jaz);
		NajkrajsaPot mojaPot = null; NajkrajsaPot nasprotnikovaPot = null ;
		if (index == 1) {
			mojaPot = igra.plosca.najkrajsaPotIgralec1;
			nasprotnikovaPot = igra.plosca.najkrajsaPotIgralec2;
		} else if(index == 2) {
			mojaPot = igra.plosca.najkrajsaPotIgralec2;
			nasprotnikovaPot = igra.plosca.najkrajsaPotIgralec1;
		}
		if(mojaPot.jeKoncna()) {
			return Integer.MAX_VALUE;
		} 
		if(nasprotnikovaPot.jeKoncna()) {
			return Integer.MIN_VALUE;
		}
		int razlikaMostov = 2 * oceniMost(mojaPot,nasprotnikovaPot) - 3 * oceniMost(nasprotnikovaPot, mojaPot);
		int razlikaPraznih = nasprotnikovaPot.steviloPraznih() - mojaPot.steviloPraznih();
		int razlikaPoti = oceniPot(igra,mojaPot) - oceniPot(igra,nasprotnikovaPot);
		if(tip == SREDNJE) {
			return razlikaPraznih;
		} else {
			return razlikaPraznih + razlikaMostov + razlikaPoti;
		}
		
	
	}
	
	
	private int oceniPot(Igra igra, NajkrajsaPot pot) {
		int skupno = 0;
		int igralec = igra.plosca.getValue(pot.get(0));
		int vrednost = igralec;
		for(Koordinati t:pot) {
			int trenutno = igra.plosca.getValue(t);
			if(trenutno == 0 & trenutno == vrednost) {
				skupno = skupno - 1;
			} else if ( trenutno == igralec & trenutno == vrednost){
				skupno ++;
			}  
			else if ( trenutno != vrednost){
				vrednost = trenutno;
			} 	
		}
		return skupno;
	}
	private int oceniMost(NajkrajsaPot pot, NajkrajsaPot nasprotnikovaPot) {
		int stevilo = 0;
		ArrayList<Most> mosti = pot.mosti();
		stevilo += mosti.size();
		
		for(Most most:mosti) {
			if(most.jeDvojni()) {
				if(most.greCezPot(nasprotnikovaPot)) {
					stevilo += 5;
				} else {
					stevilo += 2;
				}
			} else {
				stevilo -= 3;
			}
			
		}
		return stevilo;
	}
	public class OcenjenaPoteza{
		public int ocena;
		public Koordinati koordinati;
		
		public OcenjenaPoteza(Koordinati koordinati, int ocena) {
			this.koordinati = koordinati;
			this.ocena = ocena;
		}
	}
		
	// novaMatrika označuje matriko, ki je pripravljena, da se jo spusti čez class BoljšaPot,
	// kjer se za najkrajšo pot upoštevajo zgolj celice, ki so označene z 1, mimo tistih, ki so 0,
	// pa funkcija ne more (na poljih z 0 so ovire na poti, torej soigralčevi že postavljeni blocki)
	public static int[][] pretvoriMatriko (Igra igra) {
		Igralec igralec = igra.igralecNaPotezi;
		int igralčevIndeks = igra.getIgralecIndex(igralec);
		int[][] novaMatrika = new int[igra.plosca.getVelikost()][igra.plosca.getVelikost()];
		int[][] matrika = igra.plosca.getMatrika();
		for (int i = 0; i < igra.plosca.getVelikost(); i++) {
			for (int j = 0; j < igra.plosca.getVelikost(); j++) {
				if (matrika[i][j] == igralčevIndeks || matrika[i][j] == 0) {
					novaMatrika[i][j] = 1;
				} else {
					novaMatrika[i][j] = 0;
				}
			}
		}
		return novaMatrika;
	}
		
}
