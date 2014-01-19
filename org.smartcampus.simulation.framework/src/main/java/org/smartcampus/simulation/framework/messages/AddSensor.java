package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class AddSensor {
	private final String name;
	private final int nbSensors;
	private final SensorTransformation<?, ?> transformation;

	public AddSensor(String n, int nbS, SensorTransformation<?, ?> t) {
		this.name = n;
		this.transformation = t;
		this.nbSensors = nbS;
	}

	public String getName() {
		return name;
	}

	public int getNbSensors() {
		return nbSensors;
	}
	
	public SensorTransformation<?, ?> getSensorTransformation() {
		return transformation;
	}
}
