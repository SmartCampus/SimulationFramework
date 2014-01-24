package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.simulator.Replay;
import org.smartcampus.simulation.framework.simulator.Simulation;

/**
 * Created by foerster on 21/01/14.
 */
public interface Start {
    public SimulationLawWrapper0 create(final String simulationName,
            final Class<? extends Simulation<?>> simulationClass);

    public ReplayWrapper0 replay(String string, Class<? extends Replay> replayClass);
}
