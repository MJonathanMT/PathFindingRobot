package mycontroller.penalty;

import swen30006.driving.Simulation;
import world.Car;
import exceptions.UnsupportedModeException;

public class PenaltyFactory {
	public static IPenalty getCurrentPenalty() throws UnsupportedModeException {
		if (Simulation.toConserve().equals(Simulation.StrategyMode.HEALTH)) {
			return HealthPenalty.getInstance();
		} else {
			return FuelPenalty.getInstance();
		}
	}
}