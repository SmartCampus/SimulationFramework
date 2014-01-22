package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * The UpdateSensorSimulation message allows to update a sensor
 */
public class UpdateSensorSimulation<T> implements Serializable {
    private static final long serialVersionUID = -8633831626520443719L;
    private final long        begin;
    private final T           value;

    public UpdateSensorSimulation(final long begin, final T value) {
        this.begin = begin;
        this.value = value;
    }

    public long getBegin() {
        return this.begin;
    }

    public T getValue() {
        return this.value;
    }
}
