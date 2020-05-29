package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import logika.Plosca.NajkrajsaPot;
import splosno.Koordinati;

public class Igra {
	public static final Color PRAZNO = Color.WHITE;
	public static final Color RDECA = Color.RED;
	public static final Color MODRA = Color.BLUE;
	
	public static final int IGRA = 0;
	public static final int ZMAGA1 = 1;
	public static final int ZMAGA2 = 2;
	
	public static int[][] mtrx;
	
	public int velikost;
	
	public Plosca plosca;
	public Igralec igralecNaPotezi;
	
	public Igralec igralec1;
	public Igralec igralec2;
	private ArrayList<Igralec> igralca;
	private Igralec zmagovalec;
	private boolean konec;
	public ArrayList<Poteza> poteze;
	
	public static void main(String[] args) {
		Igra igra = new Igra(11);
		igra.odigraj(new Koordinati(5,5));
		igra.odigraj(new Koordinati(6,7));
		System.out.println(igra.plosca.razdalja(new Koordinati(5,5), new Koordinati(5,5)));
		//System.out.println(igra.plosca.jeMost(new Koordinati(5,5),new Koordinati(6,7)));
	}
	
	public Igra() {
		konec = false;
		zmagovalec = null;
		velikost = 11;
		initialize();
	}
	 public Igra(int velikost) {
		 this.velikost = velikost;
		 initialize();
	 }
	 
	 public void nakljucnaPoteza(){
		Random random = new Random();
		ArrayList<Koordinati> veljavne = veljavnePoteze();
		odigraj(veljavne.get(random.nextInt(veljavne.size())));
	 }
	 
	 
	 private void initialize() {
		 poteze = new ArrayList<Poteza>();
		 igralca = new ArrayList<Igralec>();
		 
		 igralec1 = new Igralec("Igralec1", this, RDECA, Igralec.IGRALEC);
		 igralca.add(igralec1);
		 igralec2 = new Igralec("Igralec2", this, MODRA, Igralec.IGRALEC);
		 igralca.add(igralec2);
		 
		 plosca = new Plosca(velikost);
		 igralecNaPotezi = igralec1; 
	 }
	 
	 public Igralec nasprotnik(Igralec igralec) {
		 if(igralec == igralec1) {
			 return igralec2;
		 } else if( igralec == igralec2) {
			 return igralec1;
		 }
		 return null;
	 }
	 
	 public Integer getIgralecIndex(Igralec igralec) {
		 if(igralec.equals(igralec1)){
			 return 1;
		 } else if (igralec.equals(igralec2)) {
			 return 2;
		 }
		 return 0;
	 }
	 
	 public Color getIgralecBarva(int index) {
		 if (index == 1) {
			 return igralec1.getBarva();
		 } else if (index == 2) {
			 return igralec2.getBarva();
		 }
		 return PRAZNO;
	 }
	 
	 public void setIgralca(Igralec igralec1, Igralec igralec2) {
		 this.igralec1 = igralec1;
		 this.igralec2 = igralec2;
		 igralec1.setIgra(this);
		 igralec2.setIgra(this);
		 
		 igralecNaPotezi = igralec1;
		 igralca.clear();
		 igralca.add(igralec1);
		 igralca.add(igralec2);
	 }
	 
	 
	 public int[][] setIntMtrx () {
	 	return plosca.getMatrika();
	 }
	 
	 public static void printIntMtrx(int[][] mtrx) {
		 for (int i = 0; i < mtrx[0].length; i++) {
			 for (int j = 0; j < mtrx[0].length; j++) {
				 System.out.print(" " +mtrx[i][j] + " ");
			 }
			 System.out.println();
		 }
	 }
	 
	 public boolean odigraj(Koordinati koordinati) {
		 if(jeVeljavnaPoteza(koordinati)) {
			 plosca.odigraj(koordinati,getIgralecIndex(igralecNaPotezi));
			 poteze.add(new Poteza(igralecNaPotezi,koordinati));
			 naslednjiNaPotezi();
			 return true;
		 } 
		 return false;
	 }
	 
	 private void naslednjiNaPotezi() {
		 int index = igralca.indexOf(igralecNaPotezi);
		 if ( index == 0) {
			 igralecNaPotezi = igralca.get(1);
		 } else {
			 igralecNaPotezi = igralca.get(0);
		 }
	 }
	 
	 
	 private boolean jeVeljavnaPoteza(Koordinati koordinati) {
		 ArrayList<Koordinati> veljavne = veljavnePoteze();
		 return veljavne.contains(koordinati);
	 }
	 
	 public ArrayList<Koordinati> veljavnePoteze(){
		 return plosca.prazne();
	 }
	 
	 public ArrayList<ArrayList<Koordinati>> vrniKoordinate() {
		 return plosca.getPlosca();
	 }
	 
	 public NajkrajsaPot najkrajsaPot(Igralec igralec) {
		 if(igralec == igralec1) {
			 return plosca.najkrajsaPotIgralec1;
		 } else if(igralec == igralec2) {
			 return plosca.najkrajsaPotIgralec2;
		 }
		 return null;
	 }
	 
	 public boolean konecIgre() {
		return plosca.konecIgre();
	 }
	 
	 public int getStanje() {
		 if (konecIgre()) {
			 if(zmagovalecIgre() == igralec1) {
				 return ZMAGA1;
			 } else if(zmagovalecIgre() == igralec2) {
				 return ZMAGA2;
			 }
		 }
		 return IGRA;
	 }
	 
