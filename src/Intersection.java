import java.util.List;

/**
 * This class provides a representation for Intersections that relies on the
 * location of the intersection and the streets that form the intersection.
 *
 * @author CIS-121 Staff
 */
public class Intersection {
    
	private Point point;
	private List<Street> list;
    
	public Intersection(Point point, List<Street> list) {
		this.point = point;
		this.list = list;
	}
    
	/**
	 * Returns the point at which this intersection is located.
	 */
	public Point getLocation() {
		return point;
	}
    
	/**
	 * Returns the list of streets that pass through this intersection.
	 */
	public List<Street> getStreetList() {
		return list;
	}
    
	/**
	 * Sets the list of streets that pass through this intersection.
	 */
	public void setStreetList(List<Street> list) {
		this.list = list;
	}
    
	/**
	 * Determines whether this intersection is connected to another intersection.
	 * @param intersection to check for connectivity
	 * @return the street that links the two intersections, null if none.
	 */
	public Street isConnected(Intersection intersection) {
		Point p = intersection.getLocation();
		for (Street s : list) {
			if (s.getFirstPoint().equals(p) || s.getSecondPoint().equals(p))
				return s;
		}
		return null;
	}
    
	@Override
	public String toString() {
		return point.toString();
	}
}
