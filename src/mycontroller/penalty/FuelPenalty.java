package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.LavaTrap;

public class FuelPenalty implements IPenalty {
	// singleton
	public static FuelPenalty instance = null;
	
	private FuelPenalty() {
	}
	
	public static FuelPenalty getInstance() {
		if (instance == null) 
			instance = new FuelPenalty();
		
		return instance;
	}
	
	public void applyPenalty(Node node) {
		if (node.tile instanceof LavaTrap) {
			node.cost += 50;
		}
	}
}
