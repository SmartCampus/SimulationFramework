package org.smartcampus.simulation.framework.messages;

import scala.concurrent.duration.FiniteDuration;

/**
 * The StartSimulation message allows to launch the simulation on all the SimulationLaw.
 * It contains the start time of the simulation, the duration and the interval of time
 * between each request.
 */
public class InitTypeSimulation {
    private final long           begin;
    private final FiniteDuration duration;
    private final FiniteDuration realTimeFrequency;
    private final long           frequency;

    public InitTypeSimulation(final long begin, final FiniteDuration duration,
            final FiniteDuration realTimeFrequency, final long frequency) {
        this.begin = begin;
        this.duration = duration;
        this.realTimeFrequency = realTimeFrequency;
        this.frequency = frequency;
    }

    public long getBegin() {
        return this.begin;
    }

    public FiniteDuration getDuration() {
        return this.duration;
    }

    public FiniteDuration getRealTimeFrequency() {
        return this.realTimeFrequency;
    }

    public long getFrequency() {
        return this.frequency;
    }

}
