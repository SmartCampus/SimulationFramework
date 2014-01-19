package org.smartcampus.simulation.stdlib.simulationlaw;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingMarkovSimulationLaw extends SimulationLaw<Integer, Double> {
	
	@Override
	protected Integer[] computeValue() {
		int i = time%15;
		Integer[] t= {i,i};
		return t;
	}
}
