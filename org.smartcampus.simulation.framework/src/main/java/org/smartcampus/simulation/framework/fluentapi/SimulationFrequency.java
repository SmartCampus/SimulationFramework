package org.smartcampus.simulation.framework.fluentapi;

import scala.concurrent.duration.FiniteDuration;

/**
 * Created by foerster on 22/01/14.
 */
public interface SimulationFrequency {
    public End frequency(FiniteDuration frequency);
}
