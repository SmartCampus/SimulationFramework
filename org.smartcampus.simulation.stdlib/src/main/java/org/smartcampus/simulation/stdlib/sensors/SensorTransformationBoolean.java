package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;

import javax.management.BadAttributeValueExpException;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.MarkovStatesLaw;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingMarkovSimulationLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;

public class SensorTransformationBoolean implements SensorTransformation<Double, Boolean>{
	
	@Override
	public Boolean transform(Double res) {
		if (res < 0)
			return false;
		Random r = new Random();
		return r.nextInt(100) < res;
	}

	public static void main(String[] args) {
		Simulator s = new Simulator();
		Law<Double,Double> polynome = new PolynomialLaw(24839.21865, -14430.25924,
				3359.404392, -401.9522656, 26.18040012, -0.8830270156,
				0.01208028907);
		Law<Integer, Double> markov=null;
		try {
			markov = new MarkovStatesLaw(15, 0.1, 0.01);
		} catch (BadAttributeValueExpException e) {
			e.printStackTrace();
		}
		s.create("Parking1", ParkingSimulationLaw.class)
				.addSensors("Parking1", new SensorTransformationBoolean(), 5)
				.initSimulation("Parking1", polynome);
		s.create("Parking2", ParkingMarkovSimulationLaw.class)
				.addSensors("Parking2", new SensorTransformationBoolean(), 15)
				.initSimulation("Parking2", markov);
		s.simulate(10, 10, 1);
		
	}


}
