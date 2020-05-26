package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import splosno.Koordinati;

public class Plosca extends ArrayList<ArrayList<Integer>> {
	private static final long serialVersionUID = 1L;
	
	public static final int PRAZNO = 0;
	public static final int IGRALEC1 = 1;
	public static final int IGRALEC2 = 2;
	
	
	private final Hex SPODNJIROB = new Hex(-1,0);
	private final Hex DESNIROB = new Hex(-2,0);
	private final Hex ZGORNJIROB = new Hex(-3,0);
	private final Hex LEVIROB = new Hex(-4,0);
	
	private static ArrayList<ArrayList<Koordinati>> plosca;
	private static LinkedHashMap<Koordinati, Color> stanje;
	private static int velikost;
	private int zmagovalec;
	
	
	private SeznamZaIskanje seznamZaIskanje;
	
	public Plosca(int velikost) {
		this.velikost = velikost;
		seznamZaIskanje = new SeznamZaIskanje();
		int i; int j;
		for(i = 0; i < velikost; i++) {
			ArrayList<Integer> vrstica = new ArrayList<Integer>();
			for(j = 0; j < velikost; j++) {
				vrstica.add(0);
			}
			add(vrstica);
		}
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
		try {
			return get(koordinati.getY()).get(koordinati.getX());
		} catch (Exception e){
			return -1;
		}
	}
	
	public int getValue(Koordinati koordinati) {
		if (koordinati == ZGORNJIROB) return 1;
		if (koordinati == SPODNJIROB) return 1;
		if (koordinati == DESNIROB) return 2;
		if (koordinati == LEVIROB) return 2;
		try {
			return get(koordinati.getY()).get(koordinati.getX());
		} catch (Exception e){
			return -1;
		}
	}
	
	public void odigraj(Koordinati koordinati, int igralec) {
		ArrayList<Integer> vrstica = get(koordinati.getX()); 
		vrstica.set(koordinati.getY(), igralec);
	}
	
	
	public NajkrajsaPot najkrajsaPot(int igralec) {
		if (igralec == IGRALEC1) {
			return najkrajsaPot(SPODNJIROB,ZGORNJIROB,IGRALEC1);
		} else if(igralec == IGRALEC2) {
			return najkrajsaPot(LEVIROB,DESNIROB,IGRALEC2);
		}
		return null;
	}
	
	
	public NajkrajsaPot najkrajsaPot(Hex zacetek,Hex konec, int igralec) {
		Hex trenutniHex;
		seznamZaIskanje.refresh();
		zacetek.teza = 0;
		zacetek.potDo.add(zacetek);
		ArrayList<Hex> trenutniSeznam = new ArrayList<Hex>(seznamZaIskanje);
		trenutniHex = zacetek;
		while(true) {
			trenutniSeznam.remove(trenutniHex);
			ArrayList<Hex> nepregledaniSosedje = filtrirajNepregledane(trenutniSeznam, trenutniHex.sosedi());
			ArrayList<Hex> veljavniSosedje = filtrirajVeljavne(nepregledaniSosedje,igralec);
			for(Hex sosed: veljavniSosedje) {
				int teza;
				if(getValue(sosed) == igralec) {
					teza = 0;
				} else {
					teza = 1;
				}
				int novaTeza = trenutniHex.teza + teza;
				if(novaTeza < sosed.teza) {
					sosed.teza = novaTeza;
					sosed.potDo = new NajkrajsaPot(trenutniHex.potDo);
					sosed.potDo.add(sosed);
				}
			}
			if(trenutniSeznam.size() == 0) {
				return getNajkrajsaPot(igralec);
			} else {
				trenutniHex = vrniNajmanjsiHex(trenutniSeznam);
			}
		}
	}
	
	private Hex vrniNajmanjsiHex(ArrayList<Hex> seznam) {
		Hex najmanjsi = null;
		for (Hex hex:seznam) {
			if (najmanjsi == null) najmanjsi = hex;
			else if(hex.teza < najmanjsi.teza) najmanjsi = hex;
		}
		return najmanjsi;
	}
	
	private NajkrajsaPot getNajkrajsaPot(int igralec) {
		if(igralec == IGRALEC1) {
			return ZGORNJIROB.potDo;
		} else if (igralec == IGRALEC2) {
			return DESNIROB.potDo;
		} return null;
	}
	
	private ArrayList<Hex> filtrirajVeljavne(ArrayList<Hex> sosedje, int igralec){
		ArrayList<Hex> tocke = new ArrayList<Hex>();
		for(Hex hex: sosedje) {
			int value = getValue(hex);
			if (value == 0 | value == igralec) tocke.add(hex);
		}
		return tocke;
	}
	
