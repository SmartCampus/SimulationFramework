package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.Law;

public class InitParking {
	private final String name;
	private final Law<?, ?> initVal;

	public InitParking(String n, Law<?, ?> initVal2) {
		this.name = n;
		this.initVal = initVal2;
	}

	public String getName() {
		return name;
	}

	public Law<?, ?> getInitVal() {
		return initVal;
	}

}
