package org.smartcampus.simulation.smartcampus.law;

import java.security.InvalidParameterException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ParkingMarkovLawTest {

    private static ParkingMarkovLaw m1;
    private static final double     arrivalFreq        = 0.2;
    private static final double     averageParkingTime = 0.3;

    @BeforeClass
    public static void setUpClass() {
        m1 = new ParkingMarkovLaw(10, arrivalFreq, averageParkingTime);
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#evaluate(java.lang.Integer[])}
     * .
     */
    @Test
    public void testEvaluate() {
        try {
            for (int i = 0; i < 10; i++) {
                Assert.assertEquals(arrivalFreq, m1.evaluate(i, i + 1), 0.01);
            }
            for (int i = 1; i < 11; i++) {
                Assert.assertEquals(i * averageParkingTime, m1.evaluate(i, i - 1), 0.01);
            }
            Assert.assertEquals(5 * averageParkingTime, m1.evaluate(5, 4), 0.01);
            Assert.assertEquals(-arrivalFreq, m1.evaluate(0, 0), 0.01);
            Assert.assertEquals(-10 * averageParkingTime, m1.evaluate(10, 10), 0.01);
            Assert.assertEquals(0., m1.evaluate(1, 6), 0.01);
            Assert.assertEquals(0., m1.evaluate(8, 6), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = InvalidParameterException.class)
    public void testEvaluateAttributeException() throws Exception {
        m1.evaluate(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEvaluateIndexException() throws Exception {
        m1.evaluate(11, 2);
    }
}
