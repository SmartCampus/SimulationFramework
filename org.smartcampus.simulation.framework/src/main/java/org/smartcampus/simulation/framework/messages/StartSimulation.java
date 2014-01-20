package org.smartcampus.simulation.framework.messages;

/**
 * The StartSimulation message allows to launch the simulation on all the SimulationLaw.
 * It contains the start time of the simulation, the duration and the interval of time
 * between each request.
 */
public class StartSimulation {
    private final int begin;
    private final int duration;
    private final int interval;

    public StartSimulation(final int b, final int d, final int i) {
        this.begin = b;
        this.duration = d;
        this.interval = i;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getInterval() {
        return this.interval;
    }

}
