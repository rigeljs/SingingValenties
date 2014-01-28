/**
 * A representation for streets using the following characteristics: beginning
 * and ending points, street name, and the street ID.
 *
 * @author CIS-121 Staff
 *
 */
public class Street {
    
	private Point firstPoint, secondPoint;
    
	public Street(Point firstPoint, Point secondPoint) {
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;
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
