package org.smartcampus.simulation.framework.messages;

/**
 * The StartSimulation message allows to launch the simulation on all the SimulationLaw.
 * It contains the start time of the simulation, the duration and the interval of time
 * between each request.
 */
public class InitTypeSimulation {
    private final int begin;
    private final int duration;
    private final int realTimeFrequency;
    private final int frequency;

    public InitTypeSimulation(final int b, final int d, final int f, final int r) {
        this.begin = b;
        this.duration = d;
        this.realTimeFrequency = r;
        this.frequency = f;
    }

    public int getBegin() {
        return this.begin;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getRealTimeFrequency() {
        return this.realTimeFrequency;
    }

    public int getFrequency() {
        return this.frequency;
    }

}
