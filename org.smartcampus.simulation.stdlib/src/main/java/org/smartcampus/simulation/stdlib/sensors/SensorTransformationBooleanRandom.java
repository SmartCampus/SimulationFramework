package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class SensorTransformationBooleanRandom implements
        SensorTransformation<Double, Boolean> {

    @Override
    public Boolean transform(final Double p, final Boolean last) {
        Random r = new Random();
        return r.nextBoolean();
    }
}
