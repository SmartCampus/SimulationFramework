package org.smartcampus.simulation.framework.fluentapi.simulation;

import java.util.Date;

/**
 * Created by foerster on 21/01/14.
 */
public interface End {
    public void startRealTimeSimulationNow();

    public void startRealTimeSimulationAt(long time);

    public void startRealTimeSimulationAt(String date);

    public void startVirtualSimulation();
}
