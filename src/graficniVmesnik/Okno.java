package graficniVmesnik;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import logika.Igra;

public class Okno {
	public JFrame frame;
	private MenuPanel menuPanel;
	private IgraPanel igraPanel;
	private JPanel konecPanel;
	
	private CardLayout okna;
	
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
		frame.setBounds(60, 20, 1000, 700);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		okna = new CardLayout();
		frame.setLayout(okna);
		
		menuPanel = new MenuPanel(this);
		frame.add(menuPanel,"menu");
		
		igraPanel = new IgraPanel(this);
		frame.add(igraPanel, "igra");
	
		
		konecPanel = new JPanel();
		frame.add(konecPanel, "konec");
		
	}
	
	public void novaIgra(Igra igra) {
		igraPanel.setIgra(igra);
		refresh();
	}
	
	public void pokaziMenu() {
		okna.show(frame.getContentPane(), "menu");
	}
	
	public void refresh() {
		Igra igra = igraPanel.getIgra();
		if (igra != null) {
			if ( igra.konecIgre()) {
				okna.show(frame.getContentPane(),"konec");
			} else {
				okna.show(frame.getContentPane(), "igra");
			}
		} else {
			pokaziMenu();
		}
	}
}
