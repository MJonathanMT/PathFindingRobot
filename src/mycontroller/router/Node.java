package mycontroller.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public class Node {
	public static final int[][] offsets = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };

	public Coordinate coord;
	public MapTile tile;
	public Node parent;
	public int dist = 0;

	public Node(Coordinate coord, MapTile tile, Node parent) {
		this.coord = coord;
		this.tile = tile;
		this.parent = parent;
		this.dist = 0; // set later to a better value if need be
	}

	public List<Node> getNeighbors(Map<Coordinate, MapTile> map) {
		List<Node> list = new ArrayList<Node>();

		for (int[] offset : offsets) {
			Coordinate coord = new Coordinate(this.coord.x + offset[0], this.coord.y + offset[1]);

			if (map.containsKey(coord) && !map.get(coord).isType(MapTile.Type.WALL)) {
				Node node = new Node(coord, map.get(coord), this);
				node.dist = this.dist + 1;
				list.add(node);
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
}