package org.smartcampus.simulation.stdlib.sensors;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

import java.util.Random;

public class RandomBooleanEventSensorTransformation extends
        SensorTransformation<Object, Boolean> {

    @Override
    public Boolean transform(final Object o, final Boolean last) {
        Random r = new Random();
        return r.nextBoolean();
    }

    @Override
    public boolean hasToSendData(Boolean lastVal, Boolean currentValue) {
        return lastVal != currentValue;
    }
}
