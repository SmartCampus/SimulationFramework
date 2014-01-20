package org.smartcampus.simulation.framework.messages;

import org.smartcampus.simulation.framework.simulator.SensorTransformation;

public class AddSensor {
    private final String                     name;
    private final int                        nbSensors;
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
