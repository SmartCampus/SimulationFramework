package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class SensorTransformationBooleanRate implements
        SensorTransformation<Double, Boolean> {

    @Override
    public Boolean transform(Double p, final Boolean last) {
        if ((p == 0) || (last == null)) {
            return false;
        }
        p = Math.abs(p);
        Random r = new Random();
        if (r.nextFloat() < p) {
            return !last;
        }
        return last;
    }
}
