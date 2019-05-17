package mycontroller;
import controller.CarController;
import world.Car;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

// hello val test.1

public class MyAutoController extends CarController {
	// How many minimum units the wall is away from the player.
	private int wallSensitivity = 1;

	private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
	
	// Car Speed to move at
	private final int CAR_MAX_SPEED = 1;

	public MyAutoController(Car car) {
		super(car);
	}

	// Coordinate initialGuess;
	// boolean notSouth = true;
	
	
	// Set a record of map coordinates
	private HashMap<Coordinate, MapTile> mapCoordinates;
	private boolean locatedParcel = false;
	
	public void updateMap(HashMap<Coordinate, MapTile> mapCoordinates, HashMap<Coordinate, MapTile> currentView) {
		// Add everything in currentView not in mapCoordinates to mapCoordinates
	    mapCoordinates.putAll(currentView);
	}
//	public void removeTile(HashMap<Coordinate, MapTile> mapCoordinates, item) {
//		mapCoordinates.remove(arg0);
//	}
	@Override
	public void update() {
		// Gets what the car can see
		HashMap<Coordinate, MapTile> currentView = getView();
		
		updateMap(mapCoordinates, currentView);

		// checkStateChange();
		if (getSpeed() < CAR_MAX_SPEED && !checkWallAhead(getOrientation(), currentView)) { // Need speed to turn and progress toward the exit
			applyForwardAcceleration(); // Tough luck if there's a wall in the way
		}
		
		if (locatedParcel) {
			
		}
		if (isFollowingWall) {
			// If wall no longer on left, turn left
			if (!checkFollowingWall(getOrientation(), currentView)) {
				turnLeft();
			} else {
				// If wall on left and wall straight ahead, turn right
				if (checkWallAhead(getOrientation(), currentView)) {
					turnRight();
				}
			}
		} else {
			// Start wall-following (with wall on left) as soon as we see a wall straight
			// ahead
			if (checkWallAhead(getOrientation(), currentView)) {
				turnRight();
				isFollowingWall = true;
			}
		}
	}

	/**
	 * Check if you have a wall in front of you!
	 * 
	 * @param orientation the orientation we are in based on WorldSpatial
	 * @param currentView what the car can currently see
	 * @return
	 */
	private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		switch (orientation) {
		case EAST:
			return checkEast(currentView);
		case NORTH:
			return checkNorth(currentView);
		case SOUTH:
			return checkSouth(currentView);
		case WEST:
			return checkWest(currentView);
		default:
			return false;
		}
	}

	/**
	 * Check if the wall is on your left hand side given your orientation
	 * 
	 * @param orientation
	 * @param currentView
	 * @returnWorldSpatial.Direction orientation
	 */
	private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
		return checkWall(currentView, orientation);
	}

	/**
	 * Method below just iterates through the list and check in the correct
	 * coordinates. i.e. Given your current position is 10,10 checkEast will check
	 * up to wallSensitivity amount of tiles to the right. checkWest will check up
	 * to wallSensitivity amount of tiles to the left. checkNorth will check up to
	 * wallSensitivity amount of tiles to the top. checkSouth will check up to
	 * wallSensitivity amount of tiles below.
	 */
	public boolean checkEast(HashMap<Coordinate, MapTile> currentView) {
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(new Coordinate(currentPosition.x + i, currentPosition.y));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkWall(HashMap<Coordinate, MapTile> currentView, WorldSpatial.Direction orientation) {
		int x_off, y_off;
		
		switch (orientation) {
		case EAST:
			x_off = 1;
			y_off = 0;
			break;
		case NORTH:
			x_off = 0;
			y_off = 1;
			break;
		case SOUTH:
			x_off = 0;
			y_off = -1;
			break;
		case WEST:
			x_off = -1;
			y_off = 0;
			break;
		default:
			return false;
		}
		
		// Check tiles to my right
		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(new Coordinate(currentPosition.x + i * x_off, currentPosition.y + i * y_off));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkWest(HashMap<Coordinate, MapTile> currentView) {
		// Check tiles to my left
		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(new Coordinate(currentPosition.x - i, currentPosition.y));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkNorth(HashMap<Coordinate, MapTile> currentView) {
		// Check tiles to towards the top
		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y + i));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}

	public boolean checkSouth(HashMap<Coordinate, MapTile> currentView) {
		// Check tiles towards the bottom
		Coordinate currentPosition = new Coordinate(getPosition());
		for (int i = 0; i <= wallSensitivity; i++) {
			MapTile tile = currentView.get(new Coordinate(currentPosition.x, currentPosition.y - i));
			if (tile.isType(MapTile.Type.WALL)) {
				return true;
			}
		}
		return false;
	}

}
