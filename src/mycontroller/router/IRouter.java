package mycontroller.router;

import java.util.Map;
import java.util.Set;

import tiles.MapTile;
import utilities.Coordinate;

public interface IRouter {
	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Set<Coordinate> dests);
}
