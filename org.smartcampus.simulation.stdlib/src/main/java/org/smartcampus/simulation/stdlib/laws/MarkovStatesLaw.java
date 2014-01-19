package org.smartcampus.simulation.stdlib.laws;

import javax.management.BadAttributeValueExpException;

import org.smartcampus.simulation.framework.simulator.Law;

public class MarkovStatesLaw extends Law<Integer, Double> {

	private int size;
	private double[][] matrix;
    
        /**
         * Creation of the tri-diagonal matrix of Markov
         * @param nbPlaces
         * @param arrivalFreq
         * @param averageParkingTime
         * @throws BadAttributeValueExpException
         */
	public MarkovStatesLaw(int nbPlaces, double arrivalFreq, double averageParkingTime) throws BadAttributeValueExpException {
	        this.size = nbPlaces + 1;
		matrix = new double[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				//diagonal
				if(i==j) matrix[i][i]= -(i * averageParkingTime + arrivalFreq);
				//upper diagonal
				else if(j==i+1) matrix[i][j]=arrivalFreq;
				//lower diagonal
				else if(j==i-1) matrix[i][j]=i*averageParkingTime;
			}
		}
		matrix[nbPlaces][nbPlaces]= -(nbPlaces * averageParkingTime);
	}

	@Override
	protected Double evaluate(Integer... x) throws Exception {
		if (x.length != 2){
		    // too much arguments or not enough
		    throw new BadAttributeValueExpException("you need to give two ints");
		}
		int i = x[0];
		int j = x[1];
		if (i < 0 || i > size - 1 || j < 0 || j > size - 1) {
		    // the arguments are out of bounds of the matrix
		    throw new BadAttributeValueExpException("ints must be between 0 and " + (size - 1));
		}
		return matrix[i][j];
	}

}
