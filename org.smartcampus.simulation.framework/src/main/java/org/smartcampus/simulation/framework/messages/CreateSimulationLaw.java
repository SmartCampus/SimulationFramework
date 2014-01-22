package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * The CreateSimulationLaw message allows to create a new SimulationLaw according to the
 * 'simulationLawClass'
 */
public class CreateSimulationLaw implements Serializable {
    private static final long                             serialVersionUID = -4644784620359844269L;
    private final String                                  name;
    private final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass;

    public CreateSimulationLaw(final String n,
            final Class<? extends SimulationLaw<?, ?, ?>> c) {
        this.name = n;
        this.simulationLawClass = c;
    }

    public String getName() {
        return this.name;
    }

    public Class<? extends SimulationLaw<?, ?, ?>> getSimulationLawClass() {
        return this.simulationLawClass;
    }

}
