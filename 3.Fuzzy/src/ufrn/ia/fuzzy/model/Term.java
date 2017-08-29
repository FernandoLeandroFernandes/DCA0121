package ufrn.ia.fuzzy.model;

import java.util.ArrayList;
import java.util.Arrays;

public class Term {

	private String name;
	
	private ArrayList<Point> points = new ArrayList<>();

	public Term(String name) {
		this.name = name;
	}
	
	public Term(String name, Point ... points) {
		
		this.name = name;
		
		if (points != null && points.length > 0) {
			this.points.addAll(Arrays.asList(points));
		}
	}
	
	public void addPoint(Point point) {
		
		if (point != null) {
			this.points.add(point);
		}
	}
	
	public void removePoint(Point point) {
		
		if (point != null) {
			this.points.remove(point);
		}
	}
	
	@Override
	public String toString() {
		
		String print = "TERM ".concat(this.name).concat(" := ");
		for (Point point: points) {
			print.concat(point.toString());
		}
		print.concat(";");
		
		return print;
	}
}
