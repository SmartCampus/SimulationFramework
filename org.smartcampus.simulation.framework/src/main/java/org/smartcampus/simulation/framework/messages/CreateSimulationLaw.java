package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

public class CreateSimulationLaw {
	
	private final String name;
	private final Class<? extends SimulationLaw<?, ?>> simulationLawClass;

	public CreateSimulationLaw(String n, Class<? extends SimulationLaw<?, ?>> c) {
		this.name = n;
		this.simulationLawClass = c;
	}

	public String getName() {
		return name;
	}

	public Class<? extends SimulationLaw<?, ?>> getSimulationLawClass() {
		return simulationLawClass;
	}

}
