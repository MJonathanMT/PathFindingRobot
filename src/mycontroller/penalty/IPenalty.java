package mycontroller.penalty;

import mycontroller.router.INode;
import tiles.MapTile;
import tiles.TrapTile;

public interface IPenalty {
	public default void penalise(INode node) {
		MapTile tile = node.getTile();
		float penalty = 1.0f;
		
		if (node.getTile().isType(MapTile.Type.TRAP)) {
			TrapTile trap = (TrapTile) tile;
			
			penalty += getPenalty(trap.getTrap());
		}
		
		node.applyPenalty(penalty);
	}
	
	float getPenalty(String trap);
}
