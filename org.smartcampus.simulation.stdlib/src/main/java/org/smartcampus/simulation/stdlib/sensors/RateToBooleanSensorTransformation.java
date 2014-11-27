package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Transforms a rate to a boolean (true with rate chances)
 */
public class RateToBooleanSensorTransformation extends
        SensorTransformation<Double, Boolean> {

    @Override
    /**
     * @inheritDoc
     * has rate*100% chances to return true
     * rate should be between 0(returns false) and 1(returns true)
     */
    public Boolean transform(Double rate, final Boolean last) {
        if (rate == 0) {
            return false;
        }
        rate = Math.abs(rate);
        Random r = new Random();
        return r.nextFloat() < rate;
    }
}
