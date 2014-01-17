package org.smartcampus.simulation.stdlib.simulationlaw;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingSimulationLaw extends SimulationLaw<Double, Double> {
	
	@Override
	protected Double computeValue() {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time*3600000);
		double t = c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60;
		if(t<6.5 || t> 18.5) return 0.;	
		return t;
	}
}
