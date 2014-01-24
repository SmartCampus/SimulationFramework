package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulatorWrapper {
    protected ActorRef controllerRef;

    public SimulatorWrapper(ActorRef controllerRef){
        this.controllerRef = controllerRef ;
    }
}
