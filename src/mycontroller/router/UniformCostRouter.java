package mycontroller.router;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import exceptions.UnsupportedModeException;
import tiles.MapTile;
import utilities.Coordinate;

import mycontroller.penalty.*;

public class UniformCostRouter implements IRouter {
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
			if (dests.contains(node.coord)) {
				// reverse engineer to get back the tile we should go to next
				Node parent = node.parent;
				while (parent.parent != null) {
					node = parent;
					parent = node.parent;
				}
				
				return node.coord;
			}

			// add neighbors, if we should 
			for (Node neighbor : node.getNeighbors(map)) {
				penalty.applyPenalty(neighbor);

				if (!seen.containsKey(neighbor.coord) || node.compareTo(neighbor) > 0) {
					seen.put(neighbor.coord, neighbor.cost);
					queue.add(neighbor);
				}
			}
		}

		return null;
	}
}