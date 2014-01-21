package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class SensorTransformationBooleanPercent implements
        SensorTransformation<Double, Boolean> {

    @Override
    public Boolean transform(Double res, final Boolean last) {
        if (res == 0) {
            return false;
        }
        res = Math.abs(res);
        Random r = new Random();
        return r.nextInt(100) < res;
    }

}
