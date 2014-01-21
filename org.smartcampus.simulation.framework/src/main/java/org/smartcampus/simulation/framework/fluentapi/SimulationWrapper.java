package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulationWrapper {
    protected ActorRef controllerRef;

    public SimulationWrapper(ActorRef controllerRef){
        this.controllerRef = controllerRef ;
    }
}
