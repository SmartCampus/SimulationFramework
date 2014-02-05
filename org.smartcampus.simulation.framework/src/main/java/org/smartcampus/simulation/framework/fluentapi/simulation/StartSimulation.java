package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.simulator.Simulation;

/**
 * Created by foerster on 21/01/14.
 */
public interface StartSimulation {
    public SimulationLawWrapper0 createSimulation(final String simulationName,
            final Class<? extends Simulation<?>> simulationClass);

}
