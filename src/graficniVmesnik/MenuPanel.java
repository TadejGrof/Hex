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
import javax.swing.JPanel;
import javax.swing.JTextField;
import logika.Igra;
import logika.Igralec;

import static javax.swing.JOptionPane.showMessageDialog;

public class MenuPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	
	private Okno okno;
	private JPanel menuBar;
	private JPanel nastavitve;
	private IgralecPanel igralec1Panel;
	private IgralecPanel igralec2Panel;
	private VelikostInput velikostInput;
	
	private CenterLabel hexLabel;
	
	private GridBagConstraints gbc;
	private JPanel gumbiPanel;

	public MenuPanel(Okno okno) {
		this.okno = okno;
		
		setLayout(new GridBagLayout());
		
		gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 0.15;
		
		menuBar = new JPanel(new GridBagLayout());
		add(menuBar,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		
		hexLabel = new CenterLabel("Hex");
		hexLabel.setRatio(2);
		menuBar.add(hexLabel, gbc);
		
		velikostInput = new VelikostInput();
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.weightx = 0.5;
		gbc.weighty = 1.0;
		menuBar.add(velikostInput,gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = 0.75;
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
	
	private class VelikostInput extends JPanel {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private CenterLabel velikostLabel;
		private JTextField velikostInput;
		
		
		public VelikostInput() {
			setLayout(new GridBagLayout());
			
			velikostLabel = new CenterLabel("Izberi velikost:");
			velikostInput = new JTextField("11");
			velikostInput.setHorizontalAlignment(JTextField.CENTER);
			velikostInput.setFont(new Font("SansSerif", Font.BOLD, 30));
			
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 0.7;
			add(velikostLabel,gbc);
			
			gbc.gridx = 1;
			gbc.gridy = 0;
			gbc.weightx = 1.0;
			gbc.weighty = 0.3;
			add(velikostInput,gbc);
		}
		
		public int getValue() {
			String stevilo = velikostInput.getText();
			if (stevilo == null || stevilo.length() == 0) {
	           return 0;
	        }
	        for (char c : stevilo.toCharArray()) {
	            if (!Character.isDigit(c)) {
	                return 0;
	            }
	        }
			return Integer.parseInt(stevilo);
		}
		
		public void setValue(int stevilo) {
			velikostInput.setText(String.valueOf(stevilo));
		}
	}
	
	private class IgralecPanel extends JPanel{
		private static final long serialVersionUID = 1L;

		private int index;
		
		private final int labelTitleRatio = 3;
		private final int labelNastavitveRatio = 4;
		
		private CenterLabel titleLabel;
		private CenterLabel barvaLabel;
		
		private TipInput tipPanel;
		private ImeInput imePanel;
		private BarvaInput barvaPanel;
		
		private JPanel barve;
		
		public IgralecPanel(int index) {
			this.index = index;
			
			setLayout(new GridLayout(0,1));
			
			titleLabel = new CenterLabel("Igralec" + String.valueOf(index) + ":");
			titleLabel.setRatio(labelTitleRatio);
			add(titleLabel);
			
			tipPanel = new TipInput();
			add(tipPanel);
			
			imePanel = new ImeInput();
			add(imePanel);
			
			barvaPanel = new BarvaInput();
			add(barvaPanel);
			
			barvaLabel = new CenterLabel("Izberi:");
			barvaLabel.setRatio(labelNastavitveRatio);
			add(barvaLabel);
			
			barve = new JPanel(new GridLayout(2,5));
			barve.add(new BarvaButton(Color.RED));
			barve.add(new BarvaButton(Color.BLUE));
			barve.add(new BarvaButton(Color.GREEN));
			barve.add(new BarvaButton(Color.YELLOW));
			barve.add(new BarvaButton(Color.ORANGE));
			barve.add(new BarvaButton(Color.GRAY));
			barve.add(new BarvaButton(Color.MAGENTA));
			barve.add(new BarvaButton(Color.PINK));
			barve.add(new BarvaButton(Color.CYAN));
			barve.add(new BarvaButton(Color.BLACK));
			add(barve);
			
		}
		
		public Igralec getIgralec() {
			String ime = imePanel.getValue();
			int tip = tipPanel.getValue();
			Color barva = barvaPanel.getValue();
			return new Igralec(ime,barva,tip);
		}
		
		
		private class TipInput extends JPanel {
			private static final long serialVersionUID = 1L;
			
			private CenterLabel tipLabel;
			private DefaultListCellRenderer listRenderer;
			private JComboBox<String> tipBox;
			private String[] tipi = { "Igralec", "Racunalnik - Lahko", "Racunalnik - Srednje", "Racunalnik - Težko" };
			
			public TipInput() {
				setLayout(new GridLayout(1,2));
				tipLabel = new CenterLabel("Tip igralca:");
				tipLabel.setRatio(labelNastavitveRatio);
				tipBox = new JComboBox<String>(tipi);
				listRenderer = new DefaultListCellRenderer();
			    listRenderer.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
			    tipBox.setRenderer(listRenderer);

				add(tipLabel);
				add(tipBox);
			}
			
			public int getValue() {
				int index = tipBox.getSelectedIndex();
				if (index == 0) {
					return Igralec.IGRALEC;
				} else if(index == 1) {
					return Igralec.LAHEK_RACUNALNIK;
				} else if(index == 2) {
					return Igralec.SREDNJI_RACUNALNIK;
				} else if(index == 3) {
					return Igralec.TEZEK_RACUNALNIK;
				}
				return Igralec.IGRALEC;
			}
		}
		
		private class ImeInput extends JPanel  {
			private static final long serialVersionUID = 1L;
			
			private CenterLabel imeLabel;
			private JTextField imeInput;
			
			public ImeInput() {
				setLayout(new GridLayout(1,2));
				imeLabel = new CenterLabel("Ime:");
				imeLabel.setRatio(labelNastavitveRatio);
				imeInput = new JTextField();
				imeInput.setHorizontalAlignment(JTextField.CENTER);
				imeInput.setFont(new Font("SansSerif", Font.BOLD, 25));
				add(imeLabel);
				add(imeInput);
			}
			
			public String getValue() {
				String ime = imeInput.getText();
				if ( ime.isBlank()) {
					int tip = tipPanel.getValue();
					if (tip == Igralec.IGRALEC) {
						return "Igralec" + String.valueOf(index);
					} else if ( Igralec.jeRacunalnik(tip)) {
						return "Racunalnik" + String.valueOf(index);
					}
				}
				return ime;
			}
		}
		
		private class BarvaInput extends JPanel {
			private static final long serialVersionUID = 1L;
			
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
				barvaLabel.setRatio(labelNastavitveRatio);
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
			private static final long serialVersionUID = 1L;
			
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
			setText("Nova igra");
			addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int velikost = velikostInput.getValue();
					if ( velikost > 0) {
						Igra igra = new Igra(velikost);
						Igralec igralec1 = igralec1Panel.getIgralec();
						Igralec igralec2 = igralec2Panel.getIgralec();
						if (igralec1.toString().contentEquals(igralec2.toString())) {
							showMessageDialog(null,"Izberi razlicni imeni za igralca");
						} else if( igralec1.barva.equals(igralec2.barva)) {
							showMessageDialog(null,"Izberi razlicni barvi za igralca");
						} else  {
							igra.setIgralca(igralec1, igralec2);
							okno.novaIgra(igra);
						}
					} else {
						showMessageDialog(null,"Izberi ustrezno velikost igre");
						velikostInput.setValue(11);
					}
					
				}
			});
		}
		
	}
	
}
