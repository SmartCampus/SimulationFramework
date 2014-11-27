package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class RandomSensorTransformation extends
        SensorTransformation<Object, Boolean> {

    @Override
    public Boolean transform(final Object o, final Boolean last) {
        Random r = new Random();
        return r.nextBoolean();
    }
}
