package mycontroller.router;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import tiles.HealthTrap;
import tiles.LavaTrap;
import tiles.MapTile;
import tiles.WaterTrap;
import utilities.Coordinate;

public class UniformCostNode implements INode, Comparable<UniformCostNode> {
	private static float LAVA_HEALTH_USAGE = 0.25f * LavaTrap.HealthDelta;

	private static final int[][] OFFSETS = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
	private static final int X = 0;
	private static final int Y = 1;

	private Coordinate coord;
	private MapTile tile;
	private UniformCostNode parent;
	private float cost = .0f;
	private float health = .0f;

	public UniformCostNode(Coordinate coord, MapTile tile, UniformCostNode parent) {
		this.coord = coord;
		this.tile = tile;
		this.parent = parent;
		this.cost = parent.cost;
		this.health = parent.health;
		
		if (tile instanceof LavaTrap) {
			this.health -= LAVA_HEALTH_USAGE;
		}
	}

	public UniformCostNode(Coordinate coord, MapTile tile, float health) {
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
		if (!(o instanceof UniformCostNode)) {
			return false;
		}
		UniformCostNode node = (UniformCostNode) o;
		return coord.equals(node.coord);
	}

	@Override
	public int compareTo(UniformCostNode n) {
		// minimize cost
		int cmp = Float.compare(cost, n.cost);
		if (cmp != 0) {
			return cmp;
		}
		
		// maximize health
		return Float.compare(n.health, health);
	}
	
	public int hashcode() {
		return coord.hashCode();
	}

	public Coordinate getCoordinate() {
		return coord;
	}

	public MapTile getTile() {
		return tile;
	}

	public UniformCostNode getParent() {
		return parent;
	}

	public float getCost() {
		return cost;
	}

	public void applyPenalty(float penalty) {
		cost += penalty;
	}
	
	public List<UniformCostNode> getNeighbors(Map<Coordinate, MapTile> map) {
		List<UniformCostNode> neighbors = new ArrayList<UniformCostNode>(4);

		for (int[] offset : OFFSETS) {
			Coordinate coord = new Coordinate(this.coord.x + offset[X], this.coord.y + offset[Y]);
			// add a neighbor if its coord is in the map and not a wall
			if (map.containsKey(coord) && !map.get(coord).isType(MapTile.Type.WALL)) {
				UniformCostNode neighbor = new UniformCostNode(coord, map.get(coord), this);
				
				// only consider neighbors with positive health
				if (neighbor.health > 0) {
					neighbors.add(neighbor);
				}
			}
		}
		
		return neighbors;
	}
}