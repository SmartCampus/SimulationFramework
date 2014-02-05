package org.smartcampus.simulation.stdlib.sensors;

import org.junit.Test;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by foerster
 * on 27/01/14.
 */
public class PercentToBooleanSensorTransformationTest {

    @Test
    public void testTransformFalse() throws Exception {
        SensorTransformation<Double,Boolean> transformation = new PercentToBooleanSensorTransformation();
        assertEquals(false,transformation.transform(0.,false));

    }

    @Test
    public void testTransformTrue() throws Exception {
        SensorTransformation<Double,Boolean> transformation = new PercentToBooleanSensorTransformation();
        assertTrue(transformation.transform(100.,true));
    }
}
