package mycontroller.penalty;

import mycontroller.router.Node;

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
		// nothing, optimise distance, which is default anyway
	}
}
