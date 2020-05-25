package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import splosno.Koordinati;

public class Plosca extends ArrayList<ArrayList<Integer>> {
	private static final long serialVersionUID = 1L;
	
	public static final int PRAZNO = 0;
	public static final int IGRALEC1 = 1;
	public static final int IGRALEC2 = 2;
	
	public static ArrayList<Koordinati> koordinate;
	private static ArrayList<ArrayList<Koordinati>> plosca;
	private static LinkedHashMap<Koordinati, Color> stanje;
	private static int velikost;
	private int zmagovalec;
	
	public Plosca(int velikost) {
		this.velikost = velikost;
		int i; int j;
		for(i = 0; i < velikost; i++) {
			ArrayList<Integer> vrstica = new ArrayList<Integer>();
			for(j = 0; j < velikost; j++) {
				vrstica.add(0);
			}
			add(vrstica);
		}
	}
	
	public ArrayList<Koordinati> prazne() {
		ArrayList<Koordinati> prazne = new ArrayList<Koordinati>();
		int i; int j;
		System.out.println("delamPrazne");
		for(i = 0; i < velikost; i++) {
			for(j = 0; j < velikost; j++) {
				int vrednost = this.get(i).get(j);
				System.out.println(vrednost);
				if (vrednost == PRAZNO) {
					prazne.add(new Koordinati(i,j));
				}
			}
		}
		return prazne;
	}
	
	public int get(Koordinati koordinati) {
		return get(koordinati.getX()).get(koordinati.getY());
	}
	
	public void odigraj(Koordinati koordinati, int igralec) {
		ArrayList<Integer> vrstica = get(koordinati.getX()); 
		vrstica.set(koordinati.getY(), igralec);
	}
	
	public static LinkedHashMap<Koordinati, Color> getStanje(){
		return stanje;
	}
	
	public ArrayList<ArrayList<Koordinati>> getPlosca(){
		return plosca;
	}
	
	public int getZmagovalec() {
		return zmagovalec;
	}
	
	public boolean konecIgre() {
		int i; int j;
		i = 0;
		for (j = 0; j < velikost; j++) {
			if (jeKoncnaPot(najdaljsaPot(IGRALEC1, new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = IGRALEC1;
				return true;
			}
		}
		j = 0;
		for (i = 0; i < velikost; i++) {
			if (jeKoncnaPot(najdaljsaPot(IGRALEC2, new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = IGRALEC2;
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Koordinati> najdaljsaPot(int igralec, ArrayList<Koordinati> pot, int i, int j) {
		ArrayList<Koordinati> osnovnaPot;
		
		Koordinati koordinati = koordinati(i,j);
		pot.add(koordinati);
		for ( Koordinati sosednji :sosednje(i,j)) {
			if (get(sosednji) == igralec & !pot.contains(sosednji)) {
				osnovnaPot = pot;
				pot = najdaljsaPot(igralec,pot,sosednji.getX(),sosednji.getY());
				if (jeKoncnaPot(pot)) {
					return pot;
				} else {
					pot = osnovnaPot;
				}
			}
		}
		return pot;
		
		
		
	}

	public boolean jeKoncnaPot(ArrayList<Koordinati> pot) {
		Koordinati prva = pot.get(0);
		Koordinati zadnja = pot.get(pot.size() - 1);
		boolean rdeca = prva.getX() == 0 & zadnja.getX() == velikost - 1 & get(prva) == IGRALEC1;
		boolean modra = prva.getY() == 0 & zadnja.getY() == velikost - 1 & get(prva) == IGRALEC2;
		if (rdeca | modra) {
			System.out.println("zmagovalna Pot:");
			System.out.println(pot);
		}
		return (rdeca | modra);
	}
	
	public static Koordinati koordinati(int i, int j) {
		if (i < 0 | j < 0 | i >= velikost | j >= velikost) {
			return null;
		} else {
			return new Koordinati(i,j);
		}
	}
	
	public static ArrayList<Koordinati> sosednje(int i, int j){
		ArrayList<Koordinati> sosednje = new ArrayList<Koordinati>();
		sosednje.add(koordinati(i - 1, j - 1));
		sosednje.add(koordinati(i - 1, j));
		sosednje.add(koordinati(i, j - 1));
		sosednje.add(koordinati(i , j + 1));
		sosednje.add(koordinati(i + 1, j));
		sosednje.add(koordinati(i + 1, j + 1));
		while (sosednje.remove(null));
		return sosednje;
	}
	
	public static int getVelikost () {
		return velikost;
	}
	
	public int[][] getMatrika(){
		int[][] matrika = new int[velikost][velikost];
		int[] vrstica = new int[velikost];
		
		LinkedHashMap<Koordinati, Color> stanje = getStanje();
		ArrayList<Integer> stanjeList = new ArrayList<Integer>();
		
		// kako dobim tu ven barvi za oba igralca, če delam brez funkcijo brez argumenta igra?
		for (Color barva : stanje.values()) {
			if (barva == igralec1) {
				stanjeList.add(1);
			} else if (barva == igralec2) {
				stanjeList.add(2);
			} else {
				stanjeList.add(0);
			}
		}
		
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				vrstica[j] = stanjeList.get(velikost * i + j);
			}
			matrika[i] = vrstica;
			vrstica = new int[velikost];
		}
		
		return matrika;
	}
}
