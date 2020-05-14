package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import koordinati.Koordinati;

public class Plosca {

	
	private ArrayList<Koordinati> koordinate;
	private ArrayList<ArrayList<Koordinati>> plosca;
	private static LinkedHashMap<Koordinati, Color> stanje;
	private static int velikost;
	private Color zmagovalec;
	private static Color igralec1;
	private static Color igralec2;
	
	public Plosca(int velikost, Color igralec1, Color igralec2) {
		this.igralec1 = igralec1;
		this.igralec2 = igralec2;
		zmagovalec = Igra.PRAZNO;
		this.velikost = velikost;
		koordinate = new ArrayList<Koordinati>();
		plosca = new ArrayList<ArrayList<Koordinati>>();
		ArrayList<Koordinati> vrstica;
		for(int i = 0; i < velikost; i++) {
			for (int j = 0; j <velikost; j++) {
				Koordinati koordinati = new Koordinati(i,j);
				if ( j == 0 ) {
					vrstica = new ArrayList<Koordinati>();
					vrstica.add(koordinati);
					plosca.add(vrstica);
				}else {
					vrstica = plosca.get(i);
					vrstica.add(koordinati);
					plosca.set(i, vrstica);
				}
				koordinate.add(koordinati);
			}
		}
		
		stanje = new LinkedHashMap<Koordinati,Color>();
			for(Koordinati koordinati: koordinate) {
				stanje.put(koordinati, Igra.PRAZNO);
			}
	}
	
	public void setIgralca(Color igralec1, Color igralec2) {
		this.igralec1 = igralec1;
		this.igralec2 = igralec2;
	}
	
	public ArrayList<Koordinati> prazne() {
		ArrayList<Koordinati> prazne = new ArrayList<Koordinati>();
		for(Koordinati koordinati: koordinate) {
			if (stanje.get(koordinati) == Igra.PRAZNO) {
				prazne.add(koordinati);
			}
		}
		return prazne;
	}
	
	public void odigraj(Koordinati koordinati, Color barva) {
		stanje.replace(koordinati, barva);
	}
	
	public static LinkedHashMap<Koordinati, Color> getStanje(){
		return stanje;
	}
	
	public ArrayList<ArrayList<Koordinati>> getPlosca(){
		return plosca;
	}
	
	public Color getZmagovalec() {
		return zmagovalec;
	}
	
	public boolean konecIgre() {
		int i; int j;
		i = 0;
		for (j = 0; j < velikost; j++) {
			if (jeKoncnaPot(najdaljsaPot(igralec1,new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = igralec1;
				return true;
			}
		}
		j = 0;
		for (i = 0; i < velikost; i++) {
			if (jeKoncnaPot(najdaljsaPot(igralec2,new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = igralec2;
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Koordinati> najdaljsaPot(Color barva, ArrayList<Koordinati> pot, int i, int j) {
		ArrayList<Koordinati> osnovnaPot;
		
		Koordinati koordinati = koordinati(i,j);
		pot.add(koordinati);
		for ( Koordinati sosednji :sosednje(i,j)) {
			if (stanje.get(sosednji).equals(barva) & !pot.contains(sosednji)) {
				osnovnaPot = pot;
				pot = najdaljsaPot(barva,pot,sosednji.getX(),sosednji.getY());
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
		boolean rdeca = prva.getX() == 0 & zadnja.getX() == velikost - 1 & stanje.get(prva) == igralec1;
		boolean modra = prva.getY() == 0 & zadnja.getY() == velikost - 1 & stanje.get(prva) == igralec2;
		return rdeca | modra;
	}
	
	public Koordinati koordinati(int i, int j) {
		if (i < 0 | j < 0 | i >= velikost | j >= velikost) {
			return null;
		} else {
			return plosca.get(i).get(j);
		}
		
	}
	
	public ArrayList<Koordinati> sosednje(int i, int j){
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
}
