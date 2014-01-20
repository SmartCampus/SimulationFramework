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
	private PolynomialLaw p, p1, p2;

	@Override
	protected void setUp() throws Exception {
		this.p = new PolynomialLaw(2., 2., 2.);
		this.p1 = new PolynomialLaw(1., 1.);
		this.p2 = new PolynomialLaw(1., 2., 3.);
	}

	/**
	 * Test method for
	 * {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#evaluate(java.lang.Double)}
	 * .
	 */
	public void testEvaluateDouble() {
		assertEquals(1., this.p1.evaluate(0.));
		assertEquals(2., this.p1.evaluate(1.));
		assertEquals(3., this.p1.evaluate(2.));
		assertEquals(2., this.p.evaluate(0.));
		assertEquals(6., this.p.evaluate(1.));
		assertEquals(14., this.p.evaluate(2.));
		assertEquals(1., this.p2.evaluate(0.));
		assertEquals(6., this.p2.evaluate(1.));
		assertEquals(17., this.p2.evaluate(2.));
	}

	/**
	 * Test method for
	 * {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#getCoeficients()}
	 * .
	 */
	public void testGetCoeficients() {
		assertEquals(3, this.p.getCoeficients().size());
		assertEquals(2, this.p1.getCoeficients().size());
		assertEquals(3, this.p2.getCoeficients().size());
	}

	/**
	 * Test method for
	 * {@link org.smartcampus.simulation.stdlib.laws.PolynomialLaw#getOriginalOrdonee()}
	 * .
	 */
	public void testGetOriginalOrdonee() {
		assertEquals(2., this.p.getOriginalOrdonee());
		assertEquals(1., this.p1.getOriginalOrdonee());
		assertEquals(1., this.p2.getOriginalOrdonee());
	}

}
