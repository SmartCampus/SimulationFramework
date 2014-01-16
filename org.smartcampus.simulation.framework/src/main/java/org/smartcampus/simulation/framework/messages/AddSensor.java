package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.Sensor;

public class AddSensor {
	private final String name;
	private final int nbSensors;
	private final Class<? extends Sensor<?, ?, ?>> sensorClass;

	public AddSensor(String n, int nbS, Class<? extends Sensor<?, ?, ?>> s) {
		this.name = n;
		this.sensorClass = s;
		this.nbSensors = nbS;
	}

	public String getName() {
		return name;
	}

	public int getNbSensors() {
		return nbSensors;
	}
	
	public Class<? extends Sensor<?, ?, ?>> getSensorClass() {
		return sensorClass;
	}
}
