package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationDurationImpl extends SimulationWrapper implements SimulationDuration {

    private int start;

    public SimulationDurationImpl(ActorRef controllerRef,int start) {
        super(controllerRef);
        this.start = start;
    }

    @Override
    public SimulationFrequency duration(int duration) {
        return new SimulationFrequencyImpl(controllerRef,start,duration);
    }
}
