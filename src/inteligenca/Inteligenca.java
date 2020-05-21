package inteligenca;

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
	public Koordinati izberiPotezo(Igra igra) {
		return null;
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
	private void MiniMax(Igra igra, int igralec) {
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
		igra.plosca.odigraj(prefKoordinata, igralec);
	}
	
}
