package org.smartcampus.simulation.framework.messages;


public class UpdateSensorSimulation<T> {
	private final int begin;
	private final T value;

	public UpdateSensorSimulation(int b, T v) {
		this.begin = b;
		this.value = v;
	}

	public int getBegin() {
		return begin;
	}

	public T getValue() {
		return value;
	}
}
