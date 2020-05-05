package graficniVmesnik;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import koordinati.Koordinati;
import logika.Igra;

public class Platno extends JPanel implements MouseListener{
	private ArrayList<Sestkotnik> sestkotniki;
	private Sestkotnik hex;
	private Igra igra;
	private int N;
	private int R;
	private int visina;
	private int sirina;
	private double v;
	private Okno okno;
	private ArrayList<Polygon> polygoni;
	
	public Platno(Okno okno,Igra igra) {
		this.okno = okno;
		this.igra = igra;
		polygoni = new ArrayList<Polygon>();
		sestkotniki = new ArrayList<Sestkotnik>();
		initialize();
		this.addMouseListener(this);
	}
	
	private void initialize() {
		int i; int j;
		N = igra.getVelikost();
		R = 28;
		visina = 550;
		sirina = 900;
		int odmikX = 100;
		int odmikY = 80;
		v = Math.sqrt(3) * R / 2;
		ArrayList<ArrayList<Koordinati>> koordinate = igra.vrniKoordinate();
		HashMap<Koordinati, Integer> stanje = igra.vrniStanje();
		for( i = 0; i < N; i++) {
			for ( j = 0; j < N; j++) {
				Koordinati koordinati = koordinate.get(i).get(j);
				Color barva = igra.getBarva(stanje.get(koordinati));
				double x = odmikX + N*v - i * v + j * 2* v;
				double y = - odmikY + visina - i * 3 * R / 2;
				sestkotniki.add(new Sestkotnik(x,y,R,koordinati,barva));
			}
		}
		
	}
	
	public void refresh() {
		removeAll();
		sestkotniki.clear();
		initialize();
		repaint();
	}
	
	@Override 
	protected void paintComponent(Graphics g) {
		polygoni.clear();
		super.paintComponent(g);
		for (Sestkotnik hex: sestkotniki) {
			g.setColor(Color.black);
			g.drawPolygon(hex.getHexagon());
			g.fillPolygon(hex.getHexagon());
			g.setColor(hex.getBarva());
			polygoni.add(hex.getHexagon());
			
			Polygon smallerHex = hex.getSmallerHexagon(2);
			g.drawPolygon(smallerHex);
			g.fillPolygon(smallerHex);
		}
		g.dispose();
	}

	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		if (!igra.konecIgre()) {
	        for(Sestkotnik hex:sestkotniki) {
	        	if (hex.getHexagon().contains(p)) {
	        		if ( igra.odigraj(hex.getKoordinati())) {
	        			okno.refresh();
	        			refresh();
	        		}
	        		break;
	        	}
	        }
		}
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}

