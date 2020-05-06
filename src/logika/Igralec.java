package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import koordinati.Koordinati;

public class Igralec {
	public static final int IGRALEC = 0;
	public static final int RACUNALNIK = 1;
	
	private int tip;
	
	private String ime;
	private Color barva;
	private Igra igra;
	
	public Igralec(String ime,Igra igra, Color barva) {
		this.igra = igra;
		this.ime = ime;
		tip = IGRALEC;
		this.barva = barva;
	}
	
	public Igralec(String ime, Igra igra, Color barva, int tip) {
		this.igra = igra;
		this.ime = ime;
		this.barva = barva;
		this.tip = tip;
	}
	
	public Igralec(String ime, Color barva, int tip) {
		this.ime = ime;
		this.barva = barva;
		this.tip = tip;
	}
	
	public void setIgra(Igra igra) {
		this.igra = igra;
	}
	
	public Color getBarva() {
		return this.barva;
	}
	
	@Override
	public String toString() {
		return ime;
	}
	
	public void nakljucnaPoteza() {
		Random random = new Random();
		ArrayList<Koordinati> veljavne = igra.veljavnePoteze();
		igra.odigraj(veljavne.get(random.nextInt(veljavne.size())));
	}
	
}
