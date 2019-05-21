package mycontroller.penalty;

import mycontroller.router.Node;
import tiles.*;

public class HealthPenalty implements IPenalty {
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
			node.dist += 100;
		}
		if (node.tile instanceof WaterTrap || node.tile instanceof HealthTrap) {
			node.dist -= 0.5;
		}
	}
}
