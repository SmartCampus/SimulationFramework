package org.smartcampus.simulation.framework.messages;

public class InitParking {
	private final String name;
	private final float initVal;
	
	public InitParking(String n, float initVal2) {
		this.name = n;
		this.initVal = initVal2;
	}

	public String getName() {
		return name;
	}

	public float getInitVal() {
		return initVal;
	}

}
