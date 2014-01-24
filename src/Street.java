/**
 * A representation for streets using the following characteristics: beginning
 * and ending points, street name, and the street ID.
 *
 * @author CIS-121 Staff
 *
 */
public class Street {
    
	private Point firstPoint, secondPoint;
	private String name;
	private int id;
    
	public Street(int id, Point firstPoint, Point secondPoint, String name) {
		this.id = id;
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
		this.name = name;
	}
    
	public void setId(int id) {
		this.id = id;
	}
    
	public int getId() {
		return id;
	}
    
	public void setName(String name) {
		this.name = name;
	}
    
	public String getName() {
		return name;
	}
    
	public void setPoints(Point firstPoint, Point secondPoint) {
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
	}
    
	public Point getFirstPoint() {
		return firstPoint;
	}
    
	public Point getSecondPoint() {
		return secondPoint;
	}
    
	/**
	 * Returns the distance of this street using the distance formula with the
	 * street's beginning and ending locations.
	 */
	public Double getDistance() {
		double x = Math.pow(secondPoint.getX() - firstPoint.getX(), 2)
        + Math.pow(secondPoint.getY() - firstPoint.getY(), 2);
		return Math.sqrt(x);
	}
}
