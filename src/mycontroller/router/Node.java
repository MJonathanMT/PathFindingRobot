package mycontroller.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public class Node implements Comparable<Node> {
	private static final int[][] OFFSETS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
	private static final int X = 0;
	private static final int Y = 1;

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

	public List<Node> getNeighbors(Map<Coordinate, MapTile> map) {
		List<Node> list = new ArrayList<Node>(4);

		for (int[] offset : OFFSETS) {
			Coordinate coord = new Coordinate(this.coord.x + offset[X], this.coord.y + offset[Y]);
			
			// add a neighbor if its coord is in the map and not a wall
			if (map.containsKey(coord) && !map.get(coord).isType(MapTile.Type.WALL)) {
				list.add(new Node(coord, map.get(coord), this));
			}
		}
		
		return list;
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