/**
 * 
 */
package org.smartcampus.simulation.stdlib.law;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Jerome Rancati
 * 
 */
public class PolynomialLawTest {
    private PolynomialLaw p, p1, p2;

    @Before
    public void setUp() throws Exception {
        this.p = new PolynomialLaw(2., 2., 2.);
        this.p1 = new PolynomialLaw(1., 1.);
        this.p2 = new PolynomialLaw(1., 2., 3.);
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.law.PolynomialLaw#evaluate(java.lang.Double)}
     * .
     */
    @Test
    public void testEvaluateDouble() {
        Assert.assertEquals(1., this.p1.evaluate(0.), 0.01);
        Assert.assertEquals(2., this.p1.evaluate(1.), 0.01);
        Assert.assertEquals(3., this.p1.evaluate(2.), 0.01);
        Assert.assertEquals(2., this.p.evaluate(0.), 0.01);
        Assert.assertEquals(6., this.p.evaluate(1.), 0.01);
        Assert.assertEquals(14., this.p.evaluate(2.), 0.01);
        Assert.assertEquals(1., this.p2.evaluate(0.), 0.01);
        Assert.assertEquals(6., this.p2.evaluate(1.), 0.01);
        Assert.assertEquals(17., this.p2.evaluate(2.), 0.01);
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.law.PolynomialLaw#getCoeficients()} .
     */
    public void testGetCoeficients() {
        Assert.assertEquals(3, this.p.getCoefficients().size());
        Assert.assertEquals(2, this.p1.getCoefficients().size());
        Assert.assertEquals(3, this.p2.getCoefficients().size());
    }

    /**
     * Test method for
     * {@link org.smartcampus.simulation.stdlib.law.PolynomialLaw#getOriginalOrdonee()} .
     */
    public void testGetOriginalOrdonee() {
        Assert.assertEquals(2., this.p.getOriginalOrdonee(), 0.01);
        Assert.assertEquals(1., this.p1.getOriginalOrdonee(), 0.01);
        Assert.assertEquals(1., this.p2.getOriginalOrdonee(), 0.01);
    }

}
