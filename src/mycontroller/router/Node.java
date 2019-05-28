package mycontroller.router;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;

public class Node implements Comparable<Node> {
	private static float LAVA_HEALTH_USAGE = 0.25f * LavaTrap.HealthDelta;

	private Coordinate coord;
	private MapTile tile;
	private Node parent;
	private float cost = .0f;
	private float health = .0f;

	public Node(Coordinate coord, MapTile tile, Node parent) {
		this.coord = coord;
		this.tile = tile;
		this.parent = parent;
		this.cost = parent.cost;
		this.health = parent.health;
		if (tile instanceof LavaTrap) {
			this.health -= LAVA_HEALTH_USAGE;
		}
	}

	public Node(Coordinate coord, MapTile tile, float health) {
		this.coord = coord;
		this.tile = tile;
		this.parent = null;
		this.cost = .0f;
		this.health = health;
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
		int cmp = Float.compare(cost, n.cost);
		if (cmp != 0) {
			return cmp;
		}

		return Float.compare(n.health, health);
	}
	
	public int hashcode() {
		return coord.toString().hashCode();
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

	public float getHealth() {
		return health;
	}
}