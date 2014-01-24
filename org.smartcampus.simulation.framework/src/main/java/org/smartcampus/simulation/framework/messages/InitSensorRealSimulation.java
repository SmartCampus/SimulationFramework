package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import akka.actor.ActorRef;

/**
 * 
 * The InitSensorVirtualSimulation message allows the setting of the url in the
 * SimulationLaw and the DataSender
 * 
 */
public class InitSensorRealSimulation implements Serializable {
    private static final long serialVersionUID = -4757997038007842114L;
    private final String url;
    private ActorRef counter;

    public InitSensorRealSimulation(final String url, final ActorRef counter) {
        this.url = url;
        this.counter = counter;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    public ActorRef getCounter() {
        return this.counter;
    }

}
