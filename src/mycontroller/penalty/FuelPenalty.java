package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.*;

public class FuelPenalty implements IPenalty {
	private static FuelPenalty instance = null;

	private FuelPenalty() {
	}

	public static FuelPenalty getInstance() {
		if (instance == null) {
			instance = new FuelPenalty();
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
				penalty -= .5f;
				break;
			}
		}
		
		node.penalise(penalty);
	}
}
