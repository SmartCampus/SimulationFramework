package org.smartcampus.simulation.stdlib;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw;
import org.smartcampus.simulation.stdlib.sensors.SensorTransformationBooleanRate;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingMarkovSimulationLaw;

public class MainParkingMarkovSimulationLaw {

    public static void main(final String[] args) {
        Simulator s = new Simulator();
        Law<Integer, Double> markov = null;
        markov = new MarkovStatesLaw(30, 0.1, 0.01);
        s.create("Parking2", ParkingMarkovSimulationLaw.class)
                .addSensors("Parking2", new SensorTransformationBooleanRate(), 30)
                .initSimulation("Parking2", markov);
        s.simulate(10, 10, 1);
    }

}
