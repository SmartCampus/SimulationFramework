/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;

/**
 * @author Jerome Rancati
 * @creationDate 15 January 2014
 * 
 */
public abstract class Law<S, T> {

    /**
     * evaluate the law at the value x
     * 
     * @param x
     *            the value where the law has to be evaluate
     * @return the evaluation of the law at the value x
     * @throws Exception
     */
    protected abstract T evaluate(S... x) throws Exception;
}
