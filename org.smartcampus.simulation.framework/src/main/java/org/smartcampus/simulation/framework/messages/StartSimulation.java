package org.smartcampus.simulation.framework.messages;

public class StartSimulation {
	private final int begin;
	private final int duration;
	private final int interval;
	
	public StartSimulation(int b, int d, int i) {
		this.begin = b;
		this.duration = d;
		this.interval = i;
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

}
