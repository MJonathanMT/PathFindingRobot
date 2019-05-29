package mycontroller.router;

import exceptions.UnsupportedModeException;
import mycontroller.penalty.PenaltyFactory;

public class RouterFactory {
	public static IRouter getRouter() throws UnsupportedModeException {
		return new UniformCostRouter(PenaltyFactory.getCurrentPenalty());
	}
}