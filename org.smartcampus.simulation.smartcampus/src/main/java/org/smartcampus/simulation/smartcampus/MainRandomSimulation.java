package org.smartcampus.simulation.smartcampus;

import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.smartcampus.simulation.ParkingSimulation;
import org.smartcampus.simulation.stdlib.sensors.RandomSensorTransformation;
import scala.concurrent.duration.Duration;

public class MainRandomSimulation {

    public static void main(final String[] args) {
        Start sim = new StartImpl();
        sim.createSimulation("Random", ParkingSimulation.class)
                .withSensors(350, new RandomSensorTransformation()).withLaw(null)
                .setOutput("http://localhost:8080/collector/value")
                .start(System.currentTimeMillis())

                .duration(Duration.create(5, TimeUnit.MINUTES))
                .frequency(Duration.create(1, TimeUnit.SECONDS)).simulateReal();

    }

}
