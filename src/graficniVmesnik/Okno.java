package graficniVmesnik;

import java.awt.BorderLayout;
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
	private Platno platno;
	private JPanel osnovno;
	private final Igra igra;
	private GridBagConstraints gbc;
	private JLabel igralecLabel;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Igra igra = new Igra();
					
					Okno okno = new Okno(igra);
					okno.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public Okno(Igra game) {	
		this.igra = game;
		
		frame = new JFrame();
		
		frame.setBounds(60, 60, 1000, 650);
		//frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		osnovno = new JPanel(new GridBagLayout());
		frame.add(osnovno);
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		
		JPanel menuBar = new JPanel(new GridLayout(1,0));
		igralecLabel = new JLabel("Igralec1",SwingConstants.CENTER);
		menuBar.add(igralecLabel);
		JButton nextButton = new JButton("Naslednja poteza");
		nextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!igra.konecIgre()) {
					igra.nakljucnaPoteza();
				}
				refresh();
			}
			
		});
		menuBar.add(nextButton);
		osnovno.add(menuBar,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.8;
		platno = new Platno(this,igra);
		osnovno.add(platno,gbc);
	
		
	}
	
	public void refresh() {
		if (igra.konecIgre()) {
			igralecLabel.setText("Konec igre");
		} else {
			igralecLabel.setText(igra.getIgralecNaPotezi().toString());
		}
		platno.refresh();
	}
}
