package logika;

import java.awt.Color;
import inteligenca.Inteligenca;

public class Igralec {
	public static final int IGRALEC = 0;
	public static final int LAHEK_RACUNALNIK = 1;
	public static final int SREDNJI_RACUNALNIK = 2;
	public static final int TEZEK_RACUNALNIK = 3;
	
	public int tip;
	
	public String ime;
	public Color barva;
	
	public Inteligenca inteligenca;
	
	public Igralec(String ime, Color barva, int tip) {
		this.ime = ime;
		this.barva = barva;
		this.tip = tip;
		setInteligenca();
	}
	
	@Override
	public String toString() {
		return ime;
	}
	
	
	// nastavi inteligenco za racunalniskega igralca
	public void setInteligenca() {
		if(tip == LAHEK_RACUNALNIK) {
			this.inteligenca = new Inteligenca(Inteligenca.LAHKO);
		} else if(tip == SREDNJI_RACUNALNIK){
			this.inteligenca = new Inteligenca(Inteligenca.SREDNJE);
		} else if( tip == TEZEK_RACUNALNIK) {
			this.inteligenca = new Inteligenca(Inteligenca.TEZKO);
		}
	}
	
	
	// preveri če je igralec računalnik
	public static boolean jeRacunalnik(int tip) {
		return tip == LAHEK_RACUNALNIK | tip == SREDNJI_RACUNALNIK | tip == TEZEK_RACUNALNIK;
	}
	
}
