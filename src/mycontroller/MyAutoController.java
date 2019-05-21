package mycontroller;

import controller.CarController;
import exceptions.UnsupportedModeException;
import mycontroller.router.*;
import world.Car;

import java.util.HashMap;
import java.util.Map;

import tiles.*;
import utilities.Coordinate;
import world.WorldSpatial;

public class MyAutoController extends CarController {
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 2;

	private Map<Coordinate, MapTile> map = new HashMap<>();

	private boolean followingLeft = false;

	private WorldSpatial.RelativeDirection followingDirection = null;
	
	private IRouter router;
	// Car Speed to move at
//	private final int CAR_MAX_SPEED = 1;

//	private Strategy strategy;

	public MyAutoController(Car car) throws UnsupportedModeException {
		super(car);

		this.router = new Router();
//		strategy = StrategyFactory.getCurrentStrategy();
	}

	@Override
	public void update() {
		// update current knowledge of world
		map.putAll(getView());
		
		followWall();
	}
	
	private void followWall() {
		if (getSpeed() >= 0 && followingDirection != null && checkAhead(getOrientation())) {
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
				if (checkAhead(getOrientation())) {
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
		if (checkAhead(getOrientation())) {
			turnLeft();
			followingDirection = WorldSpatial.RelativeDirection.RIGHT;
			followingLeft = false;
		}
	}

	/**
	 * Check if you have a wall in front of you!
	 * 
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	private boolean checkAhead(WorldSpatial.Direction orientation) {
		return checkTravellable(orientation);
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

//		private int getWallNeighbourCount(Coordinate coordinate) {
//			if (map.containsKey(coordinate)) {
//				int count = 0;
//				int[][] offsets = { { 0, 1 }, { 0, -1 }, { 1, 0 }, { -1, 0 } };
//				for (int[] offset : offsets) {
//					Coordinate relativeCoordinate = getRelativeCoordinate(coordinate, offset[0], offset[1]);
//					if (map.containsKey(relativeCoordinate)) {
//						MapTile tile = map.get(relativeCoordinate);
//						if(tile.isType(MapTile.Type.WALL) || tile instanceof LavaTrap){
//							count++;
//						}
//					}
//				}
//				return count;
//			}
//			return -1;
//		}

	/**
	 * @param coordinate Coordinate
	 * @param x_off      X offset
	 * @param y_off      Y offset
	 * @return Cooridnate with coordinates (x_off, y_off) relative to coordinate
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
}
