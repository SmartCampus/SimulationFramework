package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationFrequencyImpl extends SimulatorWrapper implements
        SimulationFrequency {

    private long           start;
    private FiniteDuration duration;

    public SimulationFrequencyImpl(final ActorRef controllerRef, final long start,
            final FiniteDuration duration) {
        super(controllerRef);
        this.start = start;
        this.duration = duration;
    }

    @Override
    public End frequency(final FiniteDuration frequency) {
        return new EndImpl(this.controllerRef, this.start, this.duration, frequency);
    }
}
