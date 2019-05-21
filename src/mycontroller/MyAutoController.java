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

	private boolean followingLeft = false;

	private WorldSpatial.RelativeDirection followingDirection = null;

	private IRouter router;
	// Car Speed to move at
//	private final int CAR_MAX_SPEED = 1;

	public MyAutoController(Car car) throws UnsupportedModeException {
		super(car);

		this.map = getMap();
		this.router = new UniformCostRouter();
	}

	@Override
	public void update() {
		updateMap();

		if (route()) {
			return;
		}

		followWall();
	}

	private void updateMap() {
		Map<Coordinate, MapTile> view = getView();
		for (Coordinate coord : view.keySet()) {
			if (onScreen(coord)) {
				map.put(coord, view.get(coord));
			}
		}
	}

	private void followWall() {
		if (getSpeed() >= 0 && followingDirection != null && checkTravellable(getOrientation())) {
			applyReverseAcceleration();
			followingDirection = null;
		} else {
			applyForwardAcceleration();
		}

		if (followingDirection != null) {
			// If wall no longer on left, turn left
			if (!checkFollowing(getOrientation(), followingDirection)) {
				if (followingLeft)
					turnLeft();
				else
					turnRight();
			} else {
				// If wall on left and wall straight ahead, turn right
				if (checkTravellable(getOrientation())) {
					if (followingLeft)
						turnRight();
					else
						turnLeft();
				}
			}
		} else {
			tryFollowWall();
		}
	}

	private void tryFollowWall() {
		if (checkFollowing(getOrientation(), WorldSpatial.RelativeDirection.LEFT)) {
			followingDirection = WorldSpatial.RelativeDirection.LEFT;
			followingLeft = true;

		} else if (checkFollowing(getOrientation(), WorldSpatial.RelativeDirection.RIGHT)) {
			followingDirection = WorldSpatial.RelativeDirection.RIGHT;
			followingLeft = false;
		}
		// Start wall-following (with wall on left) as soon as we see a wall straight
		// ahead
		if (checkTravellable(getOrientation())) {
			turnLeft();
			followingDirection = WorldSpatial.RelativeDirection.RIGHT;
			followingLeft = false;
		}
	}

	/**
	 * x Check if the wall is on your left hand side given your orientation
	 * 
	 * @param orientation
	 * @param currentView
	 * @return
	 */
	private boolean checkFollowing(WorldSpatial.Direction orientation, WorldSpatial.RelativeDirection relative) {
		return checkTravellable(WorldSpatial.changeDirection(orientation, relative));
	}

	public boolean checkTravellable(WorldSpatial.Direction orientation) {
		int x_off = 0, y_off = 0;
		switch (orientation) {
		case EAST:
			x_off = 1;
			break;
		case NORTH:
			y_off = 1;
			break;
		case SOUTH:
			y_off = -1;
			break;
		case WEST:
			x_off = -1;
			break;
		default:
			return false;
		}

		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			Coordinate relativeCoordinate = getRelativeCoordinate(currentPosition, x_off, y_off);
			if (!onScreen(relativeCoordinate)) {
				return true;
			}

			MapTile tile = map.get(relativeCoordinate);
			if (tile.isType(MapTile.Type.WALL) || tile instanceof LavaTrap) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param coordinate Coordinate
	 * @param x_off      X offset
	 * @param y_off      Y offset
	 * @return Coordinate with coordinates (x_off, y_off) relative to coordinate
	 */
	private Coordinate getRelativeCoordinate(Coordinate coordinate, int x_off, int y_off) {
		return new Coordinate(coordinate.x + x_off, coordinate.y + y_off);
	}

	/**
	 * @param coordinate Coordinate to check
	 * @return true if Coordinate is on screen, false otherwise
	 */
	private boolean onScreen(Coordinate coordinate) {
		return 0 <= coordinate.x && coordinate.x <= mapWidth() && 0 <= coordinate.y && coordinate.y <= mapHeight();
	}

	private boolean route() {
		Set<Coordinate> dests = getDests();
		
		Coordinate dest = router.getRoute(map, new Coordinate(getPosition()), dests);
		
		if (dest != null) {
			routeTowards(dest);
			return true;
		}
		
		return false;
	}
	
	private Set<Coordinate> getDests() {
		Set<Coordinate> dests = new HashSet<>();
		
		boolean toFinish = hasEnoughParcels();
		for (Coordinate coord : map.keySet()) {
			if ((toFinish && map.get(coord).isType(MapTile.Type.FINISH)
					|| (!toFinish && map.get(coord) instanceof ParcelTrap))) {
				dests.add(coord);
			}
		}
		
		return dests;
	}

	private void routeTowards(Coordinate dest) {
		Coordinate currentPos = new Coordinate(getPosition());

		WorldSpatial.Direction dir = null, orientation = getOrientation();

		if (currentPos.x > dest.x) {
			dir = WorldSpatial.Direction.WEST;
		} else if (currentPos.x < dest.x) {
			dir = WorldSpatial.Direction.EAST;
		} else if (currentPos.y > dest.y) {
			dir = WorldSpatial.Direction.SOUTH;
		} else if (currentPos.y < dest.y) {
			dir = WorldSpatial.Direction.NORTH;
		} else {
			return;
		}

		applyForwardAcceleration();
		if (dir == orientation) {
			return;
		} else if (dir == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (dir == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.RIGHT)) {
			turnRight();
		} else {
			applyReverseAcceleration();
		}
	}

	private boolean hasEnoughParcels() {
		return numParcelsFound() >= numParcels();
	}
}
