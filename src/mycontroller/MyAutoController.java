package mycontroller;

import controller.CarController;
import world.Car;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import tiles.MapTile;
import utilities.Coordinate;
import world.WorldSpatial;

public class MyAutoController extends CarController {		
		// How many minimum units the wall is away from the player.
		private int wallSensitivity = 1;
		
		private HashMap<Coordinate, MapTile> map = new HashMap<>();
		
		private boolean isFollowingWall = false; // This is set to true when the car starts sticking to a wall.
		
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
			if (getSpeed() >= 0 && checkFollowingWall(getOrientation(), currentView) && checkWallAhead(getOrientation(),currentView)) {
				applyReverseAcceleration();
				isFollowingWall = false;
			}
			if (isFollowingWall) {
				// If wall no longer on left, turn left
				if(!checkFollowingWall(getOrientation(), currentView)) {
					turnLeft();
				} else {
					// If wall on left and wall straight ahead, turn right
					if(checkWallAhead(getOrientation(), currentView)) {
						turnRight();
					}// If wall on left and wall straight ahead, turn right
				}
			} else {
				// Start wall-following (with wall on left) as soon as we see a wall straight ahead
				if(checkWallAhead(getOrientation(),currentView)) {
					turnRight();
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
		private boolean checkFollowingWall(WorldSpatial.Direction orientation, HashMap<Coordinate, MapTile> currentView) {
			return check(WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.LEFT), currentView);
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
				MapTile tile = currentView.get(new Coordinate(currentPosition.x+i*x_off, currentPosition.y+i*y_off));
				if(tile.isType(MapTile.Type.WALL) || tile.isType(MapTile.Type.TRAP)){
					return true;
				}
			}
			return false;
		}
		
		
		
		public void bfs(Coordinate position, HashMap<Coordinate, MapTile> currentView) {
			Queue<Coordinate> queue = new LinkedList<Coordinate>();
			HashMap<Coordinate, Boolean> visited = new HashMap<Coordinate, Boolean>();
	
			
			queue.add(position);
			visited.put(position, true);
			Coordinate nextNode;
			
			while(!queue.isEmpty()) {
				int i;
				nextNode = queue.remove(); //dequeue
				
			}
			
		}
		
			
	}
