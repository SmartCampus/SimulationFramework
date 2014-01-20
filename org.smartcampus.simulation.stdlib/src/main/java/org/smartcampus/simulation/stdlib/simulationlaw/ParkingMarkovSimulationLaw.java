package org.smartcampus.simulation.stdlib.simulationlaw;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

public class ParkingMarkovSimulationLaw extends SimulationLaw<Integer, Double, Boolean> {

    @Override
    protected Integer[] computeValue() {
        int i = 0;
        // counts the number of occupied parking spaces
        for (boolean b : this.values) {
            if (b) {
                i++;
            }
        }
        Integer[] t = { i, i };
        return t;
    }

    @Override
    protected void onComplete() {
        int i = 0;
        // counts the number of occupied parking spaces
        for (boolean b : this.values) {
            if (b) {
                i++;
            }
        }
        this.sendValue("occupation", ((i * 100) / this.values.size()) + "%");
    }
}
