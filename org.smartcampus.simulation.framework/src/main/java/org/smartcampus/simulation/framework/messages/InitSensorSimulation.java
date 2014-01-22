package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import akka.actor.ActorRef;

public class InitSensorSimulation implements Serializable {
    private static final long serialVersionUID = -7749579532829942003L;
    private final ActorRef    dataMaker;

    public InitSensorSimulation(final ActorRef a) {
        this.dataMaker = a;
    }

    public ActorRef getDataMaker() {
        return this.dataMaker;
    }
}
