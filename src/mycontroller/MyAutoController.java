package mycontroller;

import controller.CarController;
import exceptions.UnsupportedModeException;
import mycontroller.mapper.*;
import mycontroller.router.*;
import world.Car;

import utilities.Coordinate;
import world.WorldSpatial;

public class MyAutoController extends CarController {
	private static int MAX_SPEED = 1;
	
	private boolean forward;
	
	private IMapper mapper;
	private IRouter router;

	public MyAutoController(Car car) throws UnsupportedModeException {
		super(car);

		this.mapper = MapperFactory.getMapper(getMap());
		this.router = RouterFactory.getRouter();
		
		forward = false;
	}

	@Override
	public void update() {
		mapper.update(getView());

		Coordinate src = new Coordinate(getPosition());
		// try get to a parcel or finish first
		IMapper.Type first = (numParcelsFound() >= numParcels()) ? IMapper.Type.FINISH : IMapper.Type.PARCEL;
		IMapper.Type[] order = { first, IMapper.Type.EXPLORE };
		for (IMapper.Type type : order) {
			Coordinate dest = router.getRoute(mapper.getMap(), src, mapper.getDestinations(type));
			if (dest != null) {
				moveTowards(dest);
				return;
			}
		}

		// shouldn't occur, but just in case
		applyBrake();
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

		// try move faster forwards
		if (!forward || getSpeed() < MAX_SPEED) {
			applyForwardAcceleration();	
			forward = true;
		}
		if (direction == orientation) {
			// already heading in the right direction
			return;
		} else if (direction == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.LEFT)) {
			turnLeft();
		} else if (direction == WorldSpatial.changeDirection(orientation, WorldSpatial.RelativeDirection.RIGHT)) {
			turnRight();
		} else {
			// try move faster backwards
			if (forward || getSpeed() < MAX_SPEED) {
				applyReverseAcceleration();	
				forward = false;
			}
		}
	}
}
