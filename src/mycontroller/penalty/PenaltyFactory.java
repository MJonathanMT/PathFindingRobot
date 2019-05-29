package mycontroller.penalty;

import swen30006.driving.Simulation;
import exceptions.UnsupportedModeException;

public class PenaltyFactory {
	/**
	 * Gets the current penalty applicable for the given simulation
	 * 
	 * @return IPenalty
	 * @throws UnsupportedModeException
	 */
	public static IPenalty getCurrentPenalty() {
		switch (Simulation.toConserve()) {
		case HEALTH:
			return new HealthPenalty();
		case FUEL:
			return new FuelPenalty();
		default:
			return null;
		}
	}
}