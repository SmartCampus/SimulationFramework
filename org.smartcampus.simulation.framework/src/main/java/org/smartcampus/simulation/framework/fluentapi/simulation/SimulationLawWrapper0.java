package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationLawWrapper0 {
    public SimulationLawWrapper1 withSensors(final int nbsensors,final SensorTransformation<?, ?> transformation,Object delta);
}
