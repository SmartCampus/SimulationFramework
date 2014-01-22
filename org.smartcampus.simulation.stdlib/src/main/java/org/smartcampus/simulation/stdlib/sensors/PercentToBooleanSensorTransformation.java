package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Transforms a percentage to a boolean (true with percent% chances)
 */
public class PercentToBooleanSensorTransformation implements
        SensorTransformation<Double, Boolean> {

    @Override
    /**
     * @inheritDoc 
     * has percent% chances to return true
     * percent should be between 0(returns false) and 100(returns true)
     */
    public Boolean transform(Double percent, final Boolean last) {
        if (percent == 0) {
            return false;
        }
        percent = Math.abs(percent);
        Random r = new Random();
        return r.nextInt(100) < percent;
    }

}
