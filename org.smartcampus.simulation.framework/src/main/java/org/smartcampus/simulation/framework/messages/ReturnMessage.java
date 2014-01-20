package org.smartcampus.simulation.framework.messages;

/**
 * The ReturnMessage message gives the answer 'result' of the sensor to the simulationLaw
 */
public class ReturnMessage<R> {
    private final R result;

    public ReturnMessage(final R v) {
        this.result = v;
    }

    public R getResult() {
        return this.result;
    }
}
