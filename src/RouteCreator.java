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
	Map<String, Point> routeNames;
	Map<Point,List<Street>> map;
	int numGroups;
	
	public RouteCreator(String mapFilename, String routeFilename) {
		mapNames = new HashMap<String, Point>();
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
	
	public Map<String, Point> parseRoute(String filename) {
		HashMap<String, Point> route = new HashMap<String, Point>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			numGroups = Integer.parseInt(reader.readLine().split(",")[0]);
			while (reader.ready()) {
				String line = reader.readLine();
				String[] tokens = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
				if (tokens[0].charAt(0) == '"') {
					tokens[0] = tokens[0].substring(1, tokens[0].length() - 1);
				}
				Point curr = mapNames.get(tokens[0]);
				if (curr == null) {
					System.out.println("Location \"" + tokens[0]
							+ "\" not found in map. Continuing with " +
							"other locations.");
					continue;
				}
				if (tokens.length > 1) {
					curr.addSpecificLocation(tokens[1]);
				}
				route.put(tokens[0], curr);
				
			}
			reader.close();
			route.put("Platt Student Performing Arts House", 
					mapNames.get("Platt Student Performing Arts House"));
		} catch (FileNotFoundException e) {
			System.out.println("Incorrect route filename");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Improperly formatted route file");
			System.exit(1);
		}
		return route;
	}
	
		
	public Set<List<Point>> createRoutes() {
		HashMap<Point, List<Street>> routeMap = new HashMap<Point,List<Street>>();
		
		for (Point p : routeNames.values()) {
			LinkedList<Street> streets = new LinkedList<Street>();
			for (Point other : routeNames.values()) {
				if (other == p) continue;
				Street s = new Street(p, other);
				streets.add(s);
				List<Street> oppList = routeMap.get(other);
				if (oppList == null) {
					oppList = new LinkedList<Street>();
				}
				oppList.add(s);
				routeMap.put(other, oppList);
			}
			routeMap.put(p, streets);
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
		int i = 1;
		System.out.println("--------");
		for (List<Point> l : allRoutes) {
			System.out.println("Route " + i + ":");
			for (Point p : l) {
				System.out.print(p.getName());
				if (p.getSpecificLocations().size() > 0) { 
					System.out.print(" : ");
				}
				for (String s : p.getSpecificLocations()) {
					System.out.print(s + ", ");
				}
				System.out.println();
			}
			System.out.println("--------");
			i++;
		}

	}
}
