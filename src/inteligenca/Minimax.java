package inteligenca;
import logika.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import splosno.Koordinati;

public class Minimax {
	
	private static LinkedHashMap<Koordinati, Integer> stanje;
	private int statusEna;
	private int statusDva;
	private static Color barvaEna;
	private static Color barvaDva;
	private ArrayList<Koordinati> pot;
	
	// prvi igralec bo imel cilj čimbolj povečati vrednost, drugi pa čimbolj zmanjšati, torej
	// nastavimo prvemu na nekaj zelo malega, drugemu pa na nekaj zelo velikega (zagotovimo, da bo
	// na začetku računalnik pravilno začel)
	
	public static void main(String[] args) {
		System.out.print("Stanje je ");
		System.out.println(Plosca.getStanje());
		LinkedHashMap<Koordinati, Integer> dict = new LinkedHashMap<Koordinati, Integer>();
		Koordinati ena = new Koordinati(1, 1);
		Koordinati dva = new Koordinati(2, 2);
		Koordinati tri = new Koordinati(3, 3);
		dict.put(ena, 1);
		dict.put(dva, 2);
		dict.put(tri, 3);
		System.out.println(dict.values());
		System.out.println(dict.keySet());		
		for (int i = 0; i < dict.size(); i++) {
			System.out.println(dict.values().toArray()[i]);
			System.out.println(dict.keySet().toArray()[i]);
		}
	}
	
	public void nastaviZacetnoVrednost () {
		this.statusEna = -10000;
		this.statusDva = 10000;
	}
	
	
	public static ArrayList<Koordinati> toList (LinkedHashMap<Koordinati, Color> hmp) {
		ArrayList<Koordinati> koordinate = new ArrayList<Koordinati>(hmp.keySet());
		return koordinate;
	}
	
	
	public static LinkedHashMap<Koordinati, Integer> preklopiStanje (LinkedHashMap<Koordinati, Color> staroStanje) {
		LinkedHashMap<Koordinati, Integer> novoStanje = new LinkedHashMap<Koordinati, Integer>();
		ArrayList koordinate = toList(staroStanje);
		for (int i = 0; i < koordinate.size(); i++) {
			if (staroStanje.get(i) == Color.WHITE) {
				novoStanje.put((Koordinati) koordinate.get(i), 0);
			} else if (staroStanje.get(i) == barvaEna) {
				novoStanje.put((Koordinati) koordinate.get(i), 1);
			} else if (staroStanje.get(i) == barvaDva) {
				novoStanje.put((Koordinati) koordinate.get(i), 2);
			}
		}
		return novoStanje;
	}
	
	public void zamenjajStanje (LinkedHashMap<Koordinati, Color> staroStanje) {
		this.stanje = preklopiStanje(staroStanje);
	}
		
	public LinkedHashMap<Koordinati, Integer> vseMoznosti(LinkedHashMap<Koordinati, Integer> stanje) {
		ArrayList<Koordinati> koordinate = new ArrayList<Koordinati>(stanje.keySet());
		LinkedHashMap<Koordinati, Integer> moznosti = new LinkedHashMap<Koordinati, Integer>();
		
		for (int i = 0; i < koordinate.size(); i++) {
			//samo za preglednost
			Koordinati trenutnaKoordinata = koordinate.get(i);
			int trenutnaVrednost = stanje.get(i);
			if (trenutnaVrednost == 0) {
				moznosti.put(trenutnaKoordinata, trenutnaVrednost);
			}
		}
		
		return moznosti;
	}
	
	//public ArrayList<Integer> sosediMatrika (int x, int y) {
		//ArrayList<Integer> sosedi = new ArrayList<Integer>();
		//int velikost = Plosca.getVelikost();
		//ArrayList<ArrayList<Integer>> matrika = getMatrika();
		//sosedi.add(matrika.get(y - 1).get(x-1));
		//sosedi.add(matrika.get(y - 1).get(x));
		//sosedi.add(matrika.get(y - 1).get(x+1));
		//sosedi.add(matrika.get(y).get(x-1));
		//sosedi.add(matrika.get(y).get(x+1));
		//sosedi.add(matrika.get(y+1).get(x-1));
		//sosedi.add(matrika.get(y+1).get(x));
		//sosedi.add(matrika.get(y+1).get(x+1));
		//while (sosedi.remove(null));
		//return sosedi;
	//}
	
	
	public void shortestPath (int igralec) {
		
	}
	
	public void optimalPosition (int igralec) {
		
	}
}