	 public Igralec zmagovalecIgre() {
		int zmagovalec = plosca.getZmagovalec();
		if (zmagovalec > 0) {
			return igralca.get(zmagovalec - 1);
		}
		return null;
	 }
	 
	 public Igralec getIgralec(Color barva) {
		 for(Igralec igralec: igralca) {
			 if (igralec.getBarva().equals(barva)) {
				 return igralec;
			 }
		 }
		 return null;
	 }
	 
	 
	 public Igra kopirajIgro () {
		 Igra kopija = new Igra();
		 
		 kopija.velikost = velikost;
		 kopija.plosca = plosca.kopirajPlosco();
		 kopija.setIgralca(igralec1, igralec2);
		 kopija.igralecNaPotezi = igralecNaPotezi;
		 
		 kopija.poteze = new ArrayList<Poteza>(poteze);
		 kopija.zmagovalec = zmagovalec;
		 kopija.konec = konec;
		 
		 return kopija;
	 }
	 
	 public Koordinati naključniKoordinati () {
		 int velikost = plosca.getVelikost();
		 
		 int x = 0;
		 int y = 0;
		 
		 Random naključnaIzbira = new Random();
		 y = naključnaIzbira.nextInt(velikost + 1);
		 x = naključnaIzbira.nextInt(velikost + 1);
		 
		 Koordinati naključniKoordinati = new Koordinati(x, y);
		 
		 return naključniKoordinati;
	 }

	 public ArrayList<Koordinati> poisciVsePoteze (int igralec) {
		 ArrayList<Koordinati> vsePoteze = new ArrayList<Koordinati>();
		 LinkedHashMap<Koordinati, Color> mapa = Plosca.getStanje();
		 Set<Koordinati> koordinate = mapa.keySet();
		 
		 Color barva = Color.WHITE;
		 
		 if (igralec == 1) {
			 barva = igralec1.getBarva();
		 } else if (igralec == 2) {
			 barva = igralec2.getBarva();
		 }
		 
		 for (Koordinati koordinata : koordinate) {
			 Color lokalnaBarva = mapa.get(koordinata);
			 
			 if (lokalnaBarva == barva) {
				 vsePoteze.add(koordinata);
			 }
		 }
		 return vsePoteze;
	 }
	 
	 public Koordinati začetnaKoordinata () {;
		 int x = Math.floorDiv(velikost, 2);
		 int y = Math.floorDiv(velikost, 2);
		 
		 Koordinati k = new Koordinati(x, y);
		 return k;
	 }
	 
	 
	 public List<Koordinati> urejeneMoznePoteze(){
		List<Koordinati> poteze = veljavnePoteze();
		Collections.sort(poteze,new SortKoordinati(this));
		if(poteze.size() > 18) {
			poteze = poteze.subList(0, 18);
		}
		return poteze;
	}
	 
	 
	 public int vrednostKoordinate(Koordinati t) {
			int vrednost = 0;
			NajkrajsaPot mojaPot = najkrajsaPot(igralecNaPotezi);
			NajkrajsaPot nasprotnikovaPot = najkrajsaPot(nasprotnik(igralecNaPotezi));
			int razdaljaOdMojePrazne = mojaPot.razdaljaOdPrazne(t);
			int razdaljaOdNasprotnikovePrazne = nasprotnikovaPot.razdaljaOdPrazne(t);
			int steviloMojihPraznih = mojaPot.steviloPraznih();
			int steviloNasprotnikovihPraznih = nasprotnikovaPot.steviloPraznih();
			int razlika = steviloMojihPraznih - steviloNasprotnikovihPraznih;
			if((razlika) >= 0) {
				if (razdaljaOdMojePrazne == 0) {
					if (razlika > 5) vrednost = 20;
					else vrednost += 5;
				} else if(razdaljaOdMojePrazne == 1) {
					if (razlika > 2) vrednost += 5;
					else vrednost += 3;
				} else if (razdaljaOdMojePrazne == 2) {
					vrednost += 1;
				}
				if (razlika < 4) {
					if(razdaljaOdNasprotnikovePrazne == 0) {
						vrednost += 3;
					} if(razdaljaOdNasprotnikovePrazne == 1) {
						vrednost += 2;
					}
				}
			} else {
				if( razlika > -4) {
					if (razdaljaOdMojePrazne == 0) {
						vrednost += 2;
					} else if(razdaljaOdMojePrazne == 1) {
						vrednost += 1;
					} 
				}
				if(razdaljaOdNasprotnikovePrazne == 0) {
					if (razlika == -1) vrednost += 4;
					else vrednost += 2;
				} else if(razdaljaOdNasprotnikovePrazne == 1) {
					if(razlika < -3) {
						vrednost += 6;
					}
					else if(razlika == -1) vrednost += 3;
					else vrednost += 5;
				} else if (razdaljaOdNasprotnikovePrazne == 2) {
					if(razlika < -3) vrednost += 3;
					else vrednost += 2;
				}
			}
			return vrednost;
	 }
		
	 
		static class SortKoordinati implements Comparator<Koordinati> { 
			private Igra igra;
			
			public SortKoordinati(Igra igra){ 
				this.igra = igra;
			}
			
			public SortKoordinati(Igra igra, int tip) {
				this.igra = igra;
			}
			
		    public int compare(Koordinati a, Koordinati b) {
		    	return igra.vrednostKoordinate(b) - igra.vrednostKoordinate(a);
		    }
		} 
}
