/**
 * 
 */
package org.smartcampus.simulation.stdlib.laws;

import junit.framework.TestCase;

/**
 * @author Jerome Rancati
 *
 */
public class PolynomialLawTest extends TestCase {
    private PolynomialLaw p, p1,p2;

    protected void setUp() throws Exception {
        this.p=new PolynomialLaw(2.,2.,2.);
        this.p1 = new PolynomialLaw(1.,1.);
        this.p2 = new PolynomialLaw(1.,2.,3.);
    }

    /**
     * Test method for {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#evaluate(java.lang.Double)}.
     */
    public void testEvaluateDouble() {
        assertEquals(1., p1.evaluate(0.));
        assertEquals(2., p1.evaluate(1.));
        assertEquals(3., p1.evaluate(2.));
        assertEquals(2., p.evaluate(0.));
        assertEquals(6., p.evaluate(1.));
        assertEquals(14., p.evaluate(2.));
        assertEquals(1., p2.evaluate(0.));
        assertEquals(6., p2.evaluate(1.));
        assertEquals(17., p2.evaluate(2.));
    }

    /**
     * Test method for {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#getOriginalOrdonee()}.
     */
    public void testGetOriginalOrdonee() {
        assertEquals(2., p.getOriginalOrdonee());
        assertEquals(1., p1.getOriginalOrdonee());
        assertEquals(1., p2.getOriginalOrdonee());
    }

    /**
     * Test method for {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#getCoeficients()}.
     */
    public void testGetCoeficients() {
        assertEquals(3, p.getCoeficients().size());
        assertEquals(2, p1.getCoeficients().size());
        assertEquals(3, p2.getCoeficients().size());
    }

}
