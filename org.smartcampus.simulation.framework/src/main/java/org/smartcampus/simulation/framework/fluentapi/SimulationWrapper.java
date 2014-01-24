package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public abstract class SimulationWrapper extends SimulatorWrapper {
    protected String simulationLawName ;

    public SimulationWrapper(String simulationLawName , ActorRef controllerRef){
        super(controllerRef);
        this.simulationLawName = simulationLawName ;
    }

}
