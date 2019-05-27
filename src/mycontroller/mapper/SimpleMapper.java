package mycontroller.mapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tiles.MapTile;
import tiles.TrapTile;
import utilities.Coordinate;

public class SimpleMapper implements IMapper {
	private Map<Coordinate, MapTile> map;
	private Set<Coordinate> explored;

	public SimpleMapper(Map<Coordinate, MapTile> map) {
		this.map = new HashMap<>(map);
		this.explored = new HashSet<Coordinate>();
	}

	@Override
	public void update(Map<Coordinate, MapTile> view) {
		for (Coordinate coord : view.keySet()) {
			map.put(coord, view.get(coord));
			explored.add(coord);
		}
	}

	@Override
	public Set<Coordinate> getDestinations(Type type) {
		Set<Coordinate> dests = new HashSet<>();
		
		for (Coordinate coord : map.keySet()) {
			boolean add = false;
			switch (type) {
			case EXPLORE:
				// we want to go somewhere unexplored
				if (!explored.contains(coord))
					add = true;
				break;
			case FINISH:
				// we want to get to the finish tiles
				if (map.get(coord).isType(MapTile.Type.FINISH))
					add = true;
				break;
			case PARCEL:
				// we want to go to parcels
				if (map.get(coord).isType(MapTile.Type.TRAP) && ((TrapTile) map.get(coord)).getTrap().equals("parcel"))
					add = true;
				break;
			default: 
				break;
			}

			if (add) {
				dests.add(coord);
			}
		}

		return dests;
	}

	@Override
	public Map<Coordinate, MapTile> getMap() {
		return map;
	}
}