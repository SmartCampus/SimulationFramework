package org.smartcampus.simulation.framework.messages;


public class UpdateSensorSimulation {
	private final int begin;
	private final Object value;

	public UpdateSensorSimulation(int b, Object v) {
		this.begin = b;
		this.value = v;
	}

	public int getBegin() {
		return begin;
	}

	public Object getValue() {
		return value;
	}
}
