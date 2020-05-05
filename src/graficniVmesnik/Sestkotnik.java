package graficniVmesnik;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import koordinati.Koordinati;

public class Sestkotnik {
        private final int radius;
        
        private double x;
        private double y;

        private final Polygon hexagon;
        private Color barva;
        private Koordinati koordinati;
        
        public Sestkotnik(double x, double y, int radius, Koordinati koordinati, Color barva) {
            this.x = x;
            this.y = y;
            this.radius = radius;
            this.barva = barva;
            this.koordinati = koordinati;
            this.hexagon = createHexagon(0);
            
        }

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

        public int getRadius() {
            return radius;
        }
        
        public Koordinati getKoordinati() {
        	return koordinati;
        }

        public Polygon getHexagon() {
            return hexagon;
        }
        
        public Color getBarva() {
        	return barva;
        }
        
        public Polygon getSmallerHexagon(int zamik) {
        	return createHexagon(zamik);
        }
        

    }

