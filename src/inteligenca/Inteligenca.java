package inteligenca;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import koordinati.Koordinati;
import logika.Igra;
import logika.Plosca;

public class Inteligenca {
	
	private int globina;
	
	// 1 -> racunalnik = igralec1
	// 2 -> racunalnik = igralec2
	private int racunalnik;
	
	private static int scoreMax = 10000;
	private static int scoreMin = -10000;
	
	private void setGlobina (int globina) {
		this.globina = globina;
	}
	
	private void setRacunalnik (int igralec) {
		if (igralec != 1 || igralec != 2) {
		} else {
			this.racunalnik = igralec;
		}
	}
	
	
	private int evaluateSosedje(Koordinati k, int i) {
		int score = 0;
		ArrayList<Koordinati> sosedi = Plosca.sosednje(k.getX(), k.getY());
		
		// rad bi za vsako točko izmed sosedov pogledal,
		// če je ista (torej ima vrednost i) in jo prištel k oceni
		int[][] matrika = Igra.setIntMtrx();
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
			
			int trenutniScore = evaluateSosedje(k, igralec);
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
