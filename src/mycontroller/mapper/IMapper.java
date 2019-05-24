package mycontroller.mapper;

import java.util.Map;
import java.util.Set;

import tiles.MapTile;
import utilities.Coordinate;

public interface IMapper {
	public enum Type { EXPLORE, FINISH, PARCEL };
	
	public void update(Map<Coordinate, MapTile> view);
	
	public Set<Coordinate> getDestinations(Type type);
	
	public Map<Coordinate, MapTile> getMap();
}