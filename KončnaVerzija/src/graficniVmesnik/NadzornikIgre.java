package graficniVmesnik;

import java.util.HashMap;
import javax.swing.SwingWorker;

import logika.Igra;
import logika.Igralec;
import splosno.Koordinati;


// deluje enako kot nadzornikIgre iz predavanj.

public class NadzornikIgre {
	public static HashMap<Igralec,Integer> tipaIgralcev;
	public static boolean clovekNaPotezi;
	public static IgraPanel igraPanel;
	public static Igra igra = null;
	
	public static void novaIgra(Igra novaIgra) {
		igra = novaIgra;
		igramo();
	}
	
	public static void igramo() {
		igraPanel.refreshPanel();
		int stanje = igra.getStanje();
		if(stanje == Igra.ZMAGA1 | stanje == Igra.ZMAGA2) {return;}
		else {
			Igralec igralec = igra.igralecNaPotezi;
			int tipIgralca = tipaIgralcev.get(igralec);
			if(Igralec.jeRacunalnik(tipIgralca)) {
				racunalnikovaPoteza();
			} else if (tipIgralca == Igralec.IGRALEC) {
				clovekNaPotezi = true;
			}
		}
	}
	
	
	
	private static void racunalnikovaPoteza() {
		SwingWorker<Koordinati,Void> worker = new SwingWorker<Koordinati,Void>(){
			@Override
			protected Koordinati doInBackground() throws Exception {
				long startTime = System.currentTimeMillis();
				Koordinati poteza = igra.igralecNaPotezi.inteligenca.izberiPotezo(igra);
				long endTime = System.currentTimeMillis();
				System.out.println("That took " + (endTime - startTime) + " milliseconds");
	
				return poteza;
			}
			@Override
			protected void done() {
				Koordinati poteza = null;
				try { poteza = get();} catch(Exception e) {};
				if(poteza != null) {
					igra.odigraj(poteza);
					igramo();
				}
			}
		};
		worker.execute();
	}
	
	public static void clovekovaPoteza(Koordinati poteza) {
		if(igra.odigraj(poteza)) clovekNaPotezi = false;
		igramo();
	}
	
}
