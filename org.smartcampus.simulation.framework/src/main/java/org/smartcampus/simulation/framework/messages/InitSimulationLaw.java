package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import org.smartcampus.simulation.framework.simulator.Law;

/**
 * The InitSimulationLaw message allows to initiate a SimulationLaw with a Law
 */
public class InitSimulationLaw implements Serializable {
    private static final long serialVersionUID = 218365230248630955L;
    private final String      name;
    private final Law<?, ?>   initVal;

    public InitSimulationLaw(final String n, final Law<?, ?> initVal2) {
        this.name = n;
        this.initVal = initVal2;
    }

    public Law<?, ?> getLaw() {
        return this.initVal;
    }

    public String getName() {
        return this.name;
    }

}
