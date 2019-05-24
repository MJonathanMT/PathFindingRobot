package mycontroller.router;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import exceptions.UnsupportedModeException;
import mycontroller.penalty.IPenalty;
import mycontroller.penalty.PenaltyFactory;
import tiles.MapTile;
import utilities.Coordinate;

public class UniformCostRouter implements IRouter {
	private static final int[][] OFFSETS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
	private static final int X = 0;
	private static final int Y = 1;
	
	private IPenalty penalty;

	public UniformCostRouter() throws UnsupportedModeException {
		this.penalty = PenaltyFactory.getCurrentPenalty();
	}

	/**
	 * Performs a uniform cost search, according to penalty
	 */
	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Set<Coordinate> dests) {
		// abort early if we have no chance of finding a solution
		if (src == null || dests.isEmpty() || dests.contains(src) || !map.containsKey(src)) {
			return null;
		}

		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(src, map.get(src)));

		Map<Coordinate, Float> seen = new HashMap<>();
		seen.put(src, .0f);

		while (!queue.isEmpty()) {
			Node node = queue.poll();

			// found destination?
			if (dests.contains(node.getCoord())) {
				// reverse engineer to get back the tile we should go to next
				Node parent = node.getParent();
				while (parent.getParent() != null) {
					node = parent;
					parent = node.getParent();
				}
				
				return node.getCoord();
			}

			// add neighbors, if we should 
			for (Node neighbor : getNeighbors(node, map)) {
				Coordinate coord = neighbor.getCoord();
				if (!seen.containsKey(coord) || node.compareTo(neighbor) > 0) {
					seen.put(coord, neighbor.getCost());
					queue.add(neighbor);
				}
			}
		}

		return null;
	}
	
	private List<Node> getNeighbors(Node node, Map<Coordinate, MapTile> map) {
		List<Node> list = new ArrayList<Node>(4);

		for (int[] offset : OFFSETS) {
			Coordinate coord = new Coordinate(node.getCoord().x + offset[X], node.getCoord().y + offset[Y]);
			// add a neighbor if its coord is in the map and not a wall
			if (map.containsKey(coord) && !map.get(coord).isType(MapTile.Type.WALL)) {
				Node neighbor = new Node(coord, map.get(coord), node);
				penalty.applyPenalty(neighbor);
				
				list.add(neighbor);
			}
		}
		
		return list;
	}
}