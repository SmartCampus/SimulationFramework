package org.smartcampus.simulation.framework.fluentapi.simulation;

import scala.concurrent.duration.FiniteDuration;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationDuration {
    public SimulationFrequency duration(FiniteDuration duration);
}
