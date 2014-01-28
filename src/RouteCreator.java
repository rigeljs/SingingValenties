import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class RouteCreator {

	Map<String, Point> mapNames;
	List<String> routeNames;
	Map<Point,List<Street>> map;
	int numGroups;
	
	public RouteCreator(String mapFilename, String routeFilename) {
		this.map = mapToPoints(parseMap(mapFilename));
		this.routeNames = parseRoute(routeFilename);
		
	}

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
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Improperly formatted map file");
			System.exit(1);
		}
		return mapTokens;
	}
	
	public List<String> parseRoute(String filename) {
		LinkedList<String> route = new LinkedList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			numGroups = Integer.parseInt(reader.readLine());
			while (reader.ready()) {
				String line = reader.readLine();
				route.add(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect route filename");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Improperly formatted route file");
			System.exit(1);
		}
		return route;
	}
	
	public Map<Point, List<Street>> mapToPoints(HashMap<String, Double[]> mapTokens) {
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
		return map;
	}
		
	public Set<List<Point>> createRoutes() {
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
		m.splitPath(numGroups, minTotalRoute, new Stack<List<Point>>());
		Set<List<Point>> bestSplit = m.getBestSplit();
		return bestSplit;
	}

	public static void main(String[] args) {
		RouteCreator r = new RouteCreator(args[0],args[1]);
		Set<List<Point>> allRoutes = r.createRoutes();
		int i = 0;
		for (List<Point> l : allRoutes) {
			System.out.println("Route " + i + ":");
			for (Point p : l) {
				System.out.println(p.getName());
			}
		}

	}
}
