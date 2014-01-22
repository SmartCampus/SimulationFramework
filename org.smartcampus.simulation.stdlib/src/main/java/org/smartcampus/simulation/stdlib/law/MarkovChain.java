package org.smartcampus.simulation.stdlib.law;

import java.security.InvalidParameterException;
import org.smartcampus.simulation.framework.simulator.Law;

/**
 * Describes a MarkovChain with the state transition matrix
 * 
 */
public class MarkovChain extends Law<Integer, Double> {

    protected int        size;
    protected double[][] transition;

    /**
     * creates a default m x m transition matrix
     * 
     * @param m
     *            the size of the matrix
     */
    protected MarkovChain(final int m) {
        this.size = m;
        this.transition = new double[this.size][this.size];
    }

    /**
     * Creates a MarkovChain object from its states matrix
     * 
     * @param m
     *            matrix containing the markov probabilities
     * @throws Exception
     */
    public MarkovChain(final double[][] m) throws Exception {
        int s = m.length;
        if (m[0].length != s) {
            throw new InvalidParameterException("Matrix must be a square");
        }
        this.size = s;
        double line;
        for (int i = 0; i < s; i++) {
            line = 0;
            for (int j = 0; j < s; j++) {
                line += m[i][j];
                if ((m[i][j] < 0) || (m[i][j] > 1)) {
                    throw new InvalidParameterException(
                            "The matrix must be composed of probabilities (0<p<1)");
                }
            }
            if (line != 1) {
                throw new InvalidParameterException(
                        "Each line of the matrix must have 1 for sum");
            }
        }
        this.transition = m;
    }

    /**
     * @return the number of states of the Markov chain
     */
    public int getSize() {
        return this.size;
    }

    @Override
    /**
     * @inheritDoc
     * Must receive two integers
     */
    protected Double evaluate(final Integer... x) throws Exception {
        if (x.length != 2) {
            // too much arguments or not enough
            throw new InvalidParameterException("you need to give two ints");
        }
        int i = x[0]; // current state
        int j = x[1]; // state we want to go to
        return this.transition[i][j];
    }

}
