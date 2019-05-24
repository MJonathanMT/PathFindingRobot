package mycontroller;

import controller.CarController;
import exceptions.UnsupportedModeException;
import mycontroller.router.*;
import world.Car;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import tiles.*;
import utilities.Coordinate;
import world.WorldSpatial;

public class MyAutoController extends CarController {

	private Map<Coordinate, MapTile> map;
	private Set<Coordinate> explored;

	private IRouter router;

	public MyAutoController(Car car) throws UnsupportedModeException {
		super(car);

		// start with as much knowledge as we can
		// we will fill this in as we go
		this.map = getMap();
		this.explored = new HashSet<>();

		this.router = new UniformCostRouter();
	}

	@Override
	public void update() {
		updateMap();

		route();
	}

	/**
	 * Updates map with current view
	 */
	private void updateMap() {
		Map<Coordinate, MapTile> view = getView();
		for (Coordinate coord : view.keySet()) {
			map.put(coord, view.get(coord));
			explored.add(coord);
		}
	}
	
	/**
	 * Uses the IRouter, router, to find a route to a current destination, and
	 * movesTowards destination
	 */
	private void route() {
		Coordinate src = new Coordinate(getPosition());

		// try get to a parcel or finish first
		boolean[] order = { false, true };
		for (boolean explore : order) {
			Coordinate dest = router.getRoute(map, src, getDests(explore));
			if (dest != null) {
				moveTowards(dest);
				return;
			}
		}

		// shouldn't occur, but just in case
		applyBrake();
	}

	/**
	 * Gets the current destinations we want to travel to
	 * 
	 * @param explore boolean, true if we should explore the map more, else false to
	 *                route to the finish or a parcel
	 * @return Set<Coordinate>
	 */
	private Set<Coordinate> getDests(boolean explore) {
		Set<Coordinate> dests = new HashSet<>();

		boolean finished = (numParcelsFound() >= numParcels());
		for (Coordinate coord : map.keySet()) {
			if ((explore && !map.get(coord).isType(MapTile.Type.WALL) && !explored.contains(coord))
					|| (!explore && (finished && map.get(coord).isType(MapTile.Type.FINISH)
							|| (!finished && map.get(coord) instanceof ParcelTrap)))) {
				dests.add(coord);
			}
		}

		return dests;
	}

	/**
	 * Routes the car towards dest (assumed to be one of the four adjacent
	 * coordinates to current position)
	 * 
	 * @param dest
	 */
	private void moveTowards(Coordinate dest) {
		Coordinate currentPos = new Coordinate(getPosition());

		WorldSpatial.Direction orientation = getOrientation(), direction;

		if (currentPos.x > dest.x) {
			direction = WorldSpatial.Direction.WEST;
		} else if (currentPos.x < dest.x) {
			direction = WorldSpatial.Direction.EAST;
		} else if (currentPos.y > dest.y) {
			direction = WorldSpatial.Direction.SOUTH;
		} else if (currentPos.y < dest.y) {
			direction = WorldSpatial.Direction.NORTH;
		} else {
			applyBrake();
			return;
		}

		applyForwardAcceleration();
		if (direction == orientation) {
			return;
		} else if (direction == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (direction == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.RIGHT)) {
			turnRight();
		} else {
			applyReverseAcceleration();
		}
	}
}
