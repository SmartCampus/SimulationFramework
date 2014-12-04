package org.smartcampus.simulation.stdlib.sensors;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

import java.util.Random;

public class RandomRateSensorTransformation extends
        SensorTransformation<Object, Double> {

    @Override
    public Double transform(final Object o, final Double last) {
        Random r = new Random();
        return r.nextDouble();
    }
}
