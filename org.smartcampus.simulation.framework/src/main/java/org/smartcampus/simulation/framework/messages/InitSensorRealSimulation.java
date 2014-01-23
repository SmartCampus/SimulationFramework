package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * 
 * The InitSensorVirtualSimulation message allows the setting of the url in the
 * SimulationLaw and the DataSender
 * 
 */
public class InitSensorRealSimulation implements Serializable {
    private static final long serialVersionUID = -4757997038007842114L;
    private final String url;

    public InitSensorRealSimulation(final String url) {
        this.url = url;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

}
