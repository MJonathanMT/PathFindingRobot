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

	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Set<Coordinate> dests) {
		if (src == null || dests.contains(src) || !map.containsKey(src)) {
			return null;
		}

		PriorityQueue<Node> queue = new PriorityQueue<>();
		queue.add(new Node(src, map.get(src), null));

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

			for (Node neighbor : node.getNeighbors(map)) {
				penalty.applyPenalty(neighbor);

				if (!seen.containsKey(neighbor.coord) || seen.get(neighbor.coord) > neighbor.dist) {
					seen.put(neighbor.coord, neighbor.dist);
					queue.add(neighbor);
				}
			}
		}

		return null;
	}
}