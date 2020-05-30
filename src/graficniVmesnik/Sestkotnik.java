package graficniVmesnik;

import java.awt.Color;
import java.awt.Polygon;
import splosno.Koordinati;


// class, ki pomaga pri risanju lepih šestkotnikov.
public class Sestkotnik {
        private final int radius;
        
        private double x;
        private double y;

        public final Polygon hexagon;
        public Color barva;
        public Koordinati koordinati;
        
        public Sestkotnik(double x, double y, int radius, Koordinati koordinati, Color barva) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.barva = barva;
            this.koordinati = koordinati;
            this.hexagon = createHexagon(0);
            
        }

        // ustvari Polygon, ki predstavlja šestkotnik glede na podane podatke o središču
        // in radiju. Za argument sprejme zamik, ki nam omogoča to, da lahko ustvarimo,
        // dva podobna šestkotnika, ki ju damo enega na drugega, da potem izgleda kot
        // da ima prvi rob širine podanega zamika
        private Polygon createHexagon(int zamik) {
            Polygon polygon = new Polygon();

            for (int i = 0; i < 6; i++) {
                int xval = (int) (x + (radius - zamik)
                        * Math.sin(i * 2 * Math.PI / 6D));
                int yval = (int) (y + (radius - zamik)
                        * Math.cos(i * 2 * Math.PI / 6D));
                polygon.addPoint(xval, yval);
            }
            return polygon;
        }
        
        public Polygon getOuterLine(int i) {
        	int debelina = 10;
        	Polygon polygon = new Polygon();
        	int xval = (int) (x + (radius)
                    * Math.sin(i * 2 * Math.PI / 6D));
        	int yval = (int) (y + (radius)
                    * Math.cos(i * 2 * Math.PI / 6D));
            polygon.addPoint(xval,yval);
            xval = (int) (x + (radius + debelina)
                    * Math.sin(i * 2 * Math.PI / 6D));
        	yval = (int) (y + (radius + debelina)
                    * Math.cos(i * 2 * Math.PI / 6D));
            polygon.addPoint(xval,yval);
            xval = (int) (x + (radius + debelina)
                    * Math.sin((i + 1) * 2 * Math.PI / 6D));
        	yval = (int) (y + (radius + debelina)
                    * Math.cos((i + 1) * 2 * Math.PI / 6D));
            polygon.addPoint(xval,yval);
            xval = (int) (x + (radius)
                    * Math.sin((i + 1) * 2 * Math.PI / 6D));
        	yval = (int) (y + (radius)
                    * Math.cos((i + 1) * 2 * Math.PI / 6D));
            polygon.addPoint(xval,yval);
        	return polygon;
        }
        
        public Polygon getResizedHexagon(int zamik) {
        	return createHexagon(-zamik);
        }
        

    }

