package mycontroller.router;

import tiles.MapTile;
import utilities.Coordinate;

public class Node implements Comparable<Node> {

	public Coordinate coord;
	public MapTile tile;
	public Node parent;
	public float cost = .0f;

	public Node(Coordinate coord, MapTile tile, Node parent) {
		this.coord = coord;
		this.tile = tile;
		this.parent = parent;
		this.cost = parent.cost + 1;
	}

	public Node(Coordinate coord, MapTile tile) {
		this.coord = coord;
		this.tile = tile;
		this.parent = null;
		this.cost = .0f;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Node)) {
			return false;
		}
		Node node = (Node) o;
		return coord.equals(node.coord);
	}

	@Override
	public int compareTo(Node n) {
		return Float.compare(cost, n.cost);
	}
}