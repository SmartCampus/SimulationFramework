package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.Law;

public class InitSimulationLaw {
    private final String    name;
    private final Law<?, ?> initVal;

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
