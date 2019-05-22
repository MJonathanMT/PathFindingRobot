package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.*;

public class HealthPenalty implements IPenalty {
	// singleton
	public static HealthPenalty instance = null;

	private HealthPenalty() {
	}

	public static HealthPenalty getInstance() {
		if (instance == null)
			instance = new HealthPenalty();

		return instance;
	}

	public void applyPenalty(Node node) {
		if (node.tile instanceof LavaTrap) {
			node.cost += 50;
		}
		if (node.tile instanceof WaterTrap) {
			node.cost -= 0.5;
		}
		if (node.tile instanceof HealthTrap) {
			node.cost -= 1;
		}
	}
}
