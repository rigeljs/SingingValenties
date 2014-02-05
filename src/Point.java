import java.util.LinkedList;
import java.util.List;

/**
 * For this assignment you can assume that every point denotes essentially an
 * intersection. More precisely, the location of an intersection is a point, and
 * all points will be represented by an intersection, as we have chosen a closed
 * set of points (a closed graph).
 *
 * @author CIS-121 Staff
 *
 */
public class Point {
	private double x, y;
    private String name;
    private List<String> specificLocations;
	/**
	 * Constructor for a Point that accepts arguments for all fields.
	 */
	public Point(double x, double y, String name) {
		this.x = x;
		this.y = y;
		this.name = name;
		this.specificLocations = new LinkedList<String>();
	}
    
	public void addSpecificLocation(String location) {
		specificLocations.add(location);
	}
	
	public List<String> getSpecificLocations() {
		return specificLocations;
	}
	
	public double getX() {
		return x;
	}
    
	public void setX(double x) {
		this.x = x;
	}
    
	public double getY() {
		return y;
	}
    
	public void setY(double y) {
		this.y = y;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
    
	@Override
	public String toString() {
		return x + "," + y;
	}
    
	/**
	 * Provides a method to test if two points are equal - only if their x and y
	 * values are the same.
	 */
	@Override
	public boolean equals(Object o) {
		Point p = (Point) o;
		return x == p.getX() && y == p.getY();
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}
}
