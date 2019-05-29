package mycontroller.penalty;

public class HealthPenalty implements IPenalty {

	private static final float LAVA_PENALTY = 8.0f;
	private static final float HEALTH_PENALTY = .5f;

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
