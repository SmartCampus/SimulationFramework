package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.simulator.Law;

/**
 * Created by foerster on 21/01/14.
 */
public interface SimulationLawWrapper1 {
    public Simulator withLaw(final Law<?, ?> initVal) ;
}
