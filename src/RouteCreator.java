import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class RouteCreator {

	HashMap<String, Point> mapNames;
	HashMap<Point,List<Street>> map;

	public HashMap<String, Double[]> parseMap(String filename) {
		HashMap<String, Double[]> mapTokens = new HashMap<String, Double[]>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			while (reader.ready()) {
				String line = reader.readLine();
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (tokens[0].charAt(0) == '"') {
					tokens[0] = tokens[0].substring(1, tokens[0].length() - 1);
				}
				mapTokens.put(tokens[0],
						new Double[] { Double.parseDouble(tokens[1]),
								Double.parseDouble(tokens[2]) });
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect map filename");
		} catch (IOException e) {
			System.out.println("Incorrect map filename");
		}
		return mapTokens;
	}
	
	public void mapToPoints(HashMap<String, Double[]> mapTokens) {
		HashMap<Point, List<Street>> map = new HashMap<Point, List<Street>>();
		for (String name : mapTokens.keySet()) {
			Point p = new Point(mapTokens.get(name)[0],mapTokens.get(name)[1], name);
			mapNames.put(name, p);
			LinkedList<Street> streets = new LinkedList<Street>();
			for (Point other : map.keySet()) {
				if (other == p) continue;
				Street s = new Street(p, other);
				streets.add(s);
				List<Street> oppList = map.get(other);
				oppList.add(s);
				map.put(other, oppList);
			}
			map.put(p, streets);
		}
		this.map = map;
	}
	
	public Set<List<Point>> createRoutes(List<String> routeNames) {
		HashMap<Point, List<Street>> routeMap = new HashMap<Point,List<Street>>();
		for (String name : routeNames) {
			if (!map.containsKey(name)) {
				System.out.println("Location \"" + name + "\" not found in map. Continuing with " +
						"other locations.");
				continue;
			}
			routeMap.put(mapNames.get(name), map.get(name));
			
		}
		MinSpanningTree m = new MinSpanningTree();
		MinSpanningTree.minST(routeMap);
		List<Point> minTotalRoute = m.travelingSalesman(routeMap,
				mapNames.get("Platt Student Performing Arts House"));
		m.splitPath(1, minTotalRoute, new Stack<List<Point>>());
		Set<List<Point>> bestSplit = m.getBestSplit();
		return bestSplit;
	}

	public static void main(String[] args) {
		RouteCreator m = new RouteCreator();

	}
}
