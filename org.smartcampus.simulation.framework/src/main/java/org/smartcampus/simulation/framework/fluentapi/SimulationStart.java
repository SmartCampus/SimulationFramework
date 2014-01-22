package org.smartcampus.simulation.framework.fluentapi;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationStart {
    public SimulationDuration start(String date);

    public SimulationDuration start(long date);

}
