package mycontroller.router;

import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public interface IRouter {
	public Coordinate getRoute(Map<Coordinate, MapTile> map, Coordinate src, Coordinate dest);
}
