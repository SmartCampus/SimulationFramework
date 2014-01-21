package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * Created by foerster on 21/01/14.
 */
public interface Start {
    public SimulationLawWrapper0 create(final String simulationName,final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass);
}
