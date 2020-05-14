package logika;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import koordinati.Koordinati;

public class Igra {
	public static final Color PRAZNO = Color.WHITE;
	public static final Color RDECA = Color.RED;
	public static final Color MODRA = Color.BLUE;
	
	private int velikost;
	private Plosca plosca;
	private Igralec igralecNaPotezi;
	
	private static Igralec igralec1;
	private static Igralec igralec2;
	private ArrayList<Igralec> igralca;
	private Igralec zmagovalec;
	private boolean konec;
	
	public static void main(String[] args) {
		Igra igra = new Igra(4);
		Scanner myObj = new Scanner(System.in);
		while(! igra.konecIgre()) {
			myObj.nextLine();
			igra.nakljucnaPoteza();
		}
		myObj.close();
		System.out.println(igra.plosca.getStanje());
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
		 igralecNaPotezi.nakljucnaPoteza();
	 }
	 
	 
	 private void initialize() {
		 igralca = new ArrayList<Igralec>();
		 
		 igralec1 = new Igralec("Igralec1", this, RDECA, Igralec.RACUNALNIK);
		 igralca.add(igralec1);
		 igralec2 = new Igralec("Igralec2", this, MODRA, Igralec.RACUNALNIK);
		 igralca.add(igralec2);
		 
		 plosca = new Plosca(velikost, igralec1.getBarva(), igralec2.getBarva());
		 
		 igralecNaPotezi = igralec1;
		 
	 }
	 
	 public static Color getIgralecBarva(int index) {
		 if (index == 1) {
			 return igralec1.getBarva();
		 } else if (index == 2) {
			 return igralec2.getBarva();
		 }
		 return null;
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
		 
		 plosca.setIgralca(igralec1.getBarva(),igralec2.getBarva());
	 }
	 
	 public int getVelikost() {return velikost;}
	 
	 public Igralec getIgralecNaPotezi() {return igralecNaPotezi;}
	 
	 public boolean odigraj(Koordinati koordinati) {
		 if(jeVeljavnaPoteza(koordinati)) {
			 plosca.odigraj(koordinati,igralecNaPotezi.getBarva());
			 System.out.println(igralecNaPotezi.toString() + " je odigral " + koordinati.toString());
			 System.out.println(plosca.getPlosca());
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
	 
	 public HashMap<Koordinati,Color> vrniStanje() {
		 return plosca.getStanje();
	 }
	 public boolean konecIgre() {
		konec = plosca.konecIgre();
		if(konec) {
			zmagovalec = zmagovalecIgre();
			System.out.println(zmagovalec.toString());
		}
		return konec;
	 }
	 
	 public Igralec zmagovalecIgre() {
		Color barva = plosca.getZmagovalec();
		return getIgralec(barva); 
	 }
	 
	 private Igralec getIgralec(Color barva) {
		 for(Igralec igralec: igralca) {
			 if (igralec.getBarva().equals(barva)) {
				 return igralec;
			 }
		 }
		 return null;
	 }

}
