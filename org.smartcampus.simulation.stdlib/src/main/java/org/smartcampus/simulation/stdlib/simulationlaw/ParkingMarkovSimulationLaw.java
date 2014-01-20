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
        int nbPlacesOccuped = 0;
        for (Boolean b : this.values) {
            if (b) {
                nbPlacesOccuped++;
            }
        }
        this.sendValue("Average", ((100 * nbPlacesOccuped) / this.values.size()) + "%");
    }
}
