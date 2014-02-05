package org.smartcampus.simulation.stdlib.sensors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class RateToBooleanSensorTransformationTest {

    @Test
    public void testTransformFalse() throws Exception {
        SensorTransformation<Double, Boolean> transformation = new RateToBooleanSensorTransformation();
        assertFalse(transformation.transform(0., false));
    }

    @Test
    public void testTransformTrue() throws Exception {
        SensorTransformation<Double, Boolean> transformation = new RateToBooleanSensorTransformation();
        assertTrue(transformation.transform(1., true));
    }
}
