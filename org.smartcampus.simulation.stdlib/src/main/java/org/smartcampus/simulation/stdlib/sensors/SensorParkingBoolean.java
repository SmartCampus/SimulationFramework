package org.smartcampus.simulation.stdlib.sensors;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;

import org.smartcampus.simulation.framework.simulator.Sensor;
import org.smartcampus.simulation.framework.simulator.Simulator;
import org.smartcampus.simulation.stdlib.laws.PolynomialLaw;
import org.smartcampus.simulation.stdlib.simulationlaw.ParkingSimulationLaw;


/**
 * Created by foerster on 14/01/14.
 */
public class SensorParkingBoolean extends Sensor<Double, Double, Boolean> {

	public Double computeValue() {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time*3600000);
		double t = c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60;
		if(t<6.5 || t> 18.5) return 0.;	
		return t;
	}

	@Override
	protected Boolean transformResponse(Double res) {
		if(res<0) return false;
		Random r = new Random();
		return r.nextInt(100) < res;
	}
	
	public static void main(String[] args) {
		Simulator s = new Simulator();
		s.addParkingLot("Parking1", ParkingSimulationLaw.class).addSensors("Parking1", SensorParkingBoolean.class, 5).initParkingLot("Parking1", new PolynomialLaw(24839.21865, -14430.25924, 3359.404392, -401.9522656, 26.18040012, -0.8830270156, 0.01208028907));
		s.simulate();
	}

}


