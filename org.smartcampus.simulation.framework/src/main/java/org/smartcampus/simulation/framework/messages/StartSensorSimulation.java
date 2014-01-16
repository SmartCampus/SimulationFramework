package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.Law;

public class StartSensorSimulation {
	private final int begin;
	private final int duration;
	private final int interval;
	private final Law<?, ?> law;

	public StartSensorSimulation(int b, int d, int i, Law<?, ?> l) {
		this.begin = b;
		this.duration = d;
		this.interval = i;
		this.law = l;
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

	public Law<?, ?> getLaw() {
		return law;
	}
}
