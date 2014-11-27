package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

import java.io.Serializable;

/**
 * The AddSensor message allows to create 'nbSensors' sensors on event with 'transformation'
 */
public class AddSensorOnEvent implements Serializable {

    private static final long                serialVersionUID = -5298126988129574376L;
    /** The name of the SimulationLaw */
    private final String                     name;
    /** The number of sensors to add to the SimulationLaw */
    private final int                        nbSensors;
    /** The transformation used in the sensors */
    private final SensorTransformation<?, ?> transformation;

    public AddSensorOnEvent(final String n, final int nbS, final SensorTransformation<?, ?> t) {
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
