package logika;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import koordinati.Koordinati;

public class Igra {
	public static final int PRAZNO = 0;
	public static final int RDECA = 1;
	public static final int MODRA = 2;
	
	private int velikost;
	private Plosca plosca;
	private Igralec igralecNaPotezi;
	
	private Igralec igralec1;
	private Igralec igralec2;
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
		 plosca = new Plosca(velikost);
		 
		 igralca = new ArrayList<Igralec>();
		 
		 igralec1 = new Igralec("Igralec1", this, RDECA, Igralec.RACUNALNIK);
		 igralca.add(igralec1);
		 igralec2 = new Igralec("Igralec2", this, MODRA, Igralec.RACUNALNIK);
		 igralca.add(igralec2);
		 
		 igralecNaPotezi = igralec1;
		 
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
	 
	 public HashMap<Koordinati,Integer> vrniStanje() {
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
		int tip = plosca.getZmagovalec();
		
		return getIgralec(tip); 
	 }
	 
	 private Igralec getIgralec(int tip) {
		 for(Igralec igralec: igralca) {
			 if (igralec.getBarva() == tip) {
				 return igralec;
			 }
		 }
		 return null;
	 }
	 public Color getBarva(int barva) {
		 if(barva == PRAZNO) {
			 return Color.white;
		 } else if (barva == RDECA) {
			 return Color.red;
		 } else if ( barva == MODRA) {
			 return Color.blue;
		 } 
		 return Color.white;
	 }
}
