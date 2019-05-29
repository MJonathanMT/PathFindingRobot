package mycontroller.penalty;

import mycontroller.router.INode;
import tiles.MapTile;
import tiles.TrapTile;

public interface IPenalty {
	public static final float DEFAULT_PENALTY = 1.0f;
	
	public default void penalise(INode node) {
		MapTile tile = node.getTile();
		float penalty = DEFAULT_PENALTY;
		
		if (node.getTile().isType(MapTile.Type.TRAP)) {
			TrapTile trap = (TrapTile) tile;
			
			penalty += getPenalty(trap.getTrap());
		}
		
		node.applyPenalty(penalty);
	}
	
	float getPenalty(String trap);
}
