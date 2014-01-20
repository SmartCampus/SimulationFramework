package org.smartcampus.simulation.framework.messages;

public class UpdateSensorSimulation<T> {
    private final int begin;
    private final T   value;

    public UpdateSensorSimulation(final int b, final T v) {
        this.begin = b;
        this.value = v;
    }

    public int getBegin() {
        return this.begin;
    }

    public T getValue() {
        return this.value;
    }
}
