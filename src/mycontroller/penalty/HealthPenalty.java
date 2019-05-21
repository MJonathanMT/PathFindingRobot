package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.LavaTrap;

public class HealthPenalty implements IPenalty {
	public void applyPenalty(Node node) {
		if (node.tile instanceof LavaTrap) {
			node.dist += 4;
		}
	}
}
