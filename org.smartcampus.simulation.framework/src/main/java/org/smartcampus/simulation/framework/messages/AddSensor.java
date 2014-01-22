package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * The AddSensor message allows to create 'nbSensors' sensors with 'transformation'
 */
public class AddSensor implements Serializable {

    private static final long                serialVersionUID = -5298236988969574376L;
    /** The name of the SimulationLaw */
    private final String                     name;
    /** The number of sensors to add to the SimulationLaw */
    private final int                        nbSensors;
    /** The transformation used in the sensors */
    private final SensorTransformation<?, ?> transformation;

    public AddSensor(final String n, final int nbS, final SensorTransformation<?, ?> t) {
        this.name = n;
        this.transformation = t;
        this.nbSensors = nbS;
    }

    public String getName() {
        return this.name;
    }

    public int getNbSensors() {
        return this.nbSensors;
    }

    public SensorTransformation<?, ?> getSensorTransformation() {
        return this.transformation;
    }
}
