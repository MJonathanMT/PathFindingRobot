package mycontroller;

import controller.CarController;
import world.Car;
import world.World;

import java.util.HashMap;

import tiles.LavaTrap;
import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class MyAutoController extends CarController {		
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		
		private HashMap<Coordinate, MapTile> map = new HashMap<>();
		
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		private boolean followingLeft = false;
		
		private WorldSpatial.RelativeDirection followingDirection = null;
		// Car Speed to move at
		private final int CAR_MAX_SPEED = 1;
		
		public MyAutoController(Car car) {
			super(car);
		}
		
		// Coordinate initialGuess;
		// boolean notSouth = true;
		@Override
		public void update() {
			// Gets what the car can see
			HashMap<Coordinate, MapTile> currentView = getView();
			map.putAll(currentView);
			
			// checkStateChange();
			if(getSpeed() < CAR_MAX_SPEED) {       // Need speed to turn and progress toward the exit
				applyForwardAcceleration();   // Tough luck if there's a wall in the way
			}
			if (getSpeed() >= 0 && isFollowingWall && checkWallAhead(getOrientation(),currentView)) {
				applyReverseAcceleration();
				isFollowingWall = false;
			} else {
				applyForwardAcceleration(); 
			}
			if (isFollowingWall) {
				// If wall no longer on left, turn left
				if(!checkFollowingWall(getOrientation(), followingDirection, currentView)) {
					if (followingLeft) turnLeft();
					else turnRight();
				} else {
					// If wall on left and wall straight ahead, turn right
					if(checkWallAhead(getOrientation(), currentView)) {
						if (followingLeft) turnRight();
						else turnLeft();
					} 
				}
			} else {
				if (checkFollowingWall(getOrientation(), WorldSpatial.RelativeDirection.LEFT, currentView)) {
					isFollowingWall = true;
					followingDirection = WorldSpatial.RelativeDirection.LEFT;
					followingLeft = true;
					
				} else if (checkFollowingWall(getOrientation(), WorldSpatial.RelativeDirection.RIGHT, currentView)) {
					isFollowingWall = true;
					followingDirection = WorldSpatial.RelativeDirection.RIGHT;
					followingLeft = false;
				}
				// Start wall-following (with wall on left) as soon as we see a wall straight ahead
				if(checkWallAhead(getOrientation(),currentView)) {
					turnLeft();
					isFollowingWall = true;
				}
			}
		}

		/**
		 * Check if you have a wall in front of you!
		 * @param orientation the orientation we are in based on WorldSpatial
		 * @param currentView what the car can currently see
		 * @return
		 */
		private boolean checkWallAhead(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView){
			return check(orientation, currentView);
		}
		
		/**x
		 * Check if the wall is on your left hand side given your orientation
		 * @param orientation
		 * @param currentView
		 * @return
		 */
		private boolean checkFollowingWall(WorldSpatial.Direction orientation, WorldSpatial.RelativeDirection relative, HashMap<Coordinate, MapTile> currentView) {
			return check(WorldSpatial.changeDirection(orientation, relative), currentView);
		}
		
		public boolean check(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
			int x_off = 0, y_off = 0;
			switch(orientation){
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
			for(int i = 0; i <= wallSensitivity; i++){
				Coordinate relativeCoordinate = getRelativeCoordinate(currentPosition, x_off, y_off);
				MapTile tile = currentView.get(relativeCoordinate);
				if(!onScreen(relativeCoordinate) || tile.isType(MapTile.Type.WALL) || tile instanceof LavaTrap){
					return true;
				}
			}
			return false;
		}
		
//		private int getWallNeighbourCount(Coordinate coordinate) {
//			if (map.containsKey(coordinate)) {
//				int count = 0;
//				int[][] offsets = {{0,1},{0,-1},{1,0},{-1,0}};
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
			
		private Coordinate getRelativeCoordinate(Coordinate coordinate, int x_off, int y_off) {
			return new Coordinate(coordinate.x + x_off, coordinate.y + y_off);
		}	
		
		private boolean onScreen(Coordinate coordinate) {
			return 0 <= coordinate.x && coordinate.x <= World.MAP_WIDTH
					&& 0 <= coordinate.y && coordinate.y <= World.MAP_HEIGHT; 
		}
	}
