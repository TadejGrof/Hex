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
import logika.Plosca;

public class Inteligenca extends KdoIgra {
	public static final int LAHKO = 0;
	public static final int SREDNJE = 1;
	public static final int TEZKO = 2;
	
	private int scoreMax = 10000;
	private int scoreMin = -10000;
	
	// da se nastavi, ali je racunalnik prvi ali drugi igralec
	private int racunalnik;
	
	private int globina;
	
	public Inteligenca(String ime) {
		super(ime);
	}

	public Inteligenca() {
		super("ImeNajineSkupine");
		this.globina = 5;
	}
	
	public Inteligenca(int tip) {
		super("Racunalnik");
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
		Random random = new Random();
		ArrayList<Koordinati> najkrajšaPot = najkrajšaPot(Igra igra, Koordinati k, int igralec);
		int i = random.nextInt(najkrajšaPot.size());
		Koordinati poteza = najkrajšaPot.get(i);
		return poteza;
		//Koordinati naslednjaPoteza = MiniMax(igra, igralec);
		//return naslednjaPoteza;
	}
	
	private int evaluateSosedje(Igra igra, Koordinati k, int i) {
		int score = 0;
		ArrayList<Koordinati> sosedi = Plosca.sosednje(k.getX(), k.getY());
		
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
	
	private ArrayList<Koordinati> najkrajšaPot (Igra igra, Koordinati k, int igralec) {
		ArrayList<Koordinati> najkrajšaPot = new ArrayList<Koordinati>();
		int velikost = igra.plosca.getVelikost();
		int[][] matrika = igra.plosca.getMatrika();
		int[] začetnoPolje = {k.getX(), k.getY()};
		LinkedList<BoljšaPot> seznamKoordinat = new LinkedList();
		// igralec1 -> (gor - dol)
		// najkrajša pot bo od začetnegaPolja do gor + najkrajša pot od začetnegaPolja dol
		if (igralec == 1) {
			ArrayList<Integer> možniIndeksi = možniIndeksi(k.getX(), velikost);
			// dokler ni na mestu zgoraj in mestu spodaj polje enako 1 ali 0 išče med možnimi indeksi
			// če je polje enako 1, poišče BoljšoPot do temu primerno enega indeksa nižje/višje
			// če je polje enako 0, poišče BoljšoPot do tega polja
			
			// izbira na zgornjem delu
			for (int i = 0; i < možniIndeksi.size(); i++) {
				if (matrika[0][možniIndeksi.get(i)] == igralec) {
					int[] končnoPolje = {0, možniIndeksi.get(i)};
					seznamKoordinat = BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje);
					break;
			}
			}
			// izbira na spodnjem delu
			for (int j = 0; j < možniIndeksi.size(); j++) {
				if (matrika[velikost][možniIndeksi.get(j)] == igralec) {
					int[] končnoPolje = {velikost, možniIndeksi.get(j)};
					seznamKoordinat.addAll(BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje));
					break;
				}
			}
		}
		// igralec2 -> (levo - desno)
		else if (igralec == 2) {
			ArrayList<Integer> možniIndeksi = možniIndeksi(k.getY(), velikost);
			// izbira na levi
			for (int i = 0; i < možniIndeksi.size(); i++) {
				if (matrika[možniIndeksi.get(i)][0] == igralec) {
					int[] končnoPolje = {možniIndeksi.get(i), 0};
					seznamKoordinat = BoljšaPot.poiščiPot(matrika, začetnoPolje, končnoPolje);
					break;
				}
			}
			// izbira na desni
			for (int j = 0; j < možniIndeksi.size(); j++) {
				if (matrika[možniIndeksi.get(j)][velikost] == igralec) {
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
	
	private int boljšiEvaluate (Igra igra, Koordinati k, int igralec) {
		int score = 0;
		int velikost = Plosca.getVelikost();
		// glede na int igralca (torej 1 ali 2) je odvisno, katere poteze bodo bolje ovrednotene
		
		if (igralec == 1) {
			for (Koordinati sosed : Plosca.sosednje(k.getX(), k.getY())) {
				int x = sosed.getX();
				int y = sosed.getY();
				
			}
		} else if (igralec == 2) {
			
		}
		return score;
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
		// izberemo najboljšo možnost iz vseh ocen
		// tole bo bolj smiselno z iteratorjem (ali pač?)
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
	private Koordinati izboljšanMinimax(Igra igra, int igralec) {
		int score = 0;
		int nasprotnik = 0;
		Koordinati prefKoordinata = new Koordinati(0,0);
		
		int velikostPlosce = Plosca.getVelikost();
		// Color igralecBarva = igra.getIgralecBarva(igralec);
		
		ArrayList<Koordinati> prazne = igra.veljavnePoteze();
		LinkedHashMap<Koordinati, Integer> ovrednotenePoteze = new LinkedHashMap<Koordinati, Integer>();
		
		if (igralec == 1) {
			nasprotnik = 2;
		} else if (igralec == 2) {
			nasprotnik = 1;
		}
		
		ArrayList<Koordinati> igralčevePoteze = igra.poisciVsePoteze(igralec);
		ArrayList<Koordinati> nasprotnikovePoteze = igra.poisciVsePoteze(nasprotnik);
		
		// pri prvi izbiri koordinate naj računalnik, ki
		// postavi nekam med sosednje točke nasprotnikove poteze (če obstaja)
		// (če je računalnik prvi na potezi in ni še nič postavljenih blockov, 
		// a je narobe, če je prva poteza popolnoma naključna?)
		
		if (Plosca.getStanje().containsValue(1) || Plosca.getStanje().containsValue(2)) {
			prefKoordinata = Igra.naključniKoordinati();
		} else {
			
			// * funkcija, ki poišče vse že narete poteze prvega in drugega igralca (da se napolnita ArrayLista igralčevePoteze
			// in nasprotnikovePoteze accordingly (izmed bližnjih se bo izbrala najboljša poteza
			// - boljši algoritem bi bil, da pregleda število sosedov v točkah
			// - pomisli, kako se bo štelo, da je bila izbrana pot v pravo smer (nova funkcija, ki glede na integer in Koordinati prišteje scoru)
				if (Plosca.getStanje().containsValue(igralec)) {
					for (Koordinati k : igralčevePoteze) {
						ArrayList<Koordinati> najkrajšaPot = najkrajšaPot(igra, k, igralec);						
					}
				} else {
					for (Koordinati k : nasprotnikovePoteze) {
						
					}
				}
		}
		return prefKoordinata;
	}
	
}
