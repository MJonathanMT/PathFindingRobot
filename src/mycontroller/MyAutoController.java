package mycontroller;

import java.util.Map;
import java.util.Set;

import controller.CarController;
import mycontroller.mapper.IMapper;
import mycontroller.mapper.MapperFactory;
import mycontroller.router.IRouter;
import mycontroller.router.RouterFactory;
import tiles.MapTile;
import utilities.Coordinate;
import world.Car;
import world.WorldSpatial;

public class MyAutoController extends CarController {
	private static final int MAX_SPEED = 1;
	
	private static final float HEALTH_PERC = 0.5f;

	private boolean forward;

	private IMapper mapper;
	private IRouter router;

	public MyAutoController(Car car) {
		super(car);

		this.mapper = MapperFactory.getMapper(getMap());
		this.router = RouterFactory.getRouter();

		forward = false;
	}

	@Override
	public void update() {		
		mapper.update(getView());

		Map<Coordinate, MapTile> map = mapper.getMap();
		Coordinate src = new Coordinate(getPosition());
		
		// try get to a parcel or finish first
		// else resort to exploring, then finding health
		IMapper.Type first = (numParcelsFound() >= numParcels()) ? IMapper.Type.FINISH : IMapper.Type.PARCEL;
		IMapper.Type[] order = { first, IMapper.Type.EXPLORE, IMapper.Type.HEALTH };
		for (IMapper.Type type : order) {
			Set<Coordinate> dests = mapper.getDestinations(type);
			
			float availableHealth = HEALTH_PERC * getHealth();
			
			Coordinate dest = router.getRoute(map, src, dests, availableHealth);
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

		if (currentPos.x < dest.x) {
			direction = WorldSpatial.Direction.EAST;
		} else if (dest.x < currentPos.x) {
			direction = WorldSpatial.Direction.WEST;
		} else if (dest.y < currentPos.y) {
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
