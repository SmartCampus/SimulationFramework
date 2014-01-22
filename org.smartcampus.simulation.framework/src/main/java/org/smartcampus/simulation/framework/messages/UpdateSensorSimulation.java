package org.smartcampus.simulation.framework.messages;

/**
 * The UpdateSensorSimulation message allows to update a sensor
 */
public class UpdateSensorSimulation<T> {
    private final long begin;
    private final T    value;

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
