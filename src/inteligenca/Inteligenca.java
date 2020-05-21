	package inteligenca;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

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
		super("Racunalnik");
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
	public Koordinati izberiPotezo(Igra igra, int igralec) {
		Koordinati naslednjaPoteza = MiniMax(igra, igralec);
		return naslednjaPoteza;
	}

	//private void setGlobina (int globina) {
	//	this.globina = globina;
	//}
	
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
		Koordinati prefKoordinata = new Koordinati(0,0);
		
		int velikostPlosce = Plosca.getVelikost();
		// Color igralecBarva = igra.getIgralecBarva(igralec);
		
		ArrayList<Koordinati> prazne = igra.veljavnePoteze();
		LinkedHashMap<Koordinati, Integer> ovrednotenePoteze = new LinkedHashMap<Koordinati, Integer>();
		
		ArrayList<Koordinati> igralčevePoteze = new ArrayList<Koordinati>();
		ArrayList<Koordinati> nasprotnikovePoteze = new ArrayList<Koordinati>();
		
		if (igralec == 1) {
			int nasprotnik = 2;
		} else if (igralec == 2) {
			int nasprotnik = 1;
		}
		
		// pri prvi izbiri koordinate naj računalnik, ki
		// postavi nekam med sosednje točke nasprotnikove poteze (če obstaja)
		// (če je računalnik prvi na potezi in ni še nič postavljenih blockov, 
		// a je narobe, če je prva poteza popolnoma naključna?)
		
		if (Plosca.getStanje().containsValue(1) || Plosca.getStanje().containsValue(2)) {
			prefKoordinata = Igra.naključniKoordinati();
		} else {
			// - funkcija, ki poišče vse že narete poteze prvega in drugega igralca (da se napolnita ArrayLista igralčevePoteze
			// in nasprotnikovePoteze accordingly (izmed bližnjih se bo izbrala najboljša poteza
			// - boljši algoritem bi bil, da pregleda število sosedov v točkah
			// - pomisli, kako se bo štelo, da je bila izbrana pot v pravo smer (nova funkcija, ki glede na integer in Koordinati prišteje scoru)
		}
		return prefKoordinata;
	}
	
}
