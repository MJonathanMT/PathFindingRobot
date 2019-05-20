package mycontroller.strategy;

import swen30006.driving.Simulation;
import world.Car;
import exceptions.UnsupportedModeException;

public class StrategyFactory {
	public static Strategy getCurrentStrategy() throws UnsupportedModeException {
		if (Simulation.toConserve().equals(Simulation.StrategyMode.HEALTH)) {
			return new HealthStrategy();
		} else {
			return new FuelStrategy();
		}
	}
}