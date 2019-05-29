package mycontroller.router;

import tiles.MapTile;
import utilities.Coordinate;

public interface INode {
	public void applyPenalty(float penalty);
	public MapTile getTile();
	public Coordinate getCoordinate();
}