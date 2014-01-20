/**
 * 
 */
package org.smartcampus.simulation.stdlib.laws;

import javax.management.BadAttributeValueExpException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author user
 * 
 */
public class MarkovStatesLawTest {

    private static MarkovStatesLaw m1;

    @BeforeClass
    public static void setUpClass() {
        m1 = new MarkovStatesLaw(10, 0.2, 0.3);
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#evaluate(java.lang.Integer[])}
     * .
     */
    @Test
    public void testEvaluateIntegerArray() {
        try {
            for (int i = 0; i < 10; i++) {
                Assert.assertEquals(0.2, m1.evaluate(i, i + 1), 0.01);
            }
            for (int i = 1; i < 11; i++) {
                Assert.assertEquals(i * 0.3, m1.evaluate(i, i - 1), 0.01);
            }
            Assert.assertEquals(5 * 0.3, m1.evaluate(5, 4), 0.01);
            Assert.assertEquals(-0.2, m1.evaluate(0, 0), 0.01);
            Assert.assertEquals(-10 * 0.3, m1.evaluate(10, 10), 0.01);
            Assert.assertEquals(0., m1.evaluate(1, 6), 0.01);
            Assert.assertEquals(0., m1.evaluate(8, 6), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#MarkovStatesLaw(java.util.List, java.util.List, java.util.List)}
     * .
     */
    @Test
    public void testMarkovStatesLaw() {
        // assertEquals(5, this.m1.getSame().size);
    }

    @Test(expected = BadAttributeValueExpException.class)
    public void testEvaluateIntegerArrayBadAttributeValueExpException() throws Exception {
        m1.evaluate(3, 11);
        m1.evaluate(-1, 3);
        m1.evaluate(3, -1);
        m1.evaluate(11, 3);
    }

}
