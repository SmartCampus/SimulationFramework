package smart.campus.simulation.messages;

public class StartParkingSimulation {
	private final int begin;
	private final int duration;
	private final int interval;
	private final int value;
	
	public StartParkingSimulation(int b, int d, int i, int v) {
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

	public int getValue() {
		return value;
	}
}
