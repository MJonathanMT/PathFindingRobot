package mycontroller.stratergy;

import swen30006.driving.Simulation;
import world.Car;

class StratergyFactory {
	public static Stratergy getCurrentStratergy() {
		if (Simulation.toConserve().equals(Simulation.StrategyMode.HEALTH)) {
			//return new HealthStratergy();
			throw new 
		} else {
			return new FuelStratergy();
		}
	}
}