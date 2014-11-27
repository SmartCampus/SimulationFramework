package org.smartcampus.simulation.smartcampus;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.smartcampus.simulation.ParkingSimulation;
import org.smartcampus.simulation.stdlib.law.PolynomialLaw;
import org.smartcampus.simulation.stdlib.sensors.PercentToBooleanSensorTransformationOnEvent;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class MainParkingSimulationLawOnEvent {

    public static void main(final String[] args) {
        String pfeIP = "http://localhost:8080/collector/value";
        String pfeIP_online = "http://54.229.14.230:8080/collector/value";
        Law<Double, Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
                3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907);

        Start sim = new StartImpl();
        sim.createSimulation("Parking1", ParkingSimulation.class)
                .withSensors(5, new PercentToBooleanSensorTransformationOnEvent())
                .withLaw(polynome).setOutput(pfeIP_online)
                .startAt(System.currentTimeMillis())
                .duration(Duration.create(10, TimeUnit.SECONDS))
                .frequency(Duration.create(1, TimeUnit.SECONDS)).startRealTimeSimulationNow();
    }
}
