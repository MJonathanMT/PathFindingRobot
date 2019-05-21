package mycontroller.router;

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
	
	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate dest) {
		PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator());
		queue.add(new Node(dest, map.get(dest), null));
		
		while (!queue.isEmpty()) {
			Node node = queue.poll();

			if (node.coord.equals(dest)) {
				Node parent = node.parent;
				while (node.parent != null) {
					node = parent;
					parent = node.parent;
				}
				return node.coord;
			}

			for (Node neighbour : node.getNeighbours(map)) {
				penalty.applyPenalty(neighbour);
				queue.add(neighbour);
			}
		}

		return null;
	}
}