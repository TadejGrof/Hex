package graficniVmesnik;

import java.awt.CardLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import logika.Igra;

public class Okno {
	public JFrame frame;
	private MenuPanel menuPanel;
	private IgraPanel igraPanel;
	
	private CardLayout okna;
	
	private final String cardMenu = "menu";
	private final String cardIgra = "igra";
	
	// Požene grafični vmesnik
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Okno okno = new Okno();
					okno.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Okno() {	
		frame = new JFrame();
		
		//frame.setBounds(60, 20, 1000, 700);
		// Če hočemo manjše okno na začetku, odkomentiramo zgornjo in komentiramo spodnjo
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// med menujem in igro preklapjamo s pomočjo CardLayout-a
		okna = new CardLayout();
		frame.setLayout(okna);
		
		menuPanel = new MenuPanel(this);
		frame.add(menuPanel, cardMenu);
		
		igraPanel = new IgraPanel(this);
		frame.add(igraPanel, cardIgra);
	}
	
	// Nastavimo novo igro
	public void setIgra(Igra igra) {
		igraPanel.setIgra(igra);
		refresh();
	}
	
	// pokaze karto v CardLayout z ustreznim imenom (cardMenu ali cardIgra)
	public void pokazi(String cardName) {
		okna.show(frame.getContentPane(), cardName);
	}
	
	// osveži stanje okna glede na igro, na začetku ko je igra null pokaze menu na
	// katerem lahko usvarimo novo s funkcijo novaIgra kjer se spet poklice refresh()
	// in se pokaze igralna karta.
	public void refresh() {
		Igra igra = igraPanel.igra;
		if (igra != null) {
			pokazi("igra");
		} else {
			pokazi("menu");
		}
	}
}
