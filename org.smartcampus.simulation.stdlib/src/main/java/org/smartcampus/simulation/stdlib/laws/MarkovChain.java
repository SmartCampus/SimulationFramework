package org.smartcampus.simulation.stdlib.laws;

import javax.management.BadAttributeValueExpException;
import org.smartcampus.simulation.framework.simulator.Law;

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
            throw new BadAttributeValueExpException("Matrix must be a square");
        }
        this.size = s;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s; j++) {
                if ((m[i][j] < 0) || (m[i][j] > 1)) {
                    throw new BadAttributeValueExpException(
                            "The matrix must be composed of probabilities (0<p<1)");
                }
            }
        }
        this.transition = m;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    protected Double evaluate(final Integer... x) throws Exception {
        if (x.length != 2) {
            // too much arguments or not enough
            throw new BadAttributeValueExpException("you need to give two ints");
        }
        int i = x[0]; // current state
        int j = x[1]; // state we want to go to
        return this.transition[i][j];
    }

}
