package org.smartcampus.simulation.smartcampus.law;

import javax.management.BadAttributeValueExpException;
import org.smartcampus.simulation.stdlib.laws.MarkovChain;

public class ParkingMarkovLaw extends MarkovChain {

    /**
     * Creation of the tri-diagonal transition corresponding to the markov chain depicting
     * the parking lot
     * 
     * @param nbPlaces
     *            the number of places in the parking
     * @param arrivalFreq
     *            the arrival frequency of cars in the parking
     * @param averageParkingTime
     *            multiplicative inverse of the average time spent int the parking
     * @throws BadAttributeValueExpException
     */
    public ParkingMarkovLaw(final int nbPlaces, final double arrivalFreq,
            final double averageParkingTime) {
        super(nbPlaces + 1);
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                // diagonal
                if (i == j) {
                    this.transition[i][i] = -((i * averageParkingTime) + arrivalFreq);
                }
                // upper diagonal
                else if (j == (i + 1)) {
                    this.transition[i][j] = arrivalFreq;
                }
                // lower diagonal
                else if (j == (i - 1)) {
                    this.transition[i][j] = i * averageParkingTime;
                }
            }
        }
        this.transition[nbPlaces][nbPlaces] = -(nbPlaces * averageParkingTime);
    }

    @Override
    protected Double evaluate(final Integer... x) throws Exception {
        return super.evaluate(x);
    }

}
