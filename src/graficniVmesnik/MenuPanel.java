package graficniVmesnik;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import logika.Igra;
import logika.Igralec;

public class MenuPanel extends JPanel{
	private Okno okno;
	private JPanel menuBar;
	private JPanel nastavitve;
	private IgralecPanel igralec1Panel;
	private IgralecPanel igralec2Panel;
	private GridBagConstraints gbc;
	private JPanel gumbiPanel;
	private JButton novoButton;
	
	public MenuPanel(Okno okno) {
		this.okno = okno;
		
		setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		
		menuBar = new JPanel();
		menuBar.add(new CenterLabel("Nova igra:"));
		add(menuBar,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.8;
		nastavitve = new JPanel(new GridLayout(1,2));
		
		igralec1Panel = new IgralecPanel(1);
		igralec2Panel = new IgralecPanel(2);
		
		nastavitve.add(igralec1Panel);
		nastavitve.add(igralec2Panel);
		
		add(nastavitve,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 0.1;
		
		gumbiPanel = new JPanel(new GridLayout(1,0));
		gumbiPanel.add(new JPanel());
		gumbiPanel.add(new NovoButton());
		gumbiPanel.add(new JPanel());
		
		add(gumbiPanel,gbc);
		
	}
	
	private class IgralecPanel extends JPanel{
		private int index;
		
		private TipInput tipPanel;
		private ImeInput imePanel;
		private BarvaInput barvaPanel;
		
		private JPanel barve;
		
		public IgralecPanel(int index) {
			this.index = index;
			
			setLayout(new GridLayout(0,1));
			
			tipPanel = new TipInput();
			add(tipPanel);
			
			imePanel = new ImeInput();
			add(imePanel);
			
			barvaPanel = new BarvaInput();
			add(barvaPanel);
			
			add(new CenterLabel("Izberi:"));
			
			barve = new JPanel(new GridLayout(1,0));
			barve.add(new BarvaButton(Color.RED));
			barve.add(new BarvaButton(Color.BLUE));
			barve.add(new BarvaButton(Color.GREEN));
			barve.add(new BarvaButton(Color.YELLOW));
			barve.add(new BarvaButton(Color.ORANGE));
			barve.add(new BarvaButton(Color.GRAY));
			add(barve);
			
		}
		
		public Igralec getIgralec() {
			String ime = imePanel.getValue();
			int tip = tipPanel.getValue();
			Color barva = barvaPanel.getValue();
			return new Igralec(ime,barva,tip);
		}
		
		
		private class TipInput extends JPanel {
			private CenterLabel tipLabel;
			private DefaultListCellRenderer listRenderer;
			private JComboBox<String> tipBox;
			private String[] tipi = { "Igralec", "Racunalnik - Lahko", "Racunalik - Srednje", "Racunalnik - Težko" };
			
			public TipInput() {
				setLayout(new GridLayout(1,2));
				tipLabel = new CenterLabel("Tip igralca:");
				tipBox = new JComboBox<String>(tipi);
				listRenderer = new DefaultListCellRenderer();
			    listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
			    tipBox.setRenderer(listRenderer);

				add(tipLabel);
				add(tipBox);
			}
			
			public int getValue() {
				return 0;
			}
		}
		
		private class ImeInput extends JPanel  {
			private CenterLabel imeLabel;
			private JTextField imeInput;
			
			public ImeInput() {
				setLayout(new GridLayout(1,2));
				imeLabel = new CenterLabel("Ime:");
				imeInput = new JTextField();
				add(imeLabel);
				add(imeInput);
			}
			
			public String getValue() {
				String ime = imeInput.getText();
				if ( ime.isBlank()) {
					int tip = tipPanel.getValue();
					if (tip == Igralec.IGRALEC) {
						return "Igralec" + String.valueOf(index);
					} else if ( tip == Igralec.RACUNALNIK) {
						return "Racunalnik" + String.valueOf(index);
					}
				}
				return ime;
			}
		}
		
		private class BarvaInput extends JPanel {
			private CenterLabel barvaLabel;
			private JPanel barvaInput;
			private Color barva;
			
			public BarvaInput() {
				if (index == 1) {
					barva = Color.RED;
				} else {
					barva = Color.BLUE;
				}
				setLayout(new GridLayout(1,2));
				barvaLabel = new CenterLabel("Barva:");
				barvaInput = new JPanel();
				barvaInput.setBackground(barva);
				add(barvaLabel);
				add(barvaInput);
			}
			
			public void setValue(Color color) {
				this.barva = color;
				barvaInput.setBackground(color);
				
			}
			public Color getValue() {
				return barva;
			}
		}
		
		private class BarvaButton extends JButton{
			private Color barva;
			
			public BarvaButton(Color color) {
				barva = color;
				setBackground(barva);
				addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						barvaPanel.setValue(barva);
					}
				});
			}
		}
	}
	
	private class NovoButton extends JButton{
		private static final long serialVersionUID = 1L;

		public NovoButton (){
			setText("Nova Igra");
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Igra igra = new Igra();
					Igralec igralec1 = igralec1Panel.getIgralec();
					Igralec igralec2 = igralec2Panel.getIgralec();
					igra.setIgralca(igralec1, igralec2);
					if (igralec1.toString().contentEquals(igralec2.toString())) {
						System.out.println("Izberi razlicni imeni za igralca");
					} else if( igralec1.getBarva().equals(igralec2.getBarva())) {
						System.out.println("Izberi razlicni barvi za igralca");
					} else  {
						igra.setIgralca(igralec1, igralec2);
						okno.novaIgra(igra);
					}
				}
			});
		}
		
	}
	
}
