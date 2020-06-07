package logika;

import java.util.ArrayList;
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
	
	public int velikost;
	
	private int zmagovalec;
	
	public NajkrajsaPot najkrajsaPotIgralec1;
	public NajkrajsaPot najkrajsaPotIgralec2;
	
	private SeznamZaIskanje seznamZaIskanje;
	
	
	public Plosca(int velikost) {
		this.velikost = velikost;
		// seznam s pomočjo katerega iščemo najkrajsi poti za oba igralca
		seznamZaIskanje = new SeznamZaIskanje();
		int i; int j;
		// ustvari seznam seznamov s katerim so predstavljene vrstice in stolpci
		// ter vrednosti na posameznem polju
		for(i = 0; i < velikost; i++) {
			ArrayList<Integer> vrstica = new ArrayList<Integer>();
			for(j = 0; j < velikost; j++) {
				vrstica.add(0);
			}
			add(vrstica);
		}
	}
	
	// vrne zmagovalca, katerega najkrajsa pot je koncna
	public int getZmagovalec() {
		return zmagovalec;
	}
	
	// preveri če je dana koordinata sredinska
	public boolean jeSredinska(Koordinati t) {
		ArrayList<Koordinati> sredinske = getSredinske();
		return sredinske.contains(t);
	}
	
	
	// vrne sredinske koordinate glede na velikost plosce
	// vecja je plosca vec je sredinskih
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
	
	// izracuna razdaljo med danima koordinatama,
	// na podlagi polj, ki jih je potrebno prepotovati od ene do druge.
	public int razdalja(Koordinati t1, Koordinati t2) {
		int x1 = t1.getX(); int x2 = t2.getX();
		int y1 = t1.getY(); int y2 = t2.getY();
		if(x1 == x2 & y1 == y2) return 0;
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
	
	// pogleda seznam seznamov ki predstavlja plosco ter vrne vse katerih vrenost
	// je enaka 0, kar pomeni, da še ni bila igrana
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

	
	// vrne vrednost na polju, ki ga predstavlja podana koordinata,
	// vrednosti imajo prav tako robovi. S pomočjo tega lahko izračunamo najkrajšo pot
	// od enega roba do drugega
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
	
	
	// na podano mesto odigra vrednost igra, ter nastavi novi najkrajsi poti za 
	// oba igralca
	public void odigraj(Koordinati koordinati, int igralec) {
		ArrayList<Integer> vrstica = get(koordinati.getY()); 
		vrstica.set(koordinati.getX(), igralec);
		najkrajsaPotIgralec1 = najkrajsaPot(IGRALEC1);
		najkrajsaPotIgralec2 = najkrajsaPot(IGRALEC2);
	}
	
	// za podanega igralca izračuna ter vrne najkrajšo pot od enega do drugega roba
	// vrne pot, ki bi bila v primeru, da je cela zapolnjena iz strani igralca, zmagovalna
	// in pri tem najkrajša možna med vsemi glede na trenutno stanje igre
	// možnih je seveda več podobnih rešitev ( še posebej na začetku )
	// ...funckija vrne prvo glede na izračun
	public NajkrajsaPot najkrajsaPot(int igralec) {
		Inteligenca.steviloRacunanihPoti ++;
		if (igralec == IGRALEC1) {
			return najkrajsaPot(SPODNJIROB,ZGORNJIROB,IGRALEC1);
		} else if(igralec == IGRALEC2) {
			return najkrajsaPot(LEVIROB,DESNIROB,IGRALEC2);
		}
		return null;
	}
	
	
	// poisce najkrajso pot od podanega zacetka pa do konca za podanega igralca
	public NajkrajsaPot najkrajsaPot(Hex zacetek,Hex konec, int igralec) {
		Hex trenutniHex;
		// pripravimo seznam za novo iskanje
		seznamZaIskanje.refresh();
		zacetek.teza = 0;
		// zacetni poti dodamo prvo zacetno tocko
		zacetek.potDo.add(zacetek);
		// ustvarimo kopijo seznama, da vemo katere smo že gledali
		ArrayList<Hex> trenutniSeznam = new ArrayList<Hex>(seznamZaIskanje);
		trenutniHex = zacetek;
		while(true) {
			// prvega pregledanega odstranimo iz seznama
			trenutniSeznam.remove(trenutniHex);
			// filtriramo nepregledane
			ArrayList<Hex> nepregledaniSosedje = filtrirajNepregledane(trenutniSeznam, trenutniHex.sosedi());
			// filtriramo veljavne...iscemo torej le v smeri, ki je prazna ali še ni igrana
			ArrayList<Hex> veljavniSosedje = filtrirajVeljavne(nepregledaniSosedje,igralec);
			for(Hex sosed: veljavniSosedje) {
				int teza;
				// boljše je če je polje že igrano zato v tem primeri teže spremenimo
				if(getValue(sosed) == igralec) {
					teza = 0;
				} else {
					// v primeru ko je polje prazno prištejemo težo ena
					teza = 1;
				}
				// nastavimo novo težo
				int novaTeza = trenutniHex.teza + teza;
				if(novaTeza < sosed.teza) {
					// če je teža ( napor ) do soseda manjši kot teža od prejšnjih poti
					// do tega soseda, nastavi za hex, ki predstavja tega soseda novo pot
					// ter novo težo
					// na koncu tako dobimo za vsak hex najkrajšo pot in če izberemo
					// zadnjega ( ustrezen rob ) dobimo željeno pot 
					sosed.teza = novaTeza;
					sosed.potDo = new NajkrajsaPot(trenutniHex.potDo);
					sosed.potDo.add(sosed);
				}
			}
			if(trenutniSeznam.size() == 0) {
				// če smo pregledali vse vrnemo najkrajšo pot do končnega roba
				return getNajkrajsaPot(igralec);
			} else {
				// drugače nastavimo trenutni hex na najustreznejšega ter nadaljujemo postopek
				trenutniHex = vrniNajmanjsiHex(trenutniSeznam);
			}
		}
	}
	
	// vrne Hex, ki ima najmanjšo vrednost med vsemi iz seznama
	private Hex vrniNajmanjsiHex(ArrayList<Hex> seznam) {
		Hex najmanjsi = null;
		for (Hex hex:seznam) {
			if (najmanjsi == null) najmanjsi = hex;
			else if(hex.teza < najmanjsi.teza) najmanjsi = hex;
		}
		return najmanjsi;
	}
	
	// predvidevamo da smo začeli v spodnjem kotu za igralca1 oziroma levem 
	// za igralca2. Funkcija tako pogleda za hex ki predstavlja zgornji oziroma desni
	// rob ter vrne shranjeno najkrašo pot do tega roba od začetka
	private NajkrajsaPot getNajkrajsaPot(int igralec) {
		if(igralec == IGRALEC1) {
			return ZGORNJIROB.potDo;
		} else if (igralec == IGRALEC2) {
			return DESNIROB.potDo;
		} return null;
	}
	
	// iz seznama sosedov vrne le tiste ki so ali prazni ali igrani iz igralčeve strani
	// omogoči nadaljevanje iskanja najkrajše poti na veljavnih mestih
	private ArrayList<Hex> filtrirajVeljavne(ArrayList<Hex> sosedje, int igralec){
		ArrayList<Hex> tocke = new ArrayList<Hex>();
		for(Hex hex: sosedje) {
			int value = getValue(hex);
			if (value == 0 | value == igralec) tocke.add(hex);
		}
		return tocke;
	}
	
	// ko imamo enkrat seznam veljavnih, izločimo tiste, ki smo jih že pregledali
	private ArrayList<Hex> filtrirajNepregledane(ArrayList<Hex> seznam, ArrayList<Hex> sosedje){
		ArrayList<Hex> tocke = new ArrayList<Hex>();
		for(Hex hex: sosedje) {
			if (seznam.contains(hex)) tocke.add(hex);
		}
		return tocke;
	}
	
	// preveri če je katera od najkrajsih poti koncna in tako vrne če 
	// je že konec igre ali še ne
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
	
	// vrne kopijo plosce
	// uporablja se pri kopiranju igre
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
	
	// preveri, če koordianti tvorita most
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
	
	// vrne seznam skupnih sosedov podanih koordnati
	public ArrayList<Koordinati> skupniSosedje(Koordinati t1, Koordinati t2){
		ArrayList<Koordinati> sosedje1 = sosednje(t1);
		ArrayList<Koordinati> sosedje2 = sosednje(t2);
		ArrayList<Koordinati> skupni = new ArrayList<Koordinati>();
		for ( Koordinati t: sosedje1) {
			if (sosedje2.contains(t)) skupni.add(t);
		}
		return skupni;
	}
	
	// povezava dveh igranih polj med katerima je eno polje prazno
	// obstajata dve taki možni povezavi:
	// - enojni (koordinati imata le enega skupnega soseda)
	// - dvojni (koordianti imata dva skupna soseda)
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
		
		// preveri če je most dvojni
		public boolean jeDvojni() {
			if (prazna2 == null | prazna1 == null) return false;
			return getValue(prazna1) == 0 && getValue(prazna2) == 0;
		}
		
		// preveri če gre pot pod mostom, kar pomeni da lahko pot
		// enostavno blokiramo z povezavo mosta
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
		
		// vrne vse prazne
		public ArrayList<Koordinati> prazne(){
			ArrayList<Koordinati> prazne = new ArrayList<Koordinati>();
			for (Hex hex:this) {
				int vrednost = getValue(hex);
				if(vrednost == 0) prazne.add(hex);
			}
			return prazne;
		}
		
		// preveri če pot vsebuje podani koordinati
		public boolean vsebuje(Koordinati t) {
			for (Hex hex: this) {
				if(hex.getX() == t.getX() && hex.getY() == t.getY()) return true;
			}
			return false;
		}
		
		// za koordinati t vrne razdaljo od najbižjega še ne igranega polja
		// uporablja se pri ocenjevanju koordinat pri inteligenci (vrednostKoordinate)
		public int razdaljaOdPrazne(Koordinati t) {
			int najmanjsaVrednost = Integer.MAX_VALUE;
			for (Hex hex:this) {
				int vrednost = getValue(hex);
				if(vrednost == 0) {
					int razdalja = razdalja(hex,t);
					if (razdalja < najmanjsaVrednost) najmanjsaVrednost = razdalja;
				}
			}
			return najmanjsaVrednost;
		}
		
		
		// za koordinato t vrne razdlajo do najbižjega polnega ( igranega ) polja 
		// uporablja se pri ocenjevanju koordinat pri inteligenci (vrednostKoordinate)
		public int razdaljaOdPolne(Koordinati t) {
			int najmanjsaVrednost = Integer.MAX_VALUE;
			int igralec = getValue(get(0));
			for (Hex hex:this) {
				int vrednost = getValue(hex);
				if(vrednost == igralec) {
					int razdalja = razdalja(hex,t);
					if (razdalja < najmanjsaVrednost) najmanjsaVrednost = razdalja;
				}
			}
			return najmanjsaVrednost;
		}
		
		// vrne število praznih ( še ne igranih ) polj v tej poti.
		public int steviloPraznih() {
			int prazne = 0;
			for(Koordinati t:this) {
				if(getValue(t) == 0) {
					prazne++;
				}
			}
			return prazne;
		}
		
		// vrne vse mostove, ki jih tvori ta pot
		public ArrayList<Most> mosti(){
			ArrayList<Most> mosti = new ArrayList<Most>();
			for(int i = 0; i < this.size(); i++) {
				if ( i < (this.size() - 2) && jeMost(get(i), get(i + 2))) {
					mosti.add(new Most(get(i),get(i+2)));
				}
			}
			return mosti;
		}
		
		
		// vrne število boljših (dvojnih) mostov
		public int steviloDvojnihMostov() {
			int stevilo = 0;
			for(Most most:mosti()) {
				if (most.jeDvojni()) stevilo ++;
			}
			return stevilo;
		}
		
		// vrne število mostov, ki jih tvori pot
		public int steviloMostov() {
			int mosti = 0;
			for(int i = 0; i < this.size(); i++) {
				if ( i < (this.size() - 2) && jeMost(get(i), get(i + 2))) mosti ++;
			}
			return mosti;
		}
		
		// preveri če je pot končna
		// torej ker predvidevamo, da vedno začetno v enem robu in končamo v drugem
		// je potrebno preveriti le če so vsa polja na poti zapolnjena z ustrezno
		// vrednostjo igralca (če je seveda teh polj več kot je velikost plošče)
		public boolean jeKoncna() {
			if (this.size() < velikost) return false;
			for (Koordinati t: this) {
				if(getValue(t) == 0) return false;
			}
			return true;
		}
	}
	
	// ustvari seznam hex-ov (razširjenih koordinat)
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
		
		// za vsak hex iz seznama ponastavi začetno vrednost in ustvari novo
		// najkrajso pot, ki je zaenkrat še prazna
		public void refresh() {
			for(Hex hex: this) {
				hex.teza = Integer.MAX_VALUE;
				hex.potDo = new NajkrajsaPot();
			}
		}
		
		// za podano kooordinato vrne pripadajoči hex iz seznama
		public Hex get(Koordinati t) {
			for (Hex hex : this) {
				if(hex.getX() == t.getX() & hex.getY() == t.getY()) return hex;
			}
			return null;
		}
	}
	
	
	// razširitev koordinat, ki nam omogoča vrednotenje posameznega mesta na plosci
	// in tako poiskati najkrajso pot od zacetnega do koncnega polja
	private class Hex extends Koordinati{
		
		public Hex(int x, int y) {
			super(x, y);
		}
		
		public int teza = Integer.MAX_VALUE;
		public NajkrajsaPot potDo = new NajkrajsaPot();
		
		
		// vrne sosede iz seznamaZaIskanje
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
