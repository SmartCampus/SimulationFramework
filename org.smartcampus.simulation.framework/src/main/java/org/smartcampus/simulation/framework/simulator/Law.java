/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;

/**
 * This class allows to implement mathematical models
 * 
 * @param <S>
 *            corresponds to the type of the parameter of the method 'evaluate'
 * 
 * @param <T>
 *            corresponds to the return type of the method 'evaluate'
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
