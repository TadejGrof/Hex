package koordinati;

public class Koordinati {
	private static int x;
	private static int y;
	
	public Koordinati(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public static int getX() { 
		return x; 
	}

	public static int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Koordinati [x=" + x + ", y=" + y + "]";
	}
	
	@Override 
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || this.getClass() != o.getClass()) return false;
		Koordinati k = (Koordinati) o;
		return this.x == k.x && this.y == k.y;
	}
	
	@Override
	public int hashCode () {
		int x = this.x ; int y = this.y;
		return (x + y) * (x + y + 1) / 2 + y;
	}
}
