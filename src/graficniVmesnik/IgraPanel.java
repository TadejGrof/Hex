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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JPanel;
import splosno.Koordinati;

import logika.Igra;
import logika.Igralec;
import logika.NadzornikIgre;

public class IgraPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
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
		stanjeLabel.setRatio(5);
		labelsPanel.add(stanjeLabel);
		
		igralecLabel = new CenterLabel("Igralec1");
		igralecLabel.setRatio(4);
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
		
		NadzornikIgre.tipaIgralcev = new HashMap<Igralec,Integer>();
		NadzornikIgre.tipaIgralcev.put(igra.igralec1, igra.igralec1.tip);
		NadzornikIgre.tipaIgralcev.put(igra.igralec2,igra.igralec2.tip);
		
		NadzornikIgre.igraPanel = this;
		NadzornikIgre.novaIgra(igra);
	}
	
	
	public void refreshPanel() {
		int stanje = igra.getStanje();
		if ( stanje == Igra.ZMAGA1) {
			igralecLabel.setText(igra.igralec1.toString());
			stanjeLabel.setText(zmagaStanje);
		} else if ( stanje == Igra.ZMAGA2) {
			igralecLabel.setText(igra.igralec2.toString());
			stanjeLabel.setText(zmagaStanje);
		} else if ( stanje == Igra.IGRA) {
			igralecLabel.setText(igra.igralecNaPotezi.toString());
			stanjeLabel.setText(potezaStanje);
		}
		platno.refreshPlatno();
	}
	
	public class Platno extends JPanel implements MouseListener, ComponentListener {
		private static final long serialVersionUID = 1L;
		
		private ArrayList<Sestkotnik> sestkotniki;
		private Sestkotnik hex;
		private int N;
		private int height;
		private int width;
		private int odmikX;
		private int odmikY;
		
		private int[][] matrika;
		
		private int igralnoX;
		private int igralnoY;
		
		private int radij;
		private double visina;

		
		private Okno okno;
		private ArrayList<Polygon> polygoni;
		
		public Platno() {
			polygoni = new ArrayList<Polygon>();
			sestkotniki = new ArrayList<Sestkotnik>();
			height = 550;
			width = 900;
			initialize();
			this.addMouseListener(this);
			this.addComponentListener(this);
		}
		
		private void izracunajVrednosti() {
			System.out.println("racunamVrednosti");
			N = igra.velikost;
			
			if (N > 6) {
				odmikY = 40;
				odmikX = 40;
			} else {
				odmikY = 80;
				odmikX = 40;
			}
			
			while(true) {
				odmikY += 5;
				igralnoY = height - 2 * odmikY;
				radij = (2 * igralnoY) / (3 * N);
				igralnoX = (int) ((Math.sqrt(3) * radij * (3 * N - 1)) / 2);
				if ( (width - igralnoX - odmikX) > 0 ) {
					odmikX = (width - igralnoX) / 2;
					break;
				}
			}
			visina = Math.sqrt(3) * radij / 2;
		}
		
		private void initialize() {
			if (igra != null) {
				
				izracunajVrednosti();
				
				for(int i = 0; i < N; i++) {
					for (int j = 0; j < N; j++) {
						Koordinati koordinati = new Koordinati(i,j);
						int vrednost = igra.plosca.get(i).get(j);
						
						double x = odmikX + N * visina - i * visina + j * 2* visina;
			
						double y = odmikY - radij + igralnoY - i * 3 * radij / 2;
						sestkotniki.add(new Sestkotnik(x,y,radij,koordinati,igra.getIgralecBarva(vrednost)));
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
			Color igralec1 = igra.getIgralecBarva(1);
			Color igralec2 = igra.getIgralecBarva(2);
			
			for (Sestkotnik hex: sestkotniki) {
				int y = hex.getKoordinati().getY();
				int x = hex.getKoordinati().getX();
				if (y == 0){
					g.setColor(igralec2);
					g.drawPolygon(hex.getOuterLine(4));
					g.fillPolygon(hex.getOuterLine(4));
					g.drawPolygon(hex.getOuterLine(5));
					g.fillPolygon(hex.getOuterLine(5));
				}
				if (y == igra.velikost - 1) {
					g.setColor(igralec2);
					g.drawPolygon(hex.getOuterLine(1));
					g.fillPolygon(hex.getOuterLine(1));
					g.drawPolygon(hex.getOuterLine(2));
					g.fillPolygon(hex.getOuterLine(2));
				}
				if (x == 0){
					g.setColor(igralec1);
					g.drawPolygon(hex.getOuterLine(5));
					g.fillPolygon(hex.getOuterLine(5));
					g.drawPolygon(hex.getOuterLine(6));
					g.fillPolygon(hex.getOuterLine(6));
				}
				if (x == igra.velikost - 1) {
					g.setColor(igralec1);
					g.drawPolygon(hex.getOuterLine(3));
					g.fillPolygon(hex.getOuterLine(3));
					g.drawPolygon(hex.getOuterLine(2));
					g.fillPolygon(hex.getOuterLine(2));
				}
				Polygon osnovniHex = hex.getHexagon();
				g.setColor(Color.black);
				g.drawPolygon(osnovniHex);
				g.fillPolygon(osnovniHex);
				g.setColor(hex.getBarva());
				polygoni.add(hex.getHexagon());
				
				Polygon smallerHex = hex.getResizedHexagon(-2);
				g.drawPolygon(smallerHex);
				g.fillPolygon(smallerHex);
			}
			g.dispose();
		}

		public void mouseClicked(MouseEvent e) {
			Point p = e.getPoint();
			if (!igra.konecIgre()) {
				if(NadzornikIgre.clovekNaPotezi) {
					for(Sestkotnik hex:sestkotniki) {
			        	if (hex.getHexagon().contains(p)) {
			        		Koordinati poteza = hex.getKoordinati();
			        		NadzornikIgre.clovekovaPoteza(poteza);
			        		break;
			        	}
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

		public void componentResized(ComponentEvent e) {
			System.out.println("delamResize");
			this.removeAll();
			sestkotniki.clear();
			this.height = this.getHeight();
			this.width = this.getWidth();
			initialize();
			repaint();
		}

		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

	}
	
}
