package mycontroller.router;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import exceptions.UnsupportedModeException;
import tiles.MapTile;
import utilities.Coordinate;

import mycontroller.penalty.*;

public class Router implements IRouter {
	private IPenalty penalty;

	public Router() throws UnsupportedModeException {
		this.penalty = PenaltyFactory.getCurrentPenalty();
	}

	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Coordinate dest) {
		if (!map.containsKey(src) || !map.containsKey(dest)) {
			return null;
		}

		PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator());
		queue.add(new Node(src, map.get(src), null));

		Map<Coordinate, Integer> seen = new HashMap<>();
		seen.put(src, 0);

		while (!queue.isEmpty()) {
			Node node = queue.poll();

			// found destination?
			if (node.coord.equals(dest)) {
				// reverse engineer to get back the tile we should go to next
				Node parent = node.parent;
				while (parent != null) {
					node = parent;
					parent = node.parent;
				}
				
				return node.coord;
			}

			for (Node neighbor : node.getNeighbors(map)) {
				penalty.applyPenalty(neighbor);

				if (!seen.containsKey(neighbor.coord) || seen.get(neighbor.coord).compareTo(neighbor.dist) > 0) {
					seen.put(neighbor.coord, neighbor.dist);
					queue.add(neighbor);
				}
			}
		}

		return null;
	}
}