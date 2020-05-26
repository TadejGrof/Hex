	package inteligenca;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

import splosno.KdoIgra;

import splosno.Koordinati;
import logika.Igra;
import logika.Igralec;
import logika.Plosca;
import logika.Plosca.NajkrajsaPot;

public class Inteligenca extends KdoIgra {
	public static final int LAHKO = 0;
	public static final int SREDNJE = 1;
	public static final int TEZKO = 2;
	public static final int TEKMOVANJE = 3;
	
	private int scoreMax = Integer.MAX_VALUE;
	private int scoreMin = -scoreMax;
	
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
		super("ImeNajineSkupine");
		this.tip = TEKMOVANJE;
		this.globina = 5;
	}
	
	public Inteligenca(int tip) {
		super("Racunalnik");
		this.tip = tip;
		if(tip == LAHKO) {
			this.globina = 1;
		} else if ( tip == SREDNJE) {
			this.globina = 3;
		} else if (tip == TEZKO) {
			this.globina = 5;
		}
	}
	
	// Tukaj mora izbrati potezo.
	// Zaenkrat sem dal random za vse, da sem preveril delovanje
	public Koordinati izberiPotezo(Igra igra) {
		return minimax(igra, igra.igralecNaPotezi);
		//Random random = new Random();
		//ArrayList<Koordinati> moznePoteze = igra.veljavnePoteze();
		
		//int index = random.nextInt(moznePoteze.size());
		//Koordinati poteza = moznePoteze.get(index);
		//return poteza;
	}
	
	public Koordinati minimax(Igra igra, Igralec jaz) {
		int ocena;
		ArrayList<Koordinati> veljavne = igra.veljavnePoteze();
		OcenjenaPoteza najboljsaPoteza = null;
		for(Koordinati poteza: veljavne) {
			Igra kopijaIgre = igra.kopirajIgro();
			kopijaIgre.odigraj(poteza);
			ocena = oceniPozicijo(kopijaIgre, jaz);
			if(najboljsaPoteza == null) {
				najboljsaPoteza = new OcenjenaPoteza(poteza,ocena);
			} else {
				if (ocena > najboljsaPoteza.ocena) najboljsaPoteza = new OcenjenaPoteza(poteza,ocena);
			}
		}
		return najboljsaPoteza.koordinati;
	}
	
	public int oceniPozicijo(Igra igra, Igralec jaz) {
		NajkrajsaPot mojaPot = igra.plosca.najkrajsaPot(igra.getIgralecIndex(jaz));
		if(mojaPot.jeKoncna()) {
			return Integer.MAX_VALUE;
		} 
		NajkrajsaPot nasprotnikovaPot = igra.plosca.najkrajsaPot(igra.getIgralecIndex(igra.nasprotnik(jaz)));
		if(nasprotnikovaPot.jeKoncna()) {
			return Integer.MIN_VALUE;
		}
		return nasprotnikovaPot.steviloPraznih() - mojaPot.steviloPraznih();
	}
	
	public class OcenjenaPoteza{
		public int ocena;
		public Koordinati koordinati;
		
		public OcenjenaPoteza(Koordinati koordinati, int ocena) {
			this.koordinati = koordinati;
			this.ocena = ocena;
		}

		//Random random = new Random();
		//ArrayList<Koordinati> moznePoteze = igra.veljavnePoteze();
		//Koordinati k = new Koordinati(0,0);
		//int index = random.nextInt(moznePoteze.size());
		//ArrayList<Koordinati> najkrajšaPot = najkrajšaPot(igra, k);
		//Koordinati poteza = najkrajšaPot.get(0);
		//igra.printIntMtrx(igra.plosca.getMatrika());
		//return poteza;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public Koordinati izberiPotezo2(Igra igra) {
		if (this.plosca == igra.plosca) {
			System.out.println("ne posodablja plošče");
		}
		Random random = new Random();
		Koordinati k = izboljšanMinimax(igra);
		System.out.println("začetek");
		igra.plosca.getMatrika();
		System.out.println("konec getMatrika()");
		ArrayList<Koordinati> najkrajšaPot = najkrajšaPot(igra, k);
		System.out.println("konec najkrajšePoti");
		int i = random.nextInt(najkrajšaPot.size());
		Koordinati poteza = najkrajšaPot.get(1);
		this.plosca = igra.plosca;
		return poteza;
		//Koordinati naslednjaPoteza = MiniMax(igra, igralec);
		//return naslednjaPoteza;
	}
	
	private int evaluateSosedje(Igra igra, Koordinati k, int i) {
		int score = 0;
		ArrayList<Koordinati> sosedi = igra.plosca.sosednje(k.getX(), k.getY());
		
		// rad bi za vsako točko izmed sosedov pogledal,
		// če je ista (torej ima vrednost i) in jo prištel k oceni
		int[][] matrika = igra.setIntMtrx();
		if (i == 1) {
			for (Koordinati sosed : sosedi) {
				int x = sosed.getX();
				int y = sosed.getY();
				if (matrika[y][x] == 1) {
					score += 1;
				}
			}
		} else if (i == 2) {
			for (Koordinati sosed : sosedi) {
				int x = sosed.getX();
				int y = sosed.getY();
				if (matrika[y][x] == 2) {
					score += 1;
				}
			}
		}
		return score;
	}
	
	// bolje, da gremo po tako generiranem seznamu, da bomo po vrsti pregledovali
	// polja, ki so po absolutni vrednosti najmanj oddaljena od pravokotne projekcije
	// na željeno stran
	private static ArrayList<Integer> možniIndeksi (int indeks, int velikost) {
		ArrayList<Integer> vrstniRedIndeksov = new ArrayList();
		ArrayList<Integer> večjiIndeksi = new ArrayList();
		ArrayList<Integer> manjšiIndeksi = new ArrayList();
		for (int i = 0; i < velikost; i++) {
			if (i == indeks) {
				vrstniRedIndeksov.add(i);
			} else if (i < indeks) {
				manjšiIndeksi.add(i);
			} else if (i > indeks) {
				večjiIndeksi.add(i);
			}
		}
		Collections.reverse(manjšiIndeksi);
		
		for (int i = 0; i < večjiIndeksi.size() && i < manjšiIndeksi.size(); i++) {
			vrstniRedIndeksov.add(večjiIndeksi.get(i));
			vrstniRedIndeksov.add(manjšiIndeksi.get(i));
		}
		
		if (večjiIndeksi.size() > manjšiIndeksi.size()) {
			int razlika = večjiIndeksi.size() - manjšiIndeksi.size();
			for (int i = 1; manjšiIndeksi.size() + i <= večjiIndeksi.size(); i++) {
				vrstniRedIndeksov.add(večjiIndeksi.get(manjšiIndeksi.size() + i - 1));
			}
		} else if (manjšiIndeksi.size() > večjiIndeksi.size()) {
			int razlika = manjšiIndeksi.size() - večjiIndeksi.size();
			for (int i = 0; (večjiIndeksi.size() + i) != manjšiIndeksi.size(); i++) {
				vrstniRedIndeksov.add(manjšiIndeksi.get(večjiIndeksi.size() + i));
			}
		}
		return vrstniRedIndeksov;
	}
	
	private ArrayList<Koordinati> najkrajšaPot (Igra igra, Koordinati k) {
		Igralec igralec = igra.igralecNaPotezi;
		int igralčevIndeks = igra.getIgralecIndex(igralec);
		
		// seznam koordinat, ki sestavljajo najkrajšo pot
		ArrayList<Koordinati> najkrajšaPot = new ArrayList<Koordinati>();
		
		int velikost = igra.plosca.getVelikost();
		
		// matriko naše igre spremeni v matriko ničel in enic, odvisno od tega,
		// ali se trenutni igralec lahko pomika čez dana polja ali ne
		
		int[][] matrika = pretvoriMatriko(igra);
		igra.printIntMtrx(matrika);
		
		int[] začetnoPolje = {k.getX(), k.getY()};
		
		LinkedList<BoljšaPot> seznamKoordinat = new LinkedList();
		
		// igralec1 -> (gor - dol)
		// najkrajša pot bo od začetnegaPolja do gor + najkrajša pot od začetnegaPolja dol
		if (igralčevIndeks == 1) {
			ArrayList<Integer> možniIndeksi = možniIndeksi(k.getX(), velikost);
			// dokler ni na mestu zgoraj in mestu spodaj polje enako 1 ali 0 išče med možnimi indeksi
			// če je polje enako 1, poišče BoljšoPot do temu primerno enega indeksa nižje/višje
			// če je polje enako 0, poišče BoljšoPot do tega polja
			
			// izbira na zgornjem delu
			for (int i = 0; i < možniIndeksi.size(); i++) {
				if (matrika[0][možniIndeksi.get(i)] == 1) {
					int[] končnoPolje = {0, možniIndeksi.get(i)};
					seznamKoordinat = BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje);
					break;
			}
			}
			// izbira na spodnjem delu
			for (int j = 0; j < možniIndeksi.size(); j++) {
				if (matrika[velikost][možniIndeksi.get(j)] == 1) {
					int[] končnoPolje = {velikost, možniIndeksi.get(j)};
					seznamKoordinat.addAll(BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje));
					break;
				}
			}
		}
		// igralec2 -> (levo - desno)
		else if (igralčevIndeks == 2) {
			ArrayList<Integer> možniIndeksi = možniIndeksi(k.getY(), velikost);
			// izbira na levi
			for (int i = 0; i < možniIndeksi.size(); i++) {
				if (matrika[možniIndeksi.get(i)][0] == 1) {
					int[] končnoPolje = {možniIndeksi.get(i), 0};
					seznamKoordinat = BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje);
					break;
				}
			}
			// izbira na desni
			for (int j = 0; j < možniIndeksi.size(); j++) {
				if (matrika[možniIndeksi.get(j)][velikost] == 1) {
					int[] končnoPolje = {možniIndeksi.get(j), velikost};
					seznamKoordinat.addAll(BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje));
					break;
				}
			}
		}
		
		for (BoljšaPot koordinati : seznamKoordinat) {
			Koordinati dodajKoordinato = BoljšaPot.vKoordinati(koordinati);
			najkrajšaPot.add(dodajKoordinato);
		}
		
		return najkrajšaPot;
	}
	
	// Minimax algoritem
	private Koordinati MiniMax(Igra igra, int igralec) {
		int najboljšiScore = 0;
		Koordinati prefKoordinata = new Koordinati(0, 0);
		
		
		ArrayList<Koordinati> prazne = igra.veljavnePoteze();
		
		LinkedHashMap<Koordinati, Integer> ovrednotenePoteze = new LinkedHashMap<Koordinati, Integer>();
		
		// ovrednotimo vse možnosti (gledamo samo s stališča igralca)
		// in jih damo v slovar
		for (Koordinati k : prazne) {
			// za vsako možnost naredimo novo igro, 
			// da lahko ocenimo nastalo situacijo
			
			// - ta del bo pomemben, ko bomo ocenjevali še po čem drugem
			// kot po številu sosedov
			// Igra kopija = Igra.kopirajIgro(igra);
			// kopija.odigraj(k);
			
			int trenutniScore = evaluateSosedje(igra, k, igralec);
			ovrednotenePoteze.put(k, trenutniScore);
		}
		for (Koordinati k : ovrednotenePoteze.keySet()) {
			int vrednost = ovrednotenePoteze.get(k);
			if (vrednost > najboljšiScore) {
				najboljšiScore = vrednost;
				prefKoordinata = k;
			}
		}
		return prefKoordinata;
	}
	
	// še vedno Minimax (ni še alfa-beta prunning-a), 
	// ampak izbere naslednjo koordinato učinkoviteje, kot prvi
	private Koordinati izboljšanMinimax(Igra igra) {
		int score = 1000;
		
		// delovalo bo na trenutnem igralcu, ker bo NadzornikIgre tako ali tako dovolil avtomatsko pisanje samo računalniku
		Igralec igralec = igra.igralecNaPotezi;
		
		// getIgralecIndeks dela v redu - igralec1 -> 1 in igralec2 -> 2
		int igralčevIndeks = igra.getIgralecIndex(igralec);
		int nasprotnikovIndeks = 0;
		if (igralčevIndeks == 1) {
			nasprotnikovIndeks = 2;
		} else if (igralčevIndeks == 2) {
			nasprotnikovIndeks = 1;
		}
		
		// začetno izbrano koordinato lahko BŠS nastavimo na (0,0)
		Koordinati prefKoordinata = new Koordinati(0,0);
		// sem bomo shranjevali vse koordinate, ki sestavljajo najkrajšo pot
		ArrayList<Koordinati> pot = new ArrayList<Koordinati>();
		
		int velikostPlosce = igra.plosca.getVelikost();
		// Color igralecBarva = igra.getIgralecBarva(igralec);
		
		// vse možne poteze, ki se jih še lahko izbere
		ArrayList<Koordinati> prazne = igra.veljavnePoteze();
		// sem se bodo shranjevale vse poti, ki bodo imele kot ključ pot, torej seznam koordinat, kot vrednost pa dolžino te poti
		// cilj bo imeti čim manjšo dolžino
		LinkedHashMap<ArrayList<Koordinati>, Integer> ovrednotenePoti = new LinkedHashMap<ArrayList<Koordinati>, Integer>();
		
		// poiščiVsePoteze dela pravilno (na začetku si izbere kot barvo belo, vendar jo v naslednjem if stavku spremeni
		// v barvo igralca, ki ima podani indeks (1 -> igralec1.getBarva(), 2 -> igralec2.getBarva())
		ArrayList<Koordinati> igralčevePoteze = igra.poisciVsePoteze(igralčevIndeks);
		ArrayList<Koordinati> nasprotnikovePoteze = igra.poisciVsePoteze(nasprotnikovIndeks);
		
		// če na plošči ni izbrane nobene druge vrednosti in je računalnik prvi, naj si izbere naključno koordinato
		if (!igra.plosca.getStanje().containsValue(igra.getIgralecBarva(igralčevIndeks)) || !igra.plosca.getStanje().containsValue(igra.getIgralecBarva(nasprotnikovIndeks))) {
			prefKoordinata = Igra.naključniKoordinati();
		} else {
			// pogledamo, če je že kakšna računalnikova poteza odigrana
				if (Plosca.getStanje().containsValue(igra.getIgralecBarva(igralčevIndeks))) {
					for (Koordinati k : igralčevePoteze) {
						ArrayList<Koordinati> najkrajšaPot = najkrajšaPot(igra, k);
						int dolžinaPoti = najkrajšaPot.size();
						ovrednotenePoti.put(najkrajšaPot, dolžinaPoti);
					}
				} else {
					for (Koordinati k : nasprotnikovePoteze) {
						
					}
				}
		}
		// gremo čez vsako pot in jo ocenimo (najkrajša pot je najboljša)
		// če je krajša od prejšnje, jo zamenjamo in tako gremo čez vse
		for (ArrayList<Koordinati> trenutnaPot : ovrednotenePoti.keySet()) {
			if (trenutnaPot.size() < score) {
				score = trenutnaPot.size();
				this.pot = trenutnaPot;
			}
		}
		// tu je še možnost za napredek, da si izberemo kakšno bolj zanimivo koordinato na poti 
		// kot prvo, ampak zaenkrat bo čisto v redu
		prefKoordinata = this.pot.get(0);
		return prefKoordinata;
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
