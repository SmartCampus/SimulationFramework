package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationStartImpl extends SimulationWrapper implements SimulationStart {

    public SimulationStartImpl(ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationDuration start(int date) {
        return new SimulationDurationImpl(controllerRef,date);
    }
}
