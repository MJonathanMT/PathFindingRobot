package mycontroller.router;

import exceptions.UnsupportedModeException;

public class RouterFactory {
	public static IRouter getRouter() throws UnsupportedModeException {
		return new UniformCostRouter();
	}
}