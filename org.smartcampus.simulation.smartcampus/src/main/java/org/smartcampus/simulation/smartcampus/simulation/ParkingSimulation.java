package org.smartcampus.simulation.smartcampus.simulation;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * 
 * Created by foerster on 14/01/14.
 * 
 */
public class ParkingSimulation extends
		SimulationLaw<Double, Double, Boolean> {

	@Override
	protected Double[] computeValue() {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(this.getTime());
		Double[] t = { (double) (c.get(Calendar.HOUR_OF_DAY) + (c
				.get(Calendar.MINUTE) / 60)) };

		if ((t[0] < 6.5) || (t[0] > 18.5)) {
			return null; // the polynomial will return 0
		}
		return t;
	}

	@Override
	protected void onComplete() {
	}
}
