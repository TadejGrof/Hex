package inteligenca;
import logika.*;

import java.awt.Color;
import java.awt.List;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

import koordinati.Koordinati;

public class Minimax {
	
	private LinkedHashMap<Koordinati, Integer> stanje;
	private int statusEna;
	private int statusDva;
	private Color barvaEna;
	private Color barvaDva;
	private ArrayList<Koordinati> pot;
	
	// prvi igralec bo imel cilj čimbolj povečati vrednost, drugi pa čimbolj zmanjšati, torej
	// nastavimo prvemu na nekaj zelo malega, drugemu pa na nekaj zelo velikega (zagotovimo, da bo
	// na začetku računalnik pravilno začel)
	
	public void nastaviZacetnoVrednost () {
		this.statusEna = -10000;
		this.statusDva = 10000;
	}
	
	public void nastaviBarve () {
		this.barvaEna = Igra.getIgralecBarva(1);
		this.barvaDva = Igra.getIgralecBarva(2);
	}
	
	public ArrayList<Koordinati> toList (LinkedHashMap<Koordinati, Color> hmp) {
		ArrayList<Koordinati> koordinate = new ArrayList<Koordinati>(hmp.keySet());
		return koordinate;
	}
	
	
	public LinkedHashMap<Koordinati, Integer> preklopiStanje (LinkedHashMap<Koordinati, Color> staroStanje) {
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
	
	public ArrayList<ArrayList<Integer>> getMatrika () {
		int velikostMape = Plosca.getVelikost();
		LinkedHashMap<Koordinati, Integer> stanje = preklopiStanje(Plosca.getStanje());
		Collection<Integer> mapa = stanje.values();
		ArrayList<ArrayList<Integer>> matrika = new ArrayList<ArrayList<Integer>>();
		
		for (int i = 0; i <= velikostMape * velikostMape; i++) {
			ArrayList<Integer> vrstica = new ArrayList<Integer>(); 
			if (i % velikostMape == 0) {
				matrika.add(vrstica);
				vrstica = new ArrayList<Integer>();
			}
			else {
				vrstica.add(stanje.get(i));
			}
		}
		return matrika;
	}
	
	public ArrayList<Integer> sosediMatrika (int x, int y) {
		ArrayList<Integer> sosedi = new ArrayList<Integer>();
		int velikost = Plosca.getVelikost();
		ArrayList<ArrayList<Integer>> matrika = getMatrika();
		sosedi.add(matrika.get(y - 1).get(x-1));
		sosedi.add(matrika.get(y - 1).get(x));
		sosedi.add(matrika.get(y - 1).get(x+1));
		sosedi.add(matrika.get(y).get(x-1));
		sosedi.add(matrika.get(y).get(x+1));
		sosedi.add(matrika.get(y+1).get(x-1));
		sosedi.add(matrika.get(y+1).get(x));
		sosedi.add(matrika.get(y+1).get(x+1));
		while (sosedi.remove(null));
		return sosedi;
	}
	
	//popravi findPath
	public void findPath (int igralec, int x, int y) {
		ArrayList<Koordinati> pot = this.pot;
		ArrayList<ArrayList<Integer>> matrika = getMatrika();
		int velikost = Plosca.getVelikost();
		Koordinati začetnaTočka = new Koordinati(x, y);
		pot.add(začetnaTočka);
		while (x < velikost-1 && y < velikost-1) {
			Koordinati točka = new Koordinati(x, y);
			if ((x < velikost - 1 || y < velikost - 1) && !pot.contains(točka)) {
				pot.add(točka);
			} else {
				findPath (igralec, x - 1, y - 1);
				findPath (igralec, x, y - 1);
				findPath (igralec, x+1, y-1);
				findPath (igralec, x-1, y);
				findPath (igralec, x+1, y);
				findPath (igralec, x-1, y+1);
				findPath (igralec, x, y+1);
				findPath (igralec, x+1, y+1);
			}
		}
	}
	
	public LinkedHashMap<ArrayList<Koordinati>, Integer> dolzineInPoti (ArrayList<Koordinati> pot) {
		LinkedHashMap<ArrayList<Koordinati>, Integer> slovar = new LinkedHashMap<ArrayList<Koordinati>, Integer>();
		
	}
	
	public void shortestPath (int igralec) {
		
	}
	
	public void optimalPosition (int igralec) {
		
	}
}
