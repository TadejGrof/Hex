package logika;

import java.util.ArrayList;
import java.util.Random;

import koordinati.Koordinati;

public class Igralec {
	public static final int IGRALEC = 0;
	public static final int RACUNALNIK = 1;
	
	private int tip;
	
	private String ime;
	private int barva;
	private Igra igra;
	
	public Igralec(String ime,Igra igra, int barva) {
		this.igra = igra;
		this.ime = ime;
		tip = IGRALEC;
		this.barva = barva;
	}
	
	public Igralec(String ime, Igra igra, int barva, int tip) {
		this.igra = igra;
		this.ime = ime;
		this.barva = barva;
		this.tip = tip;
	}
	
	public int getBarva() {
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
