package smart.campus.simulation;

public class CreateParking {
	private final String name;
	private final int nbSensors;
	
	public CreateParking(String n, int nbS) {
		this.name = n;
		this.nbSensors = nbS;
	}

	public String getName() {
		return name;
	}

	public int getNbSensors() {
		return nbSensors;
	}
}
