package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import logika.Plosca.NajkrajsaPot;
import splosno.Koordinati;

public class Igra {
	public static final Color PRAZNO = Color.WHITE;
	public static final Color RDECA = Color.RED;
	public static final Color MODRA = Color.BLUE;
	
	public static final int IGRA = 0;
	public static final int ZMAGA1 = 1;
	public static final int ZMAGA2 = 2;
	
	public int velikost;
	
	public Plosca plosca;
	public Igralec igralecNaPotezi;
	
	public Igralec igralec1;
	public Igralec igralec2;
	private ArrayList<Igralec> igralca;

	public ArrayList<Poteza> poteze;
	
	public static void main(String[] args) {
		// POSKUS
	}
	
	public Igra() {
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
		 
		 igralec1 = new Igralec("Igralec1", RDECA, Igralec.IGRALEC);
		 igralca.add(igralec1);
		 igralec2 = new Igralec("Igralec2", MODRA, Igralec.IGRALEC);
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
			 return igralec1.barva;
		 } else if (index == 2) {
			 return igralec2.barva;
		 }
		 return PRAZNO;
	 }
	 
	 public void setIgralca(Igralec igralec1, Igralec igralec2) {
		 this.igralec1 = igralec1;
		 this.igralec2 = igralec2;
		 
		 igralecNaPotezi = igralec1;
		 igralca.clear();
		 igralca.add(igralec1);
		 igralca.add(igralec2);
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
	 
	 public void razveljaviZadnjoPotezo() {
		 int steviloPotez = poteze.size();
		 if(Igralec.jeRacunalnik(igralec1.tip) && Igralec.jeRacunalnik(igralec2.tip)) return;
		 else if(steviloPotez == 0) return;
		 else if (Igralec.jeRacunalnik(igralecNaPotezi.tip)) return;
		 else if(steviloPotez == 1) {
			 Poteza poteza = poteze.get(0);
			 plosca.odigraj(poteza.koordinati,0);
			 igralecNaPotezi = nasprotnik(igralecNaPotezi);
			 poteze.remove(poteza);
		 } else if( !Igralec.jeRacunalnik(igralec1.tip) && !Igralec.jeRacunalnik(igralec2.tip)) {
			 Poteza poteza = poteze.get(steviloPotez - 1);
			 plosca.odigraj(poteza.koordinati,0);
			 igralecNaPotezi = nasprotnik(igralecNaPotezi);
			 poteze.remove(poteza);
		 } else {
			 Poteza poteza = poteze.get(steviloPotez - 1);
			 plosca.odigraj(poteza.koordinati,0);
			 igralecNaPotezi = nasprotnik(igralecNaPotezi);
			 poteze.remove(poteza);
			 
			 poteza = poteze.get(steviloPotez - 2);
			 plosca.odigraj(poteza.koordinati,0);
			 igralecNaPotezi = nasprotnik(igralecNaPotezi);
			 poteze.remove(poteza);
		 }
	 }
	 
	 private boolean jeVeljavnaPoteza(Koordinati koordinati) {
		 ArrayList<Koordinati> veljavne = veljavnePoteze();
		 return veljavne.contains(koordinati);
	 }
	 
	 public ArrayList<Koordinati> veljavnePoteze(){
		 return plosca.prazne();
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
			 if (igralec.barva.equals(barva)) {
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
		 return kopija;
	 }
	 
	 public List<Koordinati> urejeneMoznePoteze(){
		List<Koordinati> poteze = veljavnePoteze();
		Collections.sort(poteze,new SortKoordinati(this));
		if(poteze.size() > 20) {
			poteze = poteze.subList(0, 20);
		}
		return poteze;
	}
	 
	 
	 public int vrednostKoordinate(Koordinati t) {
			int vrednost = 0;
			NajkrajsaPot mojaPot = najkrajsaPot(igralecNaPotezi);
			NajkrajsaPot nasprotnikovaPot = najkrajsaPot(nasprotnik(igralecNaPotezi));
			int steviloMojihPraznih = mojaPot.steviloPraznih();
			int steviloNasprotnikovihPraznih = nasprotnikovaPot.steviloPraznih();
			int razlika = steviloNasprotnikovihPraznih - steviloMojihPraznih;
			if((razlika) >= 0) {
				int razdaljaOdMojePrazne = mojaPot.razdaljaOdPrazne(t);
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
					int razdaljaOdNasprotnikovePrazne = nasprotnikovaPot.razdaljaOdPrazne(t);
					if(razdaljaOdNasprotnikovePrazne == 0) {
						vrednost += 3;
					} if(razdaljaOdNasprotnikovePrazne == 1) {
						vrednost += 2;
					}
				}
				int razdaljaOdMojePolne = mojaPot.razdaljaOdPolne(t);
				if (razdaljaOdMojePolne == 1) vrednost += 5;
			} else {
				int razdaljaOdNasprotnikovePrazne = nasprotnikovaPot.razdaljaOdPrazne(t);
				if( razlika > -4) {
					int razdaljaOdMojePrazne = mojaPot.razdaljaOdPrazne(t);
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
				int razdaljaOdNasprotnikovePolne = mojaPot.razdaljaOdPolne(t);
				if (razdaljaOdNasprotnikovePolne == 1) vrednost += 5;
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
