package org.smartcampus.simulation.stdlib;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.sensors.SensorTransformationBooleanPercent;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;

public class MainParkingSimulationLaw {

    public static void main(final String[] args) {
        Law<Double, Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
                3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();
        sim.create("Parking1",ParkingSimulationLaw.class)
                .add(5,new SensorTransformationBooleanPercent())
                .init(polynome)
           .setUrl("Not currently used")
           .simulate(10,10,1);


    }
}
