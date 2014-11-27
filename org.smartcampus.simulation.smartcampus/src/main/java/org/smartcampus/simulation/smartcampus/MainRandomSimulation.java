package org.smartcampus.simulation.smartcampus;

import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.fluentapi.Start;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.smartcampus.simulation.ParkingSimulation;
import org.smartcampus.simulation.stdlib.sensors.RandomSensorTransformation;
import scala.concurrent.duration.Duration;

public class MainRandomSimulation {

    public static void main(final String[] args) {
        String pfeIP = "http://localhost:8080/collector/value";
        String pfeIP_online = "http://54.229.14.230:8080/collector/value";
        long time = System.currentTimeMillis() + 5000;
        Start sim = new StartImpl();
        sim.createSimulation("Random", ParkingSimulation.class)
                .withSensors(300, new RandomSensorTransformation())
                .withLaw(null)
                .setOutput(pfeIP)
                .startAt(time)
                .duration(Duration.create(30, TimeUnit.SECONDS))
                .frequency(Duration.create(5, TimeUnit.SECONDS)).startRealTimeSimulationAt(time);
    }

}
