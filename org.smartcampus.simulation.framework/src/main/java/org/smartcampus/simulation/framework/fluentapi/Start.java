package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.fluentapi.replay.ReplayWrapper0;
import org.smartcampus.simulation.framework.fluentapi.simulation.SimulationLawWrapper0;
import org.smartcampus.simulation.framework.simulator.FileFormator;
import org.smartcampus.simulation.framework.simulator.Simulation;

/**
 * Created by foerster on 21/01/14.
 */
public interface Start {

    public SimulationLawWrapper0 create(final String simulationName,
            final Class<? extends Simulation<?>> simulationClass);

    public ReplayWrapper0 replay(String string, Class<? extends FileFormator> class1);

}
