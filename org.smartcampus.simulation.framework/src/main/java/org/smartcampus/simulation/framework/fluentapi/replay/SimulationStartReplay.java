package org.smartcampus.simulation.framework.fluentapi.replay;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationStartReplay {
    public EndReplay start(String date);

    public EndReplay start(long date);
}
