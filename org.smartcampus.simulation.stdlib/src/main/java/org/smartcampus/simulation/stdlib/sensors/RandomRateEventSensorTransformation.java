package org.smartcampus.simulation.stdlib.sensors;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

import java.util.Random;

public class RandomRateEventSensorTransformation extends
        SensorTransformation<Object, Double> {

    @Override
    public Double transform(final Object o, final Double last) {
        Random r = new Random();
        return r.nextDouble();
    }

    @Override
    public boolean hasToSendData(Double lastVal, Double currentValue) {
        return Math.abs(currentValue - lastVal) < 0.05;
    }
}
