package org.smartcampus.simulation.smartcampus;

import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.smartcampus.law.ParkingMarkovLaw;
import org.smartcampus.simulation.smartcampus.simulation.ParkingMarkovSimulation;
import org.smartcampus.simulation.smartcampus.simulation.ParkingSimulation;
import org.smartcampus.simulation.stdlib.law.PolynomialLaw;
import org.smartcampus.simulation.stdlib.sensors.PercentToBooleanSensorTransformation;
import org.smartcampus.simulation.stdlib.sensors.RateToBooleanChangeSensorTransformation;
import scala.concurrent.duration.Duration;

public class MainParkingMarkovSimulationLaw {

    public static void main(final String[] args) {
        String pfeIP = "http://localhost:8080/collector/value";
        String pfeIP_online = "http://54.229.14.230:8080/collector/value";
        Law<Integer, Double> markov = null;
        markov = new ParkingMarkovLaw(500, 0.1, 0.01);

        Law<Double, Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
                3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();
        sim.createSimulation("Parking1", ParkingMarkovSimulation.class)
                .withSensors(500, new RateToBooleanChangeSensorTransformation())
                .withLaw(markov).createSimulation("Parking2", ParkingSimulation.class)
                .withSensors(500, new PercentToBooleanSensorTransformation())
                .withLaw(polynome).setOutput(pfeIP).startAt("2014-02-06 11:00:00")
                .duration(Duration.create(15, TimeUnit.SECONDS))
                .frequency(Duration.create(1, TimeUnit.SECONDS)).startRealTimeSimulationNow();
    }
}
