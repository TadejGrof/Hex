package graficniVmesnik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import koordinati.Koordinati;
import logika.Igra;

public class IgraPanel extends JPanel{
	private Okno okno;
	private Igra igra;
	private JPanel menuBar;
	private Platno platno;
	private GridBagConstraints gbc;
	private CenterLabel igralecLabel;
	private CenterLabel stanjeLabel;
	private JButton nazajButton;
	private JButton novoButton;
	
	private final String zmagaStanje = "Zmagovalec:";
	private final String potezaStanje = "Igralec na potezi:";
	
	
	public IgraPanel(Okno Okno) {
		this.okno = Okno;
		
		setLayout(new GridBagLayout());
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		
		menuBar = new JPanel(new GridLayout(1,0));
		
		nazajButton = new JButton("Nazaj");
		menuBar.add(nazajButton);
		
		JPanel labelsPanel = new JPanel(new GridLayout(2,1));
		stanjeLabel = new CenterLabel(potezaStanje);
		labelsPanel.add(stanjeLabel);
		
		igralecLabel = new CenterLabel("Igralec1");
		labelsPanel.add(igralecLabel);
		
		menuBar.add(labelsPanel);
		
		novoButton = new JButton("Nova igra");
		novoButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				okno.pokaziMenu();
			}
		});
		menuBar.add(novoButton);
		
		add(menuBar,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.8;
		platno = new Platno();
		add(platno,gbc);
	}
	
	public Igra getIgra() {
		return igra;
	}
	
	public void setIgra(Igra igra) {
		this.igra = igra;
		refreshPanel();
	}
	
	
	private void refreshPanel() {
		if(igra.konecIgre()) {
			igralecLabel.setText(igra.zmagovalecIgre().toString());
			stanjeLabel.setText(zmagaStanje);
		} else {
			igralecLabel.setText(igra.getIgralecNaPotezi().toString());
			stanjeLabel.setText(potezaStanje);
		}
		platno.refreshPlatno();
	}
	
	private class CenterLabel extends JLabel{
		private static final long serialVersionUID = 1L;

		public CenterLabel(String text) {
			super(text,SwingConstants.CENTER);
		}
	}
	
	public class Platno extends JPanel implements MouseListener{
		private ArrayList<Sestkotnik> sestkotniki;
		private Sestkotnik hex;
		private int N;
		private int R;
		private int visina;
		private int sirina;
		private double v;
		private Okno okno;
		private ArrayList<Polygon> polygoni;
		
		public Platno() {
			polygoni = new ArrayList<Polygon>();
			sestkotniki = new ArrayList<Sestkotnik>();
			initialize();
			this.addMouseListener(this);
		}
		
		private void initialize() {
			if (igra != null) {
				int i; int j;
				N = igra.getVelikost();
				R = 28;
				visina = 550;
				sirina = 900;
				int odmikX = 100;
				int odmikY = 80;
				v = Math.sqrt(3) * R / 2;
				ArrayList<ArrayList<Koordinati>> koordinate = igra.vrniKoordinate();
				HashMap<Koordinati, Color> stanje = igra.vrniStanje();
				for( i = 0; i < N; i++) {
					for ( j = 0; j < N; j++) {
						Koordinati koordinati = koordinate.get(i).get(j);
						Color barva = stanje.get(koordinati);
						double x = odmikX + N*v - i * v + j * 2* v;
						double y = - odmikY + visina - i * 3 * R / 2;
						sestkotniki.add(new Sestkotnik(x,y,R,koordinati,barva));
					}
				}
			}	
		}
		
		private void refreshPlatno() {
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
		        			refreshPanel();
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
	
}
