package logika;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import graficniVmesnik.IgraPanel;
import splosno.Koordinati;

public class NadzornikIgre {
	public static HashMap<Igralec,Integer> tipaIgralcev;
	public static boolean clovekNaPotezi;
	public static IgraPanel igraPanel;
	public static Igra igra = null;
	
	private static Random random = new Random();
	
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
				Koordinati poteza = igra.igralecNaPotezi.inteligenca.izberiPotezo(igra);
				try {TimeUnit.SECONDS.sleep(1);} catch(Exception e) {};
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
