package org.smartcampus.simulation.framework.simulator;

public interface SensorTransformation<T, R> {
	public R transform(T val);
}
