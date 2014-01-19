package org.smartcampus.simulation.stdlib.laws;

import javax.management.BadAttributeValueExpException;

import org.smartcampus.simulation.framework.simulator.Law;

public class MarkovStatesLaw extends Law<Integer, Double> {

	private int size;
	private double[][] matrix;

	public MarkovStatesLaw(int nbPlaces, double arrivalFreq,
			double averageParkingTime) throws BadAttributeValueExpException {
		this.size = nbPlaces + 1;
		matrix = new double[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				//diagonal
				if(i==j) matrix[i][i]=-(i * averageParkingTime + arrivalFreq);
				//upper diagonal
				else if(j==i+1) matrix[i][j]=arrivalFreq;
				//lower diagonal
				else if(j==i-1) matrix[i][j]=i*averageParkingTime;
			}
		}
	}

	@Override
	protected Double evaluate(Integer... x) throws Exception {
		if (x.length != 2)
			throw new BadAttributeValueExpException("you need to give two ints");
		int i = x[0];
		int j = x[1];
		return matrix[i][j];
	}

}
