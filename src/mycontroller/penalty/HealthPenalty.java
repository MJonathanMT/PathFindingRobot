package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.*;

public class HealthPenalty implements IPenalty {
	public void applyPenalty(Node node) {
		MapTile tile = node.getTile();
		float penalty = .0f;
		if (tile instanceof LavaTrap) {
			penalty += 50;
		}
		if (tile instanceof WaterTrap) {
			penalty -= 0.5;
		}
		if (tile instanceof HealthTrap) {
			penalty -= 1;
		}
		
		node.penalise(penalty);
	}
}
