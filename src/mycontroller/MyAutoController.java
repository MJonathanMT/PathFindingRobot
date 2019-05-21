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
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 2;

	private Map<Coordinate, MapTile> map;

	private WorldSpatial.RelativeDirection followingDirection = null;

	private IRouter router;

	public MyAutoController(Car car) throws UnsupportedModeException {
		super(car);

		// start with as much knowledge as we can
		// we will fill this in as we go
		this.map = getMap();
		this.router = new UniformCostRouter();
	}

	@Override
	public void update() {
		updateMap();

		// try our router
		if (route()) {
			return;
		}

		// resort to wall following
		followWall();
	}

	/**
	 * Updates map with current view
	 */
	private void updateMap() {
		Map<Coordinate, MapTile> view = getView();
		for (Coordinate coord : view.keySet()) {
			if (onScreen(coord)) {
				map.put(coord, view.get(coord));
			}
		}
	}

	/**
	 * Attempts to follow a wall currently being followed, else tries to follow a wall
	 */
	private void followWall() {
		if (getSpeed() >= 0 && followingDirection != null && checkFollowable(getOrientation())) {
			applyReverseAcceleration();
			followingDirection = null;
		} else {
			applyForwardAcceleration();
		}

		boolean left = (followingDirection == WorldSpatial.RelativeDirection.LEFT);

		if (followingDirection != null) {
			// check if we are still following a wall
			if (!checkFollowing(followingDirection)) {
				// turn to follow the wall
				if (left)
					turnLeft();
				else
					turnRight();
				// check if we can follow something ahead
			} else if (checkFollowable(getOrientation())) {
				// turn to follow the wall
				if (left)
					turnRight();
				else
					turnLeft();
			}
		} else {
			tryFollowWall();
		}
	}

	private void tryFollowWall() {
		// default to left if a wall is direcly ahead
		if (checkFollowable(getOrientation())) {
			turnLeft();
			followingDirection = WorldSpatial.RelativeDirection.RIGHT;
			return;
		}
		
		// try follow a wall for each relative direction
		for (WorldSpatial.RelativeDirection relDirection : WorldSpatial.RelativeDirection.values()) {
			if (checkFollowing(relDirection)) {
				followingDirection = relDirection;
				break;
			}
		}
	}

	/**
	 * Checks if we are following a wall
	 * @param relative direction to check the wall at
	 * @return boolean
	 */
	private boolean checkFollowing(WorldSpatial.RelativeDirection relative) {
		return checkFollowable(WorldSpatial.changeDirection(getOrientation(), relative));
	}

	/**
	 * Checks if we can follow a wall in a given direction
	 * @param direction direction to check
	 * @return
	 */
	private boolean checkFollowable(WorldSpatial.Direction direction) {
		int x_off = 0, y_off = 0;
		switch (direction) {
		case EAST:
			x_off = 1;
			break;
		case WEST:
			x_off = -1;
			break;
		case NORTH:
			y_off = 1;
			break;
		case SOUTH:
			y_off = -1;
			break;
		}

		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			Coordinate relativeCoordinate = new Coordinate(currentPosition.x + x_off, currentPosition.y + y_off);
			if (!onScreen(relativeCoordinate)) {
				return true;
			}
			MapTile tile = map.get(relativeCoordinate);
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param coordinate Coordinate to check
	 * @return true if Coordinate is on screen, false otherwise
	 */
	private boolean onScreen(Coordinate coordinate) {
		return 0 <= coordinate.x && coordinate.x < mapWidth() && 0 <= coordinate.y && coordinate.y < mapHeight();
	}

	/**
	 * Uses the IRouter, router, to find a route to a current destination
	 * @return boolean, true if router succeeded, else false
	 */
	private boolean route() {
		Set<Coordinate> dests = getDests();

		Coordinate dest = router.getRoute(map, new Coordinate(getPosition()), dests);

		if (dest != null) {
			routeTowards(dest);
			return true;
		}

		return false;
	}

	/** 
	 * Gets the current destinations we want to travel to.
	 * If we have not picked up enough parcels, it will be a set of known parcel coords, else the finishes
	 * @return Set<Coordinate>
	 */
	private Set<Coordinate> getDests() {
		Set<Coordinate> dests = new HashSet<>();

		boolean finish = (numParcelsFound() >= numParcels());
		for (Coordinate coord : map.keySet()) {
			if ((finish && map.get(coord).isType(MapTile.Type.FINISH)
					|| (!finish && map.get(coord) instanceof ParcelTrap))) {
				dests.add(coord);
			}
		}

		return dests;
	}

	private void routeTowards(Coordinate dest) {
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
