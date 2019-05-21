package mycontroller.penalty;

import swen30006.driving.Simulation;
import exceptions.UnsupportedModeException;

public class PenaltyFactory {
	/**
	 * Gets the current penalty applicable for the given simulation
	 * @return IPenalty
	 * @throws UnsupportedModeException
	 */
	public static IPenalty getCurrentPenalty() throws UnsupportedModeException {
		if (Simulation.toConserve().equals(Simulation.StrategyMode.HEALTH)) {
			return HealthPenalty.getInstance();
		} else {
			return FuelPenalty.getInstance();
		}
	}
}