package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;

public class SensorTransformationBooleanPercent implements
		SensorTransformation<Double, Boolean> {

	@Override
	public Boolean transform(Double res, Boolean last) {
		if (res == 0)
			return false;
		res = Math.abs(res);
		Random r = new Random();
		return r.nextInt(100) < res;
	}

	public static void main(String[] args) {
		Simulator s = new Simulator();
		Law<Double, Double> polynome = new PolynomialLaw(24839.21865,
				-14430.25924, 3359.404392, -401.9522656, 26.18040012,
				-0.8830270156, 0.01208028907);
		s.create("Parking1", ParkingSimulationLaw.class)
				.addSensors("Parking1", new SensorTransformationBooleanPercent(), 5)
				.initSimulation("Parking1", polynome);
		s.simulate(10, 10, 1);
	}

}
