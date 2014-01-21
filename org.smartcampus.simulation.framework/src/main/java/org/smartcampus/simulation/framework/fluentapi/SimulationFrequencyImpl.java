package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationFrequencyImpl extends SimulationWrapper implements SimulationFrequency {

    private int start;
    private int duration;

    public SimulationFrequencyImpl(ActorRef controllerRef,int start,int duration) {
        super(controllerRef);
        this.start = start;
        this.duration = duration;
    }

    @Override
    public End frequency(int frequency) {
        return new EndImpl(controllerRef,start,duration,frequency);
    }
}
