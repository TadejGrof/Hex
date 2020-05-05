package logika;

import java.util.ArrayList;
import java.util.HashMap;

import koordinati.Koordinati;

public class Plosca {
	private final int PRAZNO = 0;
	
	private ArrayList<Koordinati> koordinate;
	private ArrayList<ArrayList<Koordinati>> plosca;
	private HashMap<Koordinati, Integer> stanje;
	private int velikost;
	private int zmagovalec;
	
	public Plosca(int velikost) {
		zmagovalec = PRAZNO;
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
		
		stanje = new HashMap<Koordinati,Integer>();
			for(Koordinati koordinati: koordinate) {
				stanje.put(koordinati, PRAZNO);
			}
	}
	
	public ArrayList<Koordinati> prazne() {
		ArrayList<Koordinati> prazne = new ArrayList<Koordinati>();
		for(Koordinati koordinati: koordinate) {
			if (stanje.get(koordinati) == PRAZNO) {
				prazne.add(koordinati);
			}
		}
		return prazne;
	}
	
	public void odigraj(Koordinati koordinati, int barva) {
		stanje.replace(koordinati, barva);
	}
	
	public HashMap<Koordinati, Integer> getStanje(){
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
			if (jeKoncnaPot(najdaljsaPot(Igra.RDECA,new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = Igra.RDECA;
				return true;
			}
		}
		j = 0;
		for (i = 0; i < velikost; i++) {
			if (jeKoncnaPot(najdaljsaPot(Igra.MODRA,new ArrayList<Koordinati>(), i, j))) {
				zmagovalec = Igra.MODRA;
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Koordinati> najdaljsaPot(int barva, ArrayList<Koordinati> pot, int i, int j) {
		ArrayList<Koordinati> osnovnaPot;
		
		Koordinati koordinati = koordinati(i,j);
		pot.add(koordinati);
		for ( Koordinati sosednji :sosednje(i,j)) {
			if (stanje.get(sosednji) == barva & !pot.contains(sosednji)) {
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
		boolean rdeca = prva.getX() == 0 & zadnja.getX() == velikost - 1 & stanje.get(prva) == Igra.RDECA;
		boolean modra = prva.getY() == 0 & zadnja.getY() == velikost - 1 & stanje.get(prva) == Igra.MODRA;
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
}
