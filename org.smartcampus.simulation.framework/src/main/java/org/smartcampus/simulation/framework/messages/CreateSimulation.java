package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import org.smartcampus.simulation.framework.simulator.Simulation;

/**
 * The CreateSimulationLaw message allows to create a new SimulationLaw according to the
 * 'simulationLawClass'
 */
public class CreateSimulation implements Serializable {
    private static final long serialVersionUID = -4644784620359844269L;
    private final String name;
    private final Class<? extends Simulation<?>> simulationClass;

    public CreateSimulation(final String n,
            final Class<? extends Simulation<?>> simulationClass) {
        this.name = n;
        this.simulationClass = simulationClass;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends Simulation<?>> getSimulationClass() {
        return this.simulationClass;
    }

}
