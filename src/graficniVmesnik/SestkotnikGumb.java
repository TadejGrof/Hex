package graficniVmesnik;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;

import javax.swing.JButton;

public class SestkotnikGumb extends JButton{
	private static final long serialVersionUID = 1L;
	
	private Shape hexagon;
	float size;
	float width, height;
	
	public SestkotnikGumb(float centerX, float centerY, float size){
	    Polygon p = new Polygon();
	    this.size = size;
	    p.reset();
	    for(int i=0; i<6; i++){
	        float angleDegrees = (60 * i) - 30;
	        float angleRad = ((float)Math.PI / 180.0f) * angleDegrees;

	        float x = centerX + (size * (float)Math.cos(angleRad));
	        float y = centerY + (size * (float)Math.sin(angleRad));

	        p.addPoint((int)x,(int)y);
	    }

	    width = (float)Math.sqrt(3) * size;
	    height = 2.0f * size;
	    hexagon = p;
	}

	public void paintBorder(Graphics g){
	    ((Graphics2D)g).draw(hexagon);
	}

	public void paintComponent(Graphics g){
	    ((Graphics2D)g).draw(hexagon);
	}


}
