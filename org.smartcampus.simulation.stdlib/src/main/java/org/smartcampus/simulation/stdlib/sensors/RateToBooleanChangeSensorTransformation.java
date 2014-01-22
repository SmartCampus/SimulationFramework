package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Transforms a rate to a boolean (changes the last value with rate*100% chances)
 */
public class RateToBooleanChangeSensorTransformation implements
        SensorTransformation<Double, Boolean> {

    @Override
    /**
     * @inheritDoc
     * has rate*100% chances to return the opposite of its last value
     * if it hasn't any last value, consider it was false
     * rate should be between 0(returns false) and 1(returns true)
     */
    public Boolean transform(Double rate, Boolean last) {
        if (rate == 0) {
            return false;
        }
        if (last == null) {
            last = false;
        }
        rate = Math.abs(rate);
        Random r = new Random();
        if (r.nextFloat() < rate) {
            return !last;
        }
        return last;
    }
}
