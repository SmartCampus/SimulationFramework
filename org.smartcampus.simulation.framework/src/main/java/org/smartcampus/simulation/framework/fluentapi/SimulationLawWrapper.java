package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public abstract class SimulationLawWrapper extends SimulationWrapper {
    protected String simulationLawName ;

    public SimulationLawWrapper(String simulationLawName , ActorRef controllerRef){
        super(controllerRef);
        this.simulationLawName = simulationLawName ;
    }

}
