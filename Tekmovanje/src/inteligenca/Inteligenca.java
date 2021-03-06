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

	
	private int globina;
	private int tip;
	
	public Plosca plosca;
	
	public Inteligenca(String ime) {
		super(ime);
	}

	public Inteligenca() {
		super("Hex'n'Šus");
		// možna imena so še
		// - Brogrammer
		// - (Math.PI)zza
		// - ekipaBrogrammer
		// - bitsPlease
		// - Algebros
		// - Epsilon < 0
		this.tip = TEKMOVANJE;
		this.globina = 4;
	}
	
	public Inteligenca(int tip) {
		super("Racunalnik");
		this.tip = tip;
		if(tip == LAHKO) {
			this.globina = 2;
		} else if ( tip == SREDNJE) {
			this.globina = 3;
		} else if (tip == TEZKO) {
			this.globina = 4;
		}
	}
	
	public Koordinati izberiPotezo(Igra igra) {
		if(igra.poteze.size() == 0) {
			// prva poteza:
			return nakljucnaSredinska(igra);
		} else if(igra.poteze.size()== 1) {
			return drugaPoteza(igra);
		} else{
			OcenjenaPoteza poteza = alphabetaPoteza(igra, globina, scoreMin, scoreMax, igra.igralecNaPotezi);
			System.out.println(igra.igralecNaPotezi.toString() + " IGRA: " + poteza.koordinati + "Z OCENO: " + poteza.ocena);
			return poteza.koordinati;
		}
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
			if (igra.plosca.velikost < 8) {
				return new Koordinati(zacetnaPoteza.getX() + 1, zacetnaPoteza.getY() + 2);
			} else {
				int x = zacetnaPoteza.getX();
				int y = zacetnaPoteza.getY();
				int sredina = igra.plosca.velikost / 2;
				if( y == sredina ) {
					return new Koordinati(x - 1, y - 2);
				} else if (y == sredina - 1) {
					//return new Koordinati(x + 1, y + 2);
					 return new Koordinati(x + 1, y + 3);
				} else if  (y == sredina + 1 ) {
					//return new Koordinati(x - 1, y -2);
					return new Koordinati(x-1, y-3);
				}
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
		ArrayList<Koordinati> sosedi1 = new ArrayList<Koordinati>();
		ArrayList<Koordinati> sosedi2 = new ArrayList<Koordinati>();
		ArrayList<Koordinati> točkePrazne = new ArrayList<Koordinati>();
		ArrayList<ArrayList<Koordinati>> končenSeznam = new ArrayList<ArrayList<Koordinati>>();
		
		int[][] matrika = igra.plosca.getMatrika();
		ArrayList<Koordinati> sosedi = igra.plosca.sosednje(k.getX(), k.getY());
		
		for (Koordinati sosed : sosedi) {
			int x = sosed.getX();
			int y = sosed.getY();
			
			if (matrika[y][x] == 1) {
				sosedi1.add(sosed);
			} else if (matrika[y][x] == 2) {
				sosedi2.add(sosed);
			} else {
				točkePrazne.add(sosed);
			}
		}
		končenSeznam.add(sosedi1);
		končenSeznam.add(sosedi2);
		končenSeznam.add(točkePrazne);
		return končenSeznam;
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
	
	
	// vsako potezo ocenimo in shrani v class OcenjenaPoteza
	public OcenjenaPoteza alphabetaPoteza(Igra igra, int globina, int alpha, int beta, Igralec jaz) {
		int ocena;
		
		if (igra.igralecNaPotezi == jaz) {ocena = scoreMin;} else {ocena = scoreMax;}
		
		List<Koordinati> moznePoteze = igra.urejeneMoznePoteze();
		
		Koordinati kandidat = moznePoteze.get(0);
		
		for (Koordinati p: moznePoteze) {
			Igra kopijaIgre = igra.kopirajIgro();

			kopijaIgre.odigraj(p);

			int ocenap;
			
			if(kopijaIgre.konecIgre() | globina == 1) {
				ocenap = oceniPozicijo(kopijaIgre,jaz);
			} else {
				ocenap = alphabetaPoteza (kopijaIgre, globina-1, alpha, beta, jaz).ocena;;
			}
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
	
	public int oceniPozicijo(Igra igra, Igralec jaz) {
		NajkrajsaPot mojaPot = igra.najkrajsaPot(jaz);
		NajkrajsaPot nasprotnikovaPot = igra.najkrajsaPot(igra.nasprotnik(jaz));

		if(mojaPot.jeKoncna()) return Integer.MAX_VALUE;
		if(nasprotnikovaPot.jeKoncna()) return Integer.MIN_VALUE;
		
		int steviloDvojnihMostov = mojaPot.steviloDvojnihMostov();
		int mojePrazne = mojaPot.steviloPraznih();
		int nasprotnikovePrazne = nasprotnikovaPot.steviloPraznih();
		int razlikaPraznih = nasprotnikovePrazne - mojePrazne;
		
		return 4 * razlikaPraznih + 2 * steviloDvojnihMostov;
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
