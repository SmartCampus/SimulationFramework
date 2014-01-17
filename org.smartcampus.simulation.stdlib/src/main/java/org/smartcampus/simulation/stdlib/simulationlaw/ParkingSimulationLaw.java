package org.smartcampus.simulation.stdlib.simulationlaw;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingSimulationLaw extends SimulationLaw<Double, Double> {
	
	@Override
	protected Double[] computeValue() {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time*3600000);
		Double[] t= {(double) (c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60)} ;
		if(t[0]<6.5 || t[0]> 18.5) {
			Double[] tmp = {0.};
			return tmp;	
		}
		return t;
	}
}
