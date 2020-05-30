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
	
	
	// ustvari igro velikosti 11
	public Igra() {
		velikost = 11;
		initialize();
	}
	
	// ustvari igro poljubne velikosti
	 public Igra(int velikost) {
		 this.velikost = velikost;
		 initialize();
	 }
	 
	 // odigra nakljucno potezo.
	 // Trenutno ni v uporabi..uporabljala se je pri racunalniku z nakljucno inteligenco
	 public void nakljucnaPoteza(){
		Random random = new Random();
		ArrayList<Koordinati> veljavne = veljavnePoteze();
		odigraj(veljavne.get(random.nextInt(veljavne.size())));
	 }
	 
	 // ustvari vse potrebne sezname, nastavi igralca ter ustvari plosco,
	 // zacne igralec1
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
	 
	 // vrne nasprotnika za podanega igralca
	 public Igralec nasprotnik(Igralec igralec) {
		 if(igralec == igralec1) {
			 return igralec2;
		 } else if( igralec == igralec2) {
			 return igralec1;
		 }
		 return null;
	 }
	 
	 // za danega igralca vrne index, ki se igra na plosci
	 public Integer getIgralecIndex(Igralec igralec) {
		 if(igralec.equals(igralec1)){
			 return 1;
		 } else if (igralec.equals(igralec2)) {
			 return 2;
		 }
		 return 0;
	 }
	 
	 // za index igralca vrne njegovo barvo.
	 // Uporabljena v graficnem vmesniku ko potrebujemo barvo
	 // za igrano vrednost na plosci
	 public Color getIgralecBarva(int index) {
		 if (index == 1) {
			 return igralec1.barva;
		 } else if (index == 2) {
			 return igralec2.barva;
		 }
		 return PRAZNO;
	 }
	 
	 
	 // nastavi nova igralca.
	 // Poklice se na zacetku igre
	 public void setIgralca(Igralec igralec1, Igralec igralec2) {
		 this.igralec1 = igralec1;
		 this.igralec2 = igralec2;
		 
		 igralecNaPotezi = igralec1;
		 igralca.clear();
		 igralca.add(igralec1);
		 igralca.add(igralec2);
	 }
	 
	 
	 // preveri če je poteza veljavna in jo odigra
	 public boolean odigraj(Koordinati koordinati) {
		 if(jeVeljavnaPoteza(koordinati)) {
			 plosca.odigraj(koordinati,getIgralecIndex(igralecNaPotezi));
			 poteze.add(new Poteza(igralecNaPotezi,koordinati));
			 naslednjiNaPotezi();
			 return true;
		 } 
		 return false;
	 }
	 
	 // nastavi naslednjega na potezi
	 private void naslednjiNaPotezi() {
		 igralecNaPotezi = nasprotnik(igralecNaPotezi);
	 }
	 
	 
	 // razveljavi zadnjo igrano potezo.
	 // V primeru da igralec igra z racunalnikov razveljavi zadnji dve.
	 // deluje samo takrat ko je igra še v teku in je na potezi človek.
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
	 
	 // preveri če je podana poteza veljavna
	 private boolean jeVeljavnaPoteza(Koordinati koordinati) {
		 ArrayList<Koordinati> veljavne = veljavnePoteze();
		 return veljavne.contains(koordinati);
	 }
	 
	 
	 // vrne vse veljavne poteze
	 public ArrayList<Koordinati> veljavnePoteze(){
		 return plosca.prazne();
	 }
	 
	 // vrne najkrajso pot za podanega igralca
	 public NajkrajsaPot najkrajsaPot(Igralec igralec) {
		 if(igralec == igralec1) {
			 return plosca.najkrajsaPotIgralec1;
		 } else if(igralec == igralec2) {
			 return plosca.najkrajsaPotIgralec2;
		 }
		 return null;
	 }
	 
	 
	 // preveri če je igre konec
	 public boolean konecIgre() {
		return plosca.konecIgre();
	 }
	 
	 // vrne stanje igre
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
	 
	 // vrne zmagovalca igre
	 public Igralec zmagovalecIgre() {
		int zmagovalec = plosca.getZmagovalec();
		if (zmagovalec > 0) {
			return igralca.get(zmagovalec - 1);
		}
		return null;
	 }
	 
	 
	 // vrne kopijo igre
	 public Igra kopirajIgro () {
		 Igra kopija = new Igra();
		 kopija.velikost = velikost;
		 kopija.plosca = plosca.kopirajPlosco();
		 kopija.setIgralca(igralec1, igralec2);
		 kopija.igralecNaPotezi = igralecNaPotezi;
		 kopija.poteze = new ArrayList<Poteza>(poteze);
		 return kopija;
	 }
	 
	 
	 // class omogoča beleženje potez v seznamu s pomočjo katera potem
	 // lahko razveljavimo zadnjo potezo ali pa doimo število že igranih potez.
	 public class Poteza {
			public Igralec igralec;
			public Koordinati koordinati;
			
			public Poteza(Igralec igralec, Koordinati koordinati) {
				this.igralec = igralec;
				this.koordinati = koordinati;
			}
			
		}
}
