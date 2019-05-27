package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.MapTile;
import tiles.TrapTile;

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
		float penalty = 1.0f;
		
		if (node.getTile().isType(MapTile.Type.TRAP)) {
			TrapTile trap = (TrapTile) tile;

			switch (trap.getTrap()) {
			case "lava":
				penalty += 2.5f;
				break;
			case "water":
			case "health":
				penalty -= 0.05;//.25f;
				break;
			}
		}
		
		node.penalise(penalty);
	}
}
