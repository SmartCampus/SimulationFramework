/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;


/**
 * @author Jerome Rancati 
 * @creationDate  15 January 2014
 *
 */
public abstract class Law<T, S> {
    
    /**
     * evaluate the law at the value x
     * @param x the value where the law has to be evaluate
     * @return the evaluation of the law at the value x
     */
    protected abstract S evaluate(T ...x);
}
