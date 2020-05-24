package inteligenca;

import java.util.LinkedList;

import logika.Igra;
import logika.Plosca;
import splosno.Koordinati;

// po 0 se ne moreš premikat, po enkah pa lahko
public class BoljšaPot {
       int x;
       int y;
       int dolžinaPoti;
       private BoljšaPot BoljšaPot;
       public static LinkedList<BoljšaPot> končnaPot;
       public static int velikost;
       
       BoljšaPot(int x, int y, int dolžinaPoti, BoljšaPot BoljšaPot) {
            this.x = x;
            this.y = y;
            this.dolžinaPoti = dolžinaPoti;
            this.BoljšaPot = BoljšaPot;
        }
       
       // brez tega čudno izpisuje
        public String toString(){
        	return "("+x+ ","+y+")";
        }
    
    public static int getX (BoljšaPot b) {
    	return b.x;
    }
    
    public static int getY (BoljšaPot b) {
    	return b.y;
    }
    
    public static Koordinati vKoordinati (BoljšaPot b) {
    	int x = b.getX(b);
    	int y = b.getY(b);
    	Koordinati koordinata = new Koordinati(x, y);
    	return koordinata;
    }

    public static int[][] vMatriko (LinkedList<BoljšaPot> rešitev, int velikost) {
    	int[][] novaMatrika = generateIntMtrx(velikost);
    	for (BoljšaPot polje : rešitev) {
    		novaMatrika[polje.getX(polje)][polje.getY(polje)] = 1; 
    	}
    	return novaMatrika;
    }
	public static int[][] generateIntMtrx (int velikost) {
		int[][] intmtrx = new int[velikost][velikost];
		int[] vrstica = new int[velikost];
		for (int i = 0; i < velikost; i++) {
			vrstica = new int[velikost];
			for (int j = 0; j < velikost; j++) {
				vrstica[j] = 0;
			}
			intmtrx[i] = vrstica;
		}
		return intmtrx;
	}
	
    public static LinkedList<BoljšaPot> poiščiPot(int[][] matrika, int[] začetnoPolje, int[] končnoPolje) {
    	// če sta izbiri začetnega ali končnega polja neveljavni, ne naredi nič
	   if (matrika[začetnoPolje[0]][začetnoPolje[1]] == 0 || matrika[končnoPolje[0]][končnoPolje[1]] == 0);
	   
        BoljšaPot[][] novaPot = new BoljšaPot[matrika.length][matrika.length];
        for (int i = 0; i < novaPot.length; i++) {
            for (int j = 0; j < novaPot[0].length; j++) {
                if (matrika[i][j] != 0) {
                    novaPot[i][j] = new BoljšaPot(i, j, Integer.MAX_VALUE, null);
                }
            }
        }
 
        LinkedList<BoljšaPot> izbranaPot = new LinkedList();
        // prvi element nove poti je lahko kar začetnoPolje
        BoljšaPot prvoPolje = novaPot[začetnoPolje[0]][začetnoPolje[1]];
        prvoPolje.dolžinaPoti = 0;
        izbranaPot.add(prvoPolje);
        BoljšaPot pozicija = null;
        BoljšaPot trenutnoPolje;
        
        while ((trenutnoPolje = izbranaPot.poll()) != null) {
            if (trenutnoPolje.x==končnoPolje[0] && trenutnoPolje.y == končnoPolje[1]) {
                pozicija = trenutnoPolje;
            }
            preglej(novaPot, izbranaPot, trenutnoPolje.x - 1, trenutnoPolje.y, trenutnoPolje);
            preglej(novaPot, izbranaPot, trenutnoPolje.x + 1, trenutnoPolje.y, trenutnoPolje);
            preglej(novaPot, izbranaPot, trenutnoPolje.x, trenutnoPolje.y - 1, trenutnoPolje);
            preglej(novaPot, izbranaPot, trenutnoPolje.x, trenutnoPolje.y + 1, trenutnoPolje);
        }
 
        if (pozicija == null) {
            return izbranaPot;
        } else {
            LinkedList<BoljšaPot> pot = new LinkedList();
            trenutnoPolje = pozicija;
            do {
                pot.addFirst(trenutnoPolje);
            } while ((trenutnoPolje = trenutnoPolje.BoljšaPot) != null);
            System.out.println(pot);
            končnaPot = pot;
        }
        return končnaPot;
    }
    
    public static void natisniPolja (LinkedList<BoljšaPot> pot) {
    	if (pot.size() == 0) {
    		System.out.println("prazna pot!");
    	}
    	for (BoljšaPot polje : pot) {
    		System.out.println("(" + polje.getX(polje)+"," + polje.getY(polje)+ ")");
    	}
    }
    
    public static void preglej(BoljšaPot[][] novaPot, LinkedList<BoljšaPot> izbranaPot, int x, int y, BoljšaPot prejšnjePolje) {
        int dolžinaPoti = prejšnjePolje.dolžinaPoti + 1;
        if (x < 0 || x >= novaPot.length || y < 0 || y >= novaPot[0].length || novaPot[x][y] == null) {
        	return;
        }
        BoljšaPot trenutnoPolje = novaPot[x][y];
        if (dolžinaPoti < trenutnoPolje.dolžinaPoti) {
            trenutnoPolje.dolžinaPoti = dolžinaPoti;
            trenutnoPolje.BoljšaPot = prejšnjePolje;
            izbranaPot.add(trenutnoPolje);
        }
    }
    
	public static void main(String[] args) { 
	   int[][] test = new int[][] {
           {1, 1, 1, 1, 1, 1},
           {1, 1, 1, 1, 0, 1},
           {1, 0, 0, 0, 1, 1},
           {1, 1, 1, 1, 1, 1},
           {0, 0, 0, 0, 0, 0}};
	   int[] začetnoPolje = {1,1};
	   int[] končnoPolje = {3,2};
	   LinkedList<BoljšaPot> rešitev = poiščiPot(test, začetnoPolje, končnoPolje);
	   natisniPolja(rešitev);
	   int[][] matrika = vMatriko(rešitev, 4);
	   Igra.printIntMtrx(matrika);
	} 
}