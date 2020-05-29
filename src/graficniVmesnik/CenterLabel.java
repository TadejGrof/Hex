package graficniVmesnik;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


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

	public void setRatio(int ratio) {
		this.ratio = ratio;
		setFont();
	}
	
	private void initialize() {
		this.setHorizontalAlignment(SwingConstants.CENTER);
		ratio = 3;
		setFont();	
	}
	
	private void setFont() {
		setFont(new Font(Font.SANS_SERIF, Font.BOLD, 100/ratio));
	}
	
}
