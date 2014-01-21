package org.smartcampus.simulation.stdlib;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.sensors.SensorTransformationBooleanPercent;
import org.smartcampus.simulation.stdlib.sensors.SensorTransformationBooleanRate;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingMarkovSimulationLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;

public class MainParkingMarkovSimulationLaw {

    public static void main(final String[] args) {
        Law<Integer, Double> markov = null;
        markov = new MarkovStatesLaw(30, 0.1, 0.01);

        Law<Double, Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
                3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();
        sim.create("Parking2", ParkingMarkovSimulationLaw.class)
                .add(30, new SensorTransformationBooleanRate()).init(markov)
                .create("Parking3", ParkingSimulationLaw.class)
                .add(30, new SensorTransformationBooleanPercent()).init(polynome)
                .setUrl("Not Used").simulateReal(10, 10, 1);
    }
}
