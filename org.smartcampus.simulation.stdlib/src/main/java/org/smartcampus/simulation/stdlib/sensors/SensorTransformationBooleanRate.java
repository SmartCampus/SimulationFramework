package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;

import javax.management.BadAttributeValueExpException;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingMarkovSimulationLaw;

public class SensorTransformationBooleanRate implements
		SensorTransformation<Double, Boolean> {

	public static void main(final String[] args) {
		Simulator s = new Simulator();
		Law<Integer, Double> markov = null;
		try {
			markov = new MarkovStatesLaw(30, 0.1, 0.01);
		} catch (BadAttributeValueExpException e) {
			e.printStackTrace();
		}
		s.create("Parking2", ParkingMarkovSimulationLaw.class)
				.addSensors("Parking2", new SensorTransformationBooleanRate(),
						30).initSimulation("Parking2", markov);
		s.simulate(10, 10, 1);
	}

	@Override
	public Boolean transform(Double p, final Boolean last) {
		if ((p == 0) || (last == null)) {
			return false;
		}
		p = Math.abs(p);
		Random r = new Random();
		if (r.nextFloat() < p) {
			return !last;
		}
		return last;
	}
}
