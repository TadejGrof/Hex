package logika;

import java.util.ArrayList;

import splosno.Koordinati;

public class Pot extends ArrayList<Koordinati>{
	public int igralec;
	
	public Pot(int igralec) {
		this.igralec = igralec;
	}
	
	public Pot(Koordinati t, int igralec) {
		this(igralec);
		add(t);
	}
	
	
	public boolean jeKoncna(Plosca plosca) {
		return false;
	}
	
	public Pot(ArrayList<Koordinati> pot, int igralec) {
		this(igralec);
		this.addAll(pot);
	}
	
	
}
