package mycontroller.router;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import mycontroller.penalty.IPenalty;
import tiles.MapTile;
import utilities.Coordinate;

public class UniformCostRouter implements IRouter {
	private IPenalty penalty;

	public UniformCostRouter(IPenalty penalty) {
		this.penalty = penalty;
	}

	/**
	 * Performs a uniform cost search, according to penalty
	 */
	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Set<Coordinate> dests, float health) {
		// abort early if we have no chance of finding a solution
		if (src == null || dests.isEmpty() || dests.contains(src) || !map.containsKey(src)) {
			return null;
		}

		PriorityQueue<UniformCostNode> queue = new PriorityQueue<>();
		queue.add(new UniformCostNode(src, map.get(src), health));

		Map<Coordinate, Float> seen = new HashMap<>();
		seen.put(src, .0f);

		while (!queue.isEmpty()) {
			UniformCostNode node = queue.poll();

			// found destination?
			if (dests.contains(node.getCoordinate())) {
				// reverse engineer to get back the tile we should go to next
				UniformCostNode parent = node.getParent();
				while (parent.getParent() != null) {
					node = parent;
					parent = node.getParent();
				}

				return node.getCoordinate();
			}

			// add neighbors, if we should
			for (UniformCostNode neighbor : node.getNeighbors(map)) {
				Coordinate coord = neighbor.getCoordinate();
				penalty.penalise(neighbor);
				if (!seen.containsKey(coord) || node.compareTo(neighbor) > 0) {
					seen.put(coord, neighbor.getCost());
					queue.add(neighbor);
				}
			}
		}

		return null;
	}
}