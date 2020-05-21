package logika;

import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;

import inteligenca.Minimax;
import koordinati.Koordinati;

public class Igra {
	public static final Color PRAZNO = Color.WHITE;
	public static final Color RDECA = Color.RED;
	public static final Color MODRA = Color.BLUE;
	
	public static int[][] mtrx;
	
	private int velikost = Plosca.getVelikost();
	
	public Plosca plosca;
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
		System.out.println(igra.plosca);
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
		 
		 plosca = new Plosca(velikost);
		 igralecNaPotezi = igralec1; 
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
	 
	 public int getVelikost() {return velikost;}
	 
	 public Igralec getIgralecNaPotezi() {return igralecNaPotezi;}
	 
	 public boolean odigraj(Koordinati koordinati) {
		 if(jeVeljavnaPoteza(koordinati)) {
			 plosca.odigraj(koordinati,getIgralecIndex(igralecNaPotezi));
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
		 System.out.println(veljavne);
		 return veljavne.contains(koordinati);
	 }
	 
	 public ArrayList<Koordinati> veljavnePoteze(){
		 System.out.println("iscemVedljavne");
		 return plosca.prazne();
	 }
	 
	 public ArrayList<ArrayList<Koordinati>> vrniKoordinate() {
		 return plosca.getPlosca();
	 }
	 
	 public LinkedHashMap<Koordinati,Color> vrniStanje() {
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
		int zmagovalec = plosca.getZmagovalec();
		if (zmagovalec > 0) {
			return igralca.get(zmagovalec - 1);
		}
		return null;
	 }
	 
	 private Igralec getIgralec(Color barva) {
		 for(Igralec igralec: igralca) {
			 if (igralec.getBarva().equals(barva)) {
				 return igralec;
			 }
		 }
		 return null;
	 }
	 
	 public static Igra kopirajIgro (Igra original) {
		 Igra kopija = new Igra();
		 
		 kopija.mtrx = original.mtrx;
		 
		 kopija.velikost = original.velikost;
		 kopija.plosca = original.plosca;
		 kopija.igralecNaPotezi = original.igralecNaPotezi;
		 
		 
		 kopija.igralec1 = original.igralec1;
		 kopija.igralec2 = original.igralec2;
		 kopija.igralca = original.igralca;
		 kopija.zmagovalec = original.zmagovalec;
		 kopija.konec = original.konec;
		 
		 return kopija;
	 }

}
