package mycontroller.router;

import tiles.MapTile;
import utilities.Coordinate;

public class Node implements Comparable<Node> {

	private Coordinate coord;
	private MapTile tile;
	private Node parent;
	private float cost = .0f;

	public Node(Coordinate coord, MapTile tile, Node parent) {
		this.coord = coord;
		this.tile = tile;
		this.parent = parent;
		this.cost = parent.cost;
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
	
	public Coordinate getCoord() {
		return coord;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public MapTile getTile() {
		return tile;
	}
	
	public float getCost() {
		return cost;
	}
	
	public void penalise(float penalty) {
		cost += penalty;
	}
}