package org.smartcampus.simulation.stdlib.sensors;

import java.util.Random;

import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.Sensor;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;

/**
 * Created by foerster on 14/01/14.
 */
public class SensorParkingBoolean extends Sensor<Double, Boolean> {

	@Override
	protected Boolean transformResponse(Double res) {
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
		s.addParkingLot("Parking1", ParkingSimulationLaw.class)
				.addSensors("Parking1", SensorParkingBoolean.class, 5)
				.initParkingLot("Parking1", polynome);
		s.simulate();
	}

}
