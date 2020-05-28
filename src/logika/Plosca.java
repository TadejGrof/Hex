package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import inteligenca.Inteligenca;
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
	public static LinkedHashMap<Koordinati, Color> stanje;
	public int velikost;
	private int zmagovalec;
	
	public NajkrajsaPot najkrajsaPotIgralec1;
	public NajkrajsaPot najkrajsaPotIgralec2;
	
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
	
	public Plosca(Plosca plosca) {
		super(plosca);
		this.velikost = plosca.velikost;
		seznamZaIskanje = new SeznamZaIskanje();
	}
	
	public static LinkedHashMap<Koordinati, Color> getStanje(){
//		LinkedHashMap<Koordinati, Color> stanje = new LinkedHashMap<Koordinati, Color>();
//
//		Color barvaEna = igra.getIgralecBarva(1);
//		Color barvaDva = igra.getIgralecBarva(2);
//		
//		for (ArrayList<Koordinati> vrstica : plosca) {
//			for (Koordinati koordinata : vrstica) {
//				
//			}
//		}
		return stanje;
	}
	
	public ArrayList<ArrayList<Koordinati>> getPlosca(){
		return plosca;
	}
	
	public int getZmagovalec() {
		return zmagovalec;
	}
	
	
	//samo za 11X11;
	public boolean jeSredinska(Koordinati t) {
		ArrayList<Koordinati> sredinske = getSredinske();
		return sredinske.contains(t);
	}
	
	public ArrayList<Koordinati> getSredinske(){
		ArrayList<Koordinati> sredinske = new ArrayList<Koordinati>();
		int sredina = velikost / 2;
		if (velikost < 4 ) {
			sredinske.add(new Koordinati(sredina,sredina));
		}
		if(velikost % 2 == 0) {
			sredinske.add(new Koordinati(sredina, sredina));
			sredinske.add(new Koordinati(sredina - 1, sredina));
			sredinske.add(new Koordinati(sredina, sredina - 1));
			sredinske.add(new Koordinati(sredina - 1, sredina - 1));
		} else {
			if (velikost < 8) {
				sredinske.add(new Koordinati(sredina,sredina));
			} else  {
				sredinske.add(new Koordinati(sredina, sredina));
				sredinske.add(new Koordinati(sredina - 1, sredina - 1));
				sredinske.add(new Koordinati(sredina, sredina - 1));
				sredinske.add(new Koordinati(sredina + 1, sredina - 1));
				sredinske.add(new Koordinati(sredina - 1, sredina + 1));
				sredinske.add(new Koordinati(sredina, sredina + 1));
				sredinske.add(new Koordinati(sredina + 1, sredina + 1));
				sredinske.add(new Koordinati(sredina, sredina + 1));
				sredinske.add(new Koordinati(sredina + 1, sredina + 1));
				sredinske.add(new Koordinati(sredina - 1, sredina));
				sredinske.add(new Koordinati(sredina + 1, sredina));
			}
			
		}
		return sredinske;
		
	}
	public int razdalja(Koordinati t1, Koordinati t2) {
		int x1 = t1.getX(); int x2 = t2.getX();
		int y1 = t1.getY(); int y2 = t2.getY();
		int razlikaX = Math.abs(x1-x2);
		int razlikaY = Math.abs(y1-y2);
		if (razlikaX <= 1 & razlikaY <= 1 ) return 1;
		else if(x1 >= x2 & y1 >= y2) {
			return razlikaY; 
		}else if(x1 <= x2 & y1 >= y2) {
			return razlikaX + razlikaY; 
		} else if(x1 >= x2 & y1 <= y2) {
			return razlikaX + razlikaY; 
		} else if(x1 <= x2 & y1 <= y2) {
			return razlikaY; 
		}
		return 0;
	}
	
	public ArrayList<Koordinati> prazne() {
		ArrayList<Koordinati> prazne = new ArrayList<Koordinati>();
		int i; int j;
		for(i = 0; i < velikost; i++) {
			for(j = 0; j < velikost; j++) {
				int vrednost = this.get(i).get(j);
				if (vrednost == PRAZNO) {
					prazne.add(new Koordinati(j,i));
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
		ArrayList<Integer> vrstica = get(koordinati.getY()); 
		vrstica.set(koordinati.getX(), igralec);
		najkrajsaPotIgralec1 = najkrajsaPot(IGRALEC1);
		najkrajsaPotIgralec2 = najkrajsaPot(IGRALEC2);
	}
	
	public NajkrajsaPot najkrajsaPot(int igralec) {
		Inteligenca.steviloRacunanihPoti ++;
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
		if(najkrajsaPotIgralec1 == null | najkrajsaPotIgralec2 == null) return false;
		if (najkrajsaPotIgralec1.jeKoncna()) {
			zmagovalec = IGRALEC1;
			return true;
		}
		if (najkrajsaPotIgralec2.jeKoncna()) {
			zmagovalec = IGRALEC2;
			return true;
		}
		return false;
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
	
	public int getVelikost () {
		return velikost;
	}
	
	// ta dela isto...ono pobrisi po zelji
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
	
	public Plosca kopirajPlosco() {
		int i; int j;
		Plosca kopija = new Plosca(velikost);
		kopija.najkrajsaPotIgralec1 = najkrajsaPotIgralec1;
		kopija.najkrajsaPotIgralec2 = najkrajsaPotIgralec2;
		for(i = 0; i < velikost; i++) {
			ArrayList<Integer> vrstica = kopija.get(i);
			for(j = 0; j < velikost; j++) {
				vrstica.set(j, get(i).get(j));
			}
			kopija.set(i, vrstica);
		}
		return kopija;
	}
	
	public ArrayList<Koordinati> vseKoordinate(){
		return null;
	}
	
	public boolean jeMost(Koordinati t1, Koordinati t2) {
		if (getValue(t1) == getValue(t2) && getValue(t1) != 0) {
			ArrayList<Koordinati> skupniSosedje = skupniSosedje(t1,t2);
			for ( Koordinati sosed : skupniSosedje) {
				if(getValue(sosed) == 0) return true;
			}
			return false;
		}
		return false;
	}
	
	public ArrayList<Koordinati> skupniSosedje(Koordinati t1, Koordinati t2){
		ArrayList<Koordinati> sosedje1 = sosednje(t1);
		ArrayList<Koordinati> sosedje2 = sosednje(t2);
		ArrayList<Koordinati> skupni = new ArrayList<Koordinati>();
		for ( Koordinati t: sosedje1) {
			if (sosedje2.contains(t)) skupni.add(t);
		}
		return skupni;
	}
	
	public class Most {
		Koordinati polna1;
		Koordinati polna2;
		Koordinati prazna1;
		Koordinati prazna2;
		
		public Most(Koordinati t1,Koordinati t2) {
			polna1 = t1;
			polna2 = t2;
			ArrayList<Koordinati> sosedje = skupniSosedje(t1,t2);
			if(sosedje.size() > 1){
				prazna1 = sosedje.get(0);
				prazna2 = sosedje.get(1);
			} else if (sosedje.size() == 1){
				prazna1 = sosedje.get(0);
				prazna2 = null;
			} else {
				prazna1 = null;
				prazna2 = null;
			}
				
		}
		
		public boolean jeDvojni() {
			if (prazna2 == null | prazna1 == null) return false;
			return get(prazna1) == 0 && get(prazna2) == 0;
		}
		
		public boolean greCezPot(NajkrajsaPot pot) {
			return pot.contains(prazna1) & pot.contains(prazna2); 
		}
			
	}
	
	public class NajkrajsaPot extends ArrayList<Hex>{
		private static final long serialVersionUID = 1L;
		
		public NajkrajsaPot(NajkrajsaPot pot) {
			super(pot);
		}
		
		public NajkrajsaPot() {
			super();
		}
		
		public boolean vsebuje(Koordinati t) {
			for (Hex hex: this) {
				if(hex.getX() == t.getX() && hex.getY() == t.getY()) return true;
			}
			return false;
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
		
		public ArrayList<Most> mosti(){
			ArrayList<Most> mosti = new ArrayList<Most>();
			for(int i = 0; i < this.size(); i++) {
				if ( i < (this.size() - 2) && jeMost(get(i), get(i + 2))) {
					mosti.add(new Most(get(i),get(i+2)));
				}
			}
			return mosti;
		}
		
		public int steviloMostov() {
			int mosti = 0;
			for(int i = 0; i < this.size(); i++) {
				if ( i < (this.size() - 2) && jeMost(get(i), get(i + 2))) mosti ++;
			}
			return mosti;
		}
		
		
		public boolean jeKoncna() {
			if (this.size() < velikost) return false;
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
					add(new Hex(j,i));
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
