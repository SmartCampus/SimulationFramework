package org.smartcampus.simulation.framework.messages;

public class StartParkingSimulation {
	private final int begin;
	private final int duration;
	private final int interval;
	private final float value;
	
	public StartParkingSimulation(int b, int d, int i, float v) {
		this.begin = b;
		this.duration = d;
		this.interval = i;
		this.value = v;
	}

	public int getBegin() {
		return begin;
	}

	public int getDuration() {
		return duration;
	}

	public int getInterval() {
		return interval;
	}

	public float getValue() {
		return value;
	}
}
