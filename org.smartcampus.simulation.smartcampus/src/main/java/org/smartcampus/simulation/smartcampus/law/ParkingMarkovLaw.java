package org.smartcampus.simulation.smartcampus.law;

import org.smartcampus.simulation.stdlib.law.MarkovChain;

/**
 * Describes a parking occupancy states chain. It is constructed with the arrival
 * frequency of cars in the parking and multiplicative inverse of the average time spent
 * in the parking
 * The states matrix is constructed using the formula given in the paper "Predicting
 * Parking Lot Occupancy in Vehicular Ad Hoc Networks" by Murat Caliskan, Andreas
 * Barthels, Björn Scheuermann and Martin Mauve
 * The matrix diagonal is valued so that Mii=-sum(Mij) (j=0->size,j!=i)
 */
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
     *            multiplicative inverse of the average time spent in the parking
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
    /**
     * @inheritDoc
     */
    protected Double evaluate(final Integer... x) throws Exception {
        return super.evaluate(x);
    }

}
