package mycontroller.penalty;

public class FuelPenalty implements IPenalty {

	private static final float LAVA_PENALTY = 2.5f;
	private static final float HEALTH_PENALTY = -.5f;

	@Override
	public float getPenalty(String trap) {
		switch(trap) {
		case "lava":
			return LAVA_PENALTY;
		case "health":
		case "water":
			return HEALTH_PENALTY;
		default:
			return 0f;
		}
	}
}
