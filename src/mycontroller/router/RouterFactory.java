package mycontroller.router;

import mycontroller.penalty.PenaltyFactory;

public class RouterFactory {
	public static IRouter getRouter() {
		return new UniformCostRouter(PenaltyFactory.getCurrentPenalty());
	}
}