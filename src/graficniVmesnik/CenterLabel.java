package graficniVmesnik;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;



// class, ki pomaga pri izgledu okna...Vbistvu nam samo poenostavi nastavljanje fonta 
// pri vseh uporabljenih JLabelih v grafičnem vmesniku

public class CenterLabel extends JLabel{
	private static final long serialVersionUID = 1L;
	private int ratio;
	
	public CenterLabel() {
		initialize();
	}
	
	public CenterLabel(String text) {
		setText(text);
		initialize();
	}

	
	// Nastavi ratio
	public void setRatio(int ratio) {
		this.ratio = ratio;
		setFont();
	}
	
	private void initialize() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
		ratio = 3;
		setFont();	
	}
	
	//Nastavi font glede na podan ratio.
	private void setFont() {
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100/ratio));
	}
	
}
