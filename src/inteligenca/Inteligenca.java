package inteligenca;

import java.util.ArrayList;

import koordinati.Koordinati;
import logika.Igra;
import logika.Plosca;

public class Inteligenca {
	
	private static int scoreMax = 10000;
	private static int scoreMin = -10000;
	
	private int globina;
	
	// 1 -> racunalnik = igralec1
	// 2 -> racunalnik = igralec2
	private int racunalnik;
	
	private void setGlobina (int globina) {
		this.globina = globina;
	}
	
	private void setRacunalnik (int igralec) {
		if (igralec != 1 || igralec != 2) {
		} else {
			this.racunalnik = igralec;
		}
	}
	
	
	// ocenimo z Minimaxom
	private int evaluate () {
		int score = 0;
		return score;
	}
	
	// Minimax algoritem
	private void MiniMax(Igra igra) {
		Igra kopija = Igra.kopirajIgro(igra);
		ArrayList<Koordinati> prazne = igra.veljavnePoteze();
		
		for (Koordinati k : prazne) {
			
		}
	}
	
}
