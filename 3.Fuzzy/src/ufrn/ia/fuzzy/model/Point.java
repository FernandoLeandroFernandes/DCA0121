package ufrn.ia.fuzzy.model;

public class Point {

	private float x;
	
	private float y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float x() {
		return x;
	}

	public void x(float x) {
		this.x = x;
	}

	public float y() {
		return y;
	}

	public void y(float y) {
		this.y = y;
	}
	
	
	@Override
	public String toString() {
		return String.format("(%.1f,%.1f) ", this.x(), this.y());
	}
	
}
