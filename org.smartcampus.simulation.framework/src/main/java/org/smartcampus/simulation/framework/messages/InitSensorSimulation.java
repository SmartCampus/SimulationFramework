package org.smartcampus.simulation.framework.messages;

import akka.actor.ActorRef;

public class InitSensorSimulation {
    private final ActorRef dataMaker;

    public InitSensorSimulation(final ActorRef a) {
        this.dataMaker = a;
    }

    public ActorRef getDataMaker() {
        return this.dataMaker;
    }
}
