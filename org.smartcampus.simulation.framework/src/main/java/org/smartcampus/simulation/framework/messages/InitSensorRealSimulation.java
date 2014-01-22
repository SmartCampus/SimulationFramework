package org.smartcampus.simulation.framework.messages;

/**
 * 
 * The InitSensorVirtualSimulation message allows the setting of the url in the
 * SimulationLaw and the DataSender
 * 
 */
public class InitSensorRealSimulation {
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
