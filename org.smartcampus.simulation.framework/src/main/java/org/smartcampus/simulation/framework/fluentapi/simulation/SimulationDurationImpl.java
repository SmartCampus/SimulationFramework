package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationDurationImpl extends SimulatorWrapper implements
        SimulationDuration {

    private long start;

    public SimulationDurationImpl(final ActorRef controllerRef, final long date) {
        super(controllerRef);
        this.start = date;
    }

    @Override
    public SimulationFrequency duration(final FiniteDuration duration) {
        return new SimulationFrequencyImpl(this.controllerRef, this.start, duration);
    }
}
