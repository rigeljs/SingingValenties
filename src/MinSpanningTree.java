import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class MinSpanningTree {
	
	Set<Set<List<Point>>> allSplits = new HashSet<Set<List<Point>>>();
	Point platt; 

	public List<Point> travelingSalesman(Map<Point, List<Street>> map, Point platt){
		List<Street> mst = minST(map);
		for (Street s : mst) {
			if (s.getFirstPoint().getName().equals("Platt Student Performing Arts House")){
				platt = s.getFirstPoint();
				break;
			}
		}
		if (platt == null) {
			System.out.println("Map must include Platt");
			System.exit(1);
		}
		this.platt = platt;
		HashSet<Point> discovered = new HashSet<Point>();
		Stack<Point> dfsStack = new Stack<Point>();
		
		dfsStack.push(platt);
		LinkedList<Point> path = new LinkedList<Point>();

		while (!dfsStack.isEmpty()){
			Point p = dfsStack.pop();
			if (!discovered.contains(p)) {
				discovered.add(p);
				path.add(p);
				for (Street s : map.get(p)) {
						dfsStack.push(getOpposite(p,s));
				}
			}
		}
		
		//remove platt
		return path.subList(1, path.size() - 1);
	}
	
	public void splitPath(int n, List<Point> original, Stack<List<Point>> allSt) {
		if (n == 1) {
			allSt.push(original);
			allSplits.add(new HashSet<List<Point>>(allSt));
			allSt.pop();
			return;
		}
		
		for (int i = 0; i < original.size(); i++) {
			List<Point> firstSplit = original.subList(0, i);
			allSt.push(firstSplit);
			splitPath(n - 1, original.subList(i, original.size()), allSt);
			allSt.pop();
		}
		return;
	}
	
	public Set<List<Point>> getBestSplit() {
		
		Set<List<Point>> best = null;
		double minVariance = Double.POSITIVE_INFINITY;
		System.out.println(allSplits.size());
		for (Set<List<Point>> split : allSplits) {
			double[] data = new double[split.size()];
			int i = 0;
			for (List<Point> path : split) {
				//add platt to end of path
				LinkedList<Point> withPlatt = new LinkedList<Point>(path);
				Point prev = platt;
				double time = 0;
				for (Point p : withPlatt) {
					//5 min per valentine
					time += 2;
					//find time to get between points
					double dist = gps2m(prev.getX(), prev.getY(), p.getX(), p.getY());
					//System.out.println(prev.getName() + " to "  + p.getName() + ": " + dist);
					//avg people walk at 5 km/h
					time += dist * 5;
					prev = p;
				}
				data[i] = time;
				withPlatt.add(platt);
				for (Point p : withPlatt) {
					System.out.println(p.getName());
				}
				System.out.println("Total time:" + data[i]);
				System.out.println("-----");
				i++;
			}
			double variance = getVariance(data);
			if (variance < minVariance) {
				minVariance = variance;
				best = split;
			}
		}
		return best;
	}
	
	private double getVariance(double[] data) {
		//find mean
		double mean = 0;
		for (int i = 0; i < data.length; i++) {
			mean += data[i];
		}
		mean /= data.length;
		
		//find variance
		double variance = 0;
		for (int i = 0; i < data.length; i++) {
			variance += (data[i] - mean) * (data[i] - mean);
		}
		variance /= (data.length - 1);
		
		return variance;
	}
	
	private double gps2m(double lat_a, double lng_a, double lat_b, double lng_b) {
	    double pk = (double) (180/3.14169);

	    double a1 = lat_a / pk;
	    double a2 = lng_a / pk;
	    double b1 = lat_b / pk;
	    double b2 = lng_b / pk;

	    double t1 = Math.cos(a1)*Math.cos(a2)*Math.cos(b1)*Math.cos(b2);
	    double t2 = Math.cos(a1)*Math.sin(a2)*Math.cos(b1)*Math.sin(b2);
	    double t3 = Math.sin(a1)*Math.sin(b1);
	    double tt = Math.acos(t1 + t2 + t3);

	    return 6366000*tt/1000;
	}
	
	
	/**
	 * Computes the minimum spanning tree using EAGER Prim's algorithm for the
	 * intersections that have been generated. Implementation should run in
	 * O(E*log(V)) or faster. Runtime: all steps run in O(E log V).
	 * 
	 * @throws IllegalArgumentException
	 *             - if argument map is null - if any element of the map (vertex
	 *             or edge) is null
	 */
	public static List<Street> minST(Map<Point, List<Street>> map) {
		if (map == null) {
			throw new IllegalArgumentException();
		}
		LinkedList<Street> mst = new LinkedList<Street>();
		LinkedList<IntersectionComp> vertexTree = new LinkedList<IntersectionComp>();
		PriorityQueue<IntersectionComp> pq = new PriorityQueue<IntersectionComp>();
		HashMap<Point, IntersectionComp> intmap = new HashMap<Point, IntersectionComp>();
		
		for (Point p : map.keySet()) {
			if (p == null || map.get(p) == null) { 
				throw new IllegalArgumentException();
			}
			IntersectionComp i = new IntersectionComp(p, map.get(p));
			intmap.put(p, i);
		}

		Point p = map.keySet().iterator().next();
		vertexTree.add(intmap.get(p));
		intmap.get(p).visit();
		IntersectionComp first = new IntersectionComp(p, map.get(p));
		pq.add(first);

		while (!pq.isEmpty()) {
			IntersectionComp i = pq.poll();
			if (i.edgeTo != null) {
				mst.add(i.edgeTo);
			}
			vertexTree.add(i);
			for (Street s : i.getStreetList()) {
				
				if (s == null) {
					throw new IllegalArgumentException();
				}
				
				//skip if edge is already in tree
				if (mst.contains(s)) {
					continue;
				}
				//get adjacent node
				Point oppPt = MinSpanningTree.getOpposite(i.getLocation(), s);
				IntersectionComp adj = intmap.get(oppPt);
				//mark and insert into priority queue if it hasn't been seen
				if (!adj.visited) {
					adj.setStreetTo(s);
					pq.add(adj);
					adj.visit();
				} else {
					
					// if the distance is smaller, replace it in the priority queue
					if (s.getDistance() < adj.weight && !vertexTree.contains(adj)) {
						//create copy of new node with smaller edge
						adj.setStreetTo(s);
						//update key
						pq.remove(adj);
						pq.add(adj);

					}

				}
			}
		}

		return mst;
	}

	public static Point getOpposite(Point p, Street s) {
		if (s.getFirstPoint().equals(p)) {
			return s.getSecondPoint();
		} else
			return s.getFirstPoint();
	}


	public static class IntersectionComp extends Intersection implements
			Comparable<IntersectionComp> {

		public double weight;
		public boolean visited;
		public Street edgeTo;

		public IntersectionComp(Intersection icomp) {
			super(icomp.getLocation(), icomp.getStreetList());
			this.visited = false;
			this.weight = Double.POSITIVE_INFINITY;
		}

		public IntersectionComp(Point p, List<Street> streetlist) {
			super(p, streetlist);
			this.visited = false;
			this.weight = Double.POSITIVE_INFINITY;
		}

		public void visit() {
			visited = true;
		}

		public void setStreetTo(Street s) {
			edgeTo = s;
			weight = s.getDistance();
		}

		public int compareTo(IntersectionComp arg0) {
			if (this.weight == arg0.weight) {
				return 0;
			} else if (this.weight > arg0.weight) {
				return 1;
			} else {
				return -1;
			}

		}

	}

}
