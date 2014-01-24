import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Provides functionality to parse a data file into an adjacency list graph
 * representation.
 *
 * @author CIS-121 Staff
 *
 */
public class GraphMaker {
    
	private Map<Point, List<Street>> graph = new HashMap<Point, List<Street>>();
	private Point startPoint;
	private Point endPoint;
    
	/**
	 * Returns the start point of the identified path, which is initially read
	 * from the file's second line. This point will be used in the ShortestPath
	 * method.
	 */
	public Point getStartPoint() {
		return startPoint;
	}
    
	/**
	 * Returns the ending point of the identified path, which is initially read
	 * from second line of the file. This point will be used in the ShortestPath
	 * method.
	 */
	public Point getEndPoint() {
		return endPoint;
	}
    
	/**
	 * Returns the ending point of the identified path, which is initially read
	 * from the file's second line. This point will be used in the ShortestPath
	 * method.
	 *
	 * @throws IOException
	 *             - Because use of FileReader and its methods could result in
	 *             IOExceptions
	 */
	public Map<Point, List<Street>> parse(String inFile) throws IOException {
		BufferedReader f = new BufferedReader(new FileReader(inFile));
        
		// Parse road data file and create a graph
		f.readLine(); // ignore the first line of the file
		StringTokenizer st = new StringTokenizer(f.readLine(), ",");
        
		// Start and end points for shortest path
		double x1 = Double.parseDouble(st.nextToken());
		double y1 = Double.parseDouble(st.nextToken());
		double x2 = Double.parseDouble(st.nextToken());
		double y2 = Double.parseDouble(st.nextToken());
		startPoint = new Point(x1, y1);
		endPoint = new Point(x2, y2);
        
		// Construct graph
		while (f.ready()) {
			st = new StringTokenizer(f.readLine(), ",");
			int id = Integer.parseInt(st.nextToken());
			x1 = Double.parseDouble(st.nextToken());
			y1 = Double.parseDouble(st.nextToken());
			x2 = Double.parseDouble(st.nextToken());
			y2 = Double.parseDouble(st.nextToken());
			String name = st.nextToken();
			Point p1 = new Point(x1, y1);
			Point p2 = new Point(x2, y2);
			Street street = new Street(id, p1, p2, name);
            
			List<Street> list;
			if (graph.containsKey(p1)) {
				list = graph.get(p1);
				list.add(street);
				graph.put(p1, list);
			} else {
				list = new LinkedList<Street>();
				list.add(street);
				graph.put(p1, list);
			}
			if (graph.containsKey(p2)) {
				list = graph.get(p2);
				list.add(street);
				graph.put(p2, list);
			} else {
				list = new LinkedList<Street>();
				list.add(street);
				graph.put(p2, list);
			}
		}
        
		f.close();
		return graph;
	}
}
