/**
 * 
 */
package org.smartcampus.simulation.stdlib.laws;

import javax.management.BadAttributeValueExpException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class MarkovChainTest {

    private static MarkovChain m1;

    @BeforeClass
    public static void setUpClass() {
        double[][] m = { { 0.3, 0.4, 0.3 }, { 0.2, 0.2, 0.6 }, { 0.2, 0.2, 0.6 } };
        try {
            m1 = new MarkovChain(m);
        } catch (Exception e) {
            Assert.fail("bad initialisation value");
        }
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#evaluate(java.lang.Integer[])}
     * .
     */
    @Test
    public void testEvaluate() {
        try {
            Assert.assertEquals(0.3, m1.evaluate(0, 0), 0.1);
            Assert.assertEquals(0.3, m1.evaluate(2, 2), 0.6);
            Assert.assertEquals(0.2, m1.evaluate(1, 0), 0.1);
        } catch (Exception e) {
            Assert.fail();
        }
    }

    @Test(expected = BadAttributeValueExpException.class)
    public void testConstructorExceptionSize() throws Exception {
        double[][] mat = { { 0.3, 0.4 }, { 0.2, 0.2 }, { 0.2, 0.2 } };
        new MarkovChain(mat);
    }

    @Test(expected = BadAttributeValueExpException.class)
    public void testConstructorExceptionProbabilities() throws Exception {
        double[][] mat = { { 0.3, 14 }, { 0.2, 0.2 } };
        new MarkovChain(mat);
    }

    @Test(expected = BadAttributeValueExpException.class)
    public void testEvaluateAttributeException1() throws Exception {
        m1.evaluate(1, 1, 1);
    }

    @Test(expected = BadAttributeValueExpException.class)
    public void testEvaluateAttributeException2() throws Exception {
        m1.evaluate(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEvaluateIndexException1() throws Exception {
        m1.evaluate(3, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEvaluateIndexException2() throws Exception {
        m1.evaluate(-1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEvaluateIndexException3() throws Exception {
        m1.evaluate(3, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testEvaluateIndexException4() throws Exception {
        m1.evaluate(2, 3);
    }

}
