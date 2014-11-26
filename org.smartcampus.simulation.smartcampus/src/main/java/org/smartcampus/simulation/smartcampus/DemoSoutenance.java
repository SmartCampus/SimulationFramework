package org.smartcampus.simulation.smartcampus;

import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.smartcampus.law.ParkingMarkovLaw;
import org.smartcampus.simulation.smartcampus.simulation.ParkingMarkovSimulation;
import org.smartcampus.simulation.stdlib.sensors.RateToBooleanChangeSensorTransformation;
import scala.concurrent.duration.Duration;

public class DemoSoutenance {

    public static void main(final String[] args) {

        Start sim = new StartImpl();
        sim.createSimulation("Parking1", ParkingMarkovSimulation.class)
                .withSensors(10, new RateToBooleanChangeSensorTransformation(),0.1)
                .withLaw(new ParkingMarkovLaw(10, 0.1, 0.01))
                .setOutput("bob")
                .startAt("2014-02-07 11:25:00")
                .duration(Duration.create(2, TimeUnit.MINUTES))
                .frequency(Duration.create(5, TimeUnit.SECONDS)).startVirtualSimulation();

    }
}
