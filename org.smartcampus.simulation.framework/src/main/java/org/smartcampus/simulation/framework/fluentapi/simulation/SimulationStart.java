package org.smartcampus.simulation.framework.fluentapi.simulation;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationStart {
    public SimulationDuration startAt(String date);

    public SimulationDuration startAt(long date);

}
