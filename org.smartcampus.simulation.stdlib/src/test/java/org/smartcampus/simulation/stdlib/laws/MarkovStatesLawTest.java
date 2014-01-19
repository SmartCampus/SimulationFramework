/**
 * 
 */
package org.smartcampus.simulation.stdlib.laws;

import java.util.ArrayList;
import java.util.List;

import javax.management.BadAttributeValueExpException;

import junit.framework.TestCase;

/**
 * @author user
 *
 */
public class MarkovStatesLawTest extends TestCase {

    private MarkovStatesLaw m1;

    protected void setUp() throws Exception {
        this.m1 = new MarkovStatesLaw(10, 0.2, 0.3);
    }

    /**
     * Test method for {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#MarkovStatesLaw(java.util.List, java.util.List, java.util.List)}.
     */
    public void testMarkovStatesLaw() {
        //assertEquals(5, this.m1.getSame().size);
    }

    /**
     * Test method for {@link org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw#evaluate(java.lang.Integer[])}.
     */
    public void testEvaluateIntegerArray() {
        try {
            for(int i=0;i<10;i++){
                assertEquals(0.2, m1.evaluate(i,i+1),0.01);
            }
            for(int i=1;i<11;i++){
                assertEquals(i*0.3, m1.evaluate(i,i-1),0.01);
            }
            assertEquals(5*0.3, m1.evaluate(5,4),0.01);
            assertEquals(-0.2, m1.evaluate(0,0),0.01);
            assertEquals(-10*0.3, m1.evaluate(10,10),0.01);
            assertEquals(0., m1.evaluate(1,6), 0.01);
            assertEquals(0., m1.evaluate(8,6), 0.01);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    @Test(expected = BadAttributeValueExpException.class)
    public void testEvaluateIntegerArrayBadAttributeValueExpException() throws BadAttributeValueExpException{
        this.m1.evaluate(3,11);
        this.m1.evaluate(-1,3);
        this.m1.evaluate(3,-1);
        this.m1.evaluate(11,3);
    }*/

}
