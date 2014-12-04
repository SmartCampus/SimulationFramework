package org.smartcampus.simulation.services;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartcampus.simulation.framework.fluentapi.StartImpl;
import org.smartcampus.simulation.framework.fluentapi.simulation.SimulationLawWrapper0;
import org.smartcampus.simulation.framework.fluentapi.simulation.SimulationLawWrapper1;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;
import org.smartcampus.simulation.smartcampus.simulation.ParkingSimulation;
import org.smartcampus.simulation.stdlib.sensors.RandomBooleanEventSensorTransformation;
import org.smartcampus.simulation.stdlib.sensors.RandomBooleanSensorTransformation;
import org.smartcampus.simulation.stdlib.sensors.RandomRateEventSensorTransformation;
import org.smartcampus.simulation.stdlib.sensors.RandomRateSensorTransformation;
import org.smartcampus.simulation.stdlib.simulation.RandomBooleanSimulation;
import org.smartcampus.simulation.stdlib.simulation.RandomRateSimulation;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

public class SimulationLauncher {

    public static void launchSimulation(String json, boolean events) throws JSONException {
        JSONObject o = new JSONObject(json);
        boolean virtual;
        try {
            virtual = o.getBoolean("virtual");
        } catch (JSONException e) {
            virtual = false;
        }

        SimulationLawWrapper0 simulation;
        SensorTransformation t;

        if (virtual) {
            simulation = new StartImpl().createSimulation(o.getString("name"), RandomRateSimulation.class);
            if (events)
                t = new RandomRateEventSensorTransformation();
            else
                t = new RandomRateSensorTransformation();
        } else {
            simulation = new StartImpl().createSimulation(o.getString("name"), RandomBooleanSimulation.class);
            if (events)
                t = new RandomBooleanEventSensorTransformation();
            else
                t = new RandomBooleanSensorTransformation();
        }
        simulation.withSensors(o.getInt("sensors"), t)
                .withLaw(null)
                .setOutput("http://54.229.14.230:8080/collector/value")
                .startAt(o.getLong("start"))
                .duration(Duration.create(o.getLong("duration"), TimeUnit.MILLISECONDS))
                .frequency(Duration.create(o.getLong("frequency"), TimeUnit.MILLISECONDS))
                .startRealTimeSimulationAt(o.getLong("start"));
    }

}
