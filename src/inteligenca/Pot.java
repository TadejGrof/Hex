package inteligenca;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import splosno.Koordinati;
import logika.Igra;
import logika.Plosca;

public class Pot {

	public static int[][] resitev;
	public int[] velikost;

	public Pot(int velikost) {
		resitev = new int[velikost][velikost];
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				resitev[i][j] = 0;
			}
		}
	}

	public void resiMatriko(int[][] matrika, int velikost) {
		if (najdiPot(matrika, 0, 0, velikost, "dol")) {
			print(resitev, velikost);
		}else{
			System.out.println("ni rešljivo");
		}
		
	}

	public boolean najdiPot(int[][] matrika, int x, int y, int velikost, String smer) {
		if(x==velikost-1 && y==velikost-1){
			resitev[x][y] = 1;
			return true;
		}
		if (jeResljivo(matrika, x, y, velikost)) {
			resitev[x][y] = 1;			
			if(smer!="gor" && najdiPot(matrika, x+1, y, velikost, "dol")){
				return true;
			}
			if(smer!="levo" && najdiPot(matrika, x, y+1, velikost,"desno")){
				return true;
			}
			if(smer!="dol" && najdiPot(matrika, x-1, y, velikost, "gor")){
				return true;
			}
			if(smer!="desno" &&  najdiPot(matrika, x, y-1, velikost, "levo")){
				return true;
			}
			resitev[x][y] = 0;
			return false;
		}
		return false;
	}

	public boolean jeResljivo(int[][] matrika, int x, int y, int velikost) {
		if (x >= 0 && y >= 0 && x < velikost  && y < velikost && matrika[x][y] != 0) {
			return true;
		}
		return false;
	}
	
	public void print(int [][] resitev, int velikost){
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				System.out.print(" " + resitev[i][j]);
			}
			System.out.println();
		}
	}
	
	public static int dolzina(int [][] resitev, int velikost){
		int dolzina = 0;
		for (int i = 0; i < velikost; i++) {
			for (int j = 0; j < velikost; j++) {
				if (resitev[i][j] == 1) dolzina++;
			}
		}
		return dolzina;
	}	
	
	public static int[][] generateIntMtrx (int velikost) {
		int[][] intmtrx = new int[velikost][velikost];
		int[] vrstica = new int[velikost];
		for (int i = 0; i < velikost; i++) {
			vrstica = new int[velikost];
			for (int j = 0; j < velikost; j++) {
				vrstica[j] = 1;
			}
			intmtrx[i] = vrstica;
		}
		return intmtrx;
	}
	
	public static void printIntMtrx (int[][] matrika) {
		for (int i = 0; i < matrika.length; i++) {
			System.out.println();
			for (int j = 0; j < matrika.length; j++) {
				System.out.print(" " + matrika[i][j] + " ");
			}
		}
	}
	//(x, y) -> začetna pozicija
	//igralec -> pove, ali gledamo levo-desno ali gor-dol
	public static int evaluate (int[][] matrika, int igralec, int x, int y) {
		int score = 0;
		//
		
		return score;
	}
	
	public static void main(String[] args) {
		int velikost = 5;
		int[][] test = { { 1, 1, 0, 0, 0 },{ 0, 0, 0, 0, 0 },{ 0, 0, 0, 0, 0 },
				{ 0, 0, 0, 0, 0 },{ 0, 0, 0, 0, 0 } };
		int[][] matrika = generateIntMtrx(5);
		System.out.println();
		Pot r = new Pot(velikost);
		r.resiMatriko(matrika, velikost);
		System.out.println(dolzina(resitev, velikost));
	}

}