	private ArrayList<Hex> filtrirajNepregledane(ArrayList<Hex> seznam, ArrayList<Hex> sosedje){
		ArrayList<Hex> tocke = new ArrayList<Hex>();
		for(Hex hex: sosedje) {
			if (seznam.contains(hex)) tocke.add(hex);
		}
		return tocke;
	}
	
	public boolean konecIgre() {
		return najkrajsaPot(IGRALEC1).jeKoncna() | najkrajsaPot(IGRALEC2).jeKoncna();
	}
	
	//vrne ustrezni koordinati za podani vrednsot i in j oziroma ustrezen rob plosce ob 
	//negativnih oziroma prevelikih vrednostih.
	public Koordinati koordinati(int i, int j) {
		if(i < 0) {
			return LEVIROB;
		} else if(j < 0) {
			return SPODNJIROB;
		} else if( i >= velikost) {
			return DESNIROB;
		} else if( j >= velikost) {
			return ZGORNJIROB;
		}else{
			return new Koordinati(i,j);
		}
	}
	
	// za podano koordinato poda njene sosede
	public ArrayList<Koordinati> sosednje(Koordinati koordinati){
		ArrayList<Koordinati> sosednje = new ArrayList<Koordinati>();
		if(koordinati == SPODNJIROB) {
			return vrsta(0);
		} else if(koordinati == ZGORNJIROB) {
			return vrsta(velikost - 1);
		} else if(koordinati == DESNIROB) {
			return stolpec(velikost - 1);
		} else if(koordinati == LEVIROB) {
			return stolpec(0);
		} else {
			int i = koordinati.getX();
			int j = koordinati.getY();
			sosednje.add(koordinati(i - 1, j - 1));
			sosednje.add(koordinati(i - 1, j));
			sosednje.add(koordinati(i, j - 1));
			sosednje.add(koordinati(i , j + 1));
			sosednje.add(koordinati(i + 1, j));
			sosednje.add(koordinati(i + 1, j + 1));
			return sosednje;
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
	
	public int[][] getMatrika(){
		int[][] matrika = new int[velikost][velikost];
		int[] vrstica = new int[velikost];
		
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				vrstica[j] = get(i).get(j);
			}
			matrika[i] = vrstica;
			vrstica = new int[velikost];
		}
		
		return matrika;
	}
	
	
	// vrne seznam koordinata poljubne vrste;
	public ArrayList<Koordinati> vrsta(int y){
		ArrayList<Koordinati> vrsta = new ArrayList<Koordinati>();
		for (int i = 0; i < velikost; i++) {
			vrsta.add(new Koordinati(i,y));
		}
		return vrsta;
	}
	
	// vrne seznam koordinata poljubnega stolpa;
	public ArrayList<Koordinati> stolpec(int x){
		ArrayList<Koordinati> stolpec = new ArrayList<Koordinati>();
		for (int i = 0; i < velikost; i++) {
			stolpec.add(new Koordinati(x,i));
		}
		return stolpec;
	}
	
	public class NajkrajsaPot extends ArrayList<Hex>{
		private static final long serialVersionUID = 1L;
		
		public NajkrajsaPot(NajkrajsaPot pot) {
			super(pot);
		}
		
		public NajkrajsaPot() {
			super();
		}
		
		public int steviloPraznih() {
			int prazne = 0;
			for(Koordinati t:this) {
				if(getValue(t) == 0) {
					prazne++;
				}
			}
			return prazne;
		}
		
		public boolean jeKoncna() {
			for (Koordinati t: this) {
				if(getValue(t) == 0) return false;
			}
			return true;
		}
	}
	
	
	private class SeznamZaIskanje extends ArrayList<Hex>{
		private static final long serialVersionUID = 1L;
		
		public SeznamZaIskanje() {
			int i;int j;
			for(i = 0; i < velikost; i++) {
				for(j = 0; j < velikost; j++) {
					add(new Hex(i,j));
				}
			}
			add(SPODNJIROB);
			add(ZGORNJIROB);
			add(DESNIROB);
			add(LEVIROB);
			
		}
		public void refresh() {
			for(Hex hex: this) {
				hex.teza = 100000;
				hex.potDo = new NajkrajsaPot();
			}
		}
		
		public Hex get(Koordinati t) {
			for (Hex hex : this) {
				if(hex.getX() == t.getX() & hex.getY() == t.getY()) return hex;
			}
			return null;
		}
	}
	
	private class Hex extends Koordinati{
		
		public Hex(int x, int y) {
			super(x, y);
			// TODO Auto-generated constructor stub
		}
		public int teza = 100000;
		public NajkrajsaPot potDo = new NajkrajsaPot();
		
		public ArrayList<Hex> sosedi(){
			ArrayList<Hex> sosedi = new ArrayList<Hex>();
			ArrayList<Koordinati> sosednje = sosednje(this);
			for(Koordinati t: sosednje) {
				sosedi.add(seznamZaIskanje.get(t));
			}
			return sosedi;
		}
		
	}
}
