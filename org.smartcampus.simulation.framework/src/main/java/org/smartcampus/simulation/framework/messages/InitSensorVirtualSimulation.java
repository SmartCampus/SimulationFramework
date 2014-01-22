package org.smartcampus.simulation.framework.messages;

import akka.actor.ActorRef;

/**
 * 
 * The InitSensorVirtualSimulation message allows the setting of the DataWriter in the
 * SimulationLaw and the Sensors
 * 
 */
public class InitSensorVirtualSimulation {
    private final ActorRef dataMaker;

    public InitSensorVirtualSimulation(final ActorRef a) {
        this.dataMaker = a;
    }

    /**
     * @return the DataMaker
     */
    public ActorRef getDataMaker() {
        return this.dataMaker;
    }
}
