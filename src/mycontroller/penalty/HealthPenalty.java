package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.*;

public class HealthPenalty implements IPenalty {
	private static HealthPenalty instance = null;

	private HealthPenalty() {
	}

	public static HealthPenalty getInstance() {
		if (instance == null) {
			instance = new HealthPenalty();
		}

		return instance;
	}

	public void applyPenalty(Node node) {
		MapTile tile = node.getTile();
		if (node.getTile().isType(MapTile.Type.TRAP)) {
			float penalty = .0f;
			TrapTile trap = (TrapTile) tile;

			switch (trap.getTrap()) {
			case "lava":
				penalty = 50f;
				break;
			case "water":
			case "health":
				penalty = -.5f;
				break;
			}

			node.penalise(penalty);
		}
	}
}
