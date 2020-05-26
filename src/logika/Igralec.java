package logika;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import inteligenca.Inteligenca;
import splosno.Koordinati;

public class Igralec {
	public static final int IGRALEC = 0;
	public static final int LAHEK_RACUNALNIK = 1;
	public static final int SREDNJI_RACUNALNIK = 2;
	public static final int TEZEK_RACUNALNIK = 3;
	
	public int tip;
	
	private String ime;
	private Color barva;
	private Igra igra;
	
	public Inteligenca inteligenca;
	
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
		setInteligenca();
	}
	
	public Igralec(String ime, Color barva, int tip) {
		this.ime = ime;
		this.barva = barva;
		this.tip = tip;
		setInteligenca();
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
	
	public void setInteligenca() {
		if(tip == LAHEK_RACUNALNIK) {
			this.inteligenca = new Inteligenca(Inteligenca.LAHKO);
		} else if(tip == SREDNJI_RACUNALNIK){
			this.inteligenca = new Inteligenca(Inteligenca.SREDNJE);
		} else if( tip == TEZEK_RACUNALNIK) {
			this.inteligenca = new Inteligenca(Inteligenca.TEZKO);
		}
	}
	
	public static boolean jeRacunalnik(int tip) {
		return tip == LAHEK_RACUNALNIK | tip == SREDNJI_RACUNALNIK | tip == TEZEK_RACUNALNIK;
	}
	
}
