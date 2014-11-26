package org.smartcampus.simulation.smartcampus.simulation;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

public class ParkingMarkovSimulation extends SimulationLaw<Integer, Double, Boolean> {

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
        int nbPlacesOccupied = 0;
        for (Boolean b : this.values) {
            if (b) {
                nbPlacesOccupied++;
            }
        }
        this.sendValue("occupation", ((nbPlacesOccupied * 100) / this.values.size()) + "%");
    }
}
