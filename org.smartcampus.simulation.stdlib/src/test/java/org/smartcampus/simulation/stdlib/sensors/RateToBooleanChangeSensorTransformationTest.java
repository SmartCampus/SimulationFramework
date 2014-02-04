package org.smartcampus.simulation.stdlib.sensors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class RateToBooleanChangeSensorTransformationTest {

    @Test
    public void testTransformFalse() throws Exception {
        SensorTransformation<Double, Boolean> transformation = new RateToBooleanChangeSensorTransformation();
        assertFalse(transformation.transform(0., null));
        assertFalse(transformation.transform(0., false));
        assertTrue(transformation.transform(1., false));
    }

    @Test
    public void testTransformTrue() throws Exception {
        SensorTransformation<Double, Boolean> transformation = new RateToBooleanChangeSensorTransformation();
        assertFalse(transformation.transform(1., true));
        assertTrue(transformation.transform(0., true));
    }
}
