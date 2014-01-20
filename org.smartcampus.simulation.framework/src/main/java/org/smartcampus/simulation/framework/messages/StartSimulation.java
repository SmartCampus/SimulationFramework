package org.smartcampus.simulation.framework.messages;

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
