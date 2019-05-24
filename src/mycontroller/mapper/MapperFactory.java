package mycontroller.mapper;

import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;

public class MapperFactory {
	public static IMapper getMapper(Map<Coordinate, MapTile> map) {
		return new SimpleMapper(map);
	}
}