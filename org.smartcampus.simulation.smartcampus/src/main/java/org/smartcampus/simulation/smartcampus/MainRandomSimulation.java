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
        sim.create("Random", ParkingSimulation.class)
                .add(5, new RandomSensorTransformation()).withLaw(null)
                .setOutput("http://localhost:8000/value")
                .start(System.currentTimeMillis())
                .duration(Duration.create(1, TimeUnit.DAYS))
                .frequency(Duration.create(1, TimeUnit.HOURS))
                .simulateVirtual();
    }

}
