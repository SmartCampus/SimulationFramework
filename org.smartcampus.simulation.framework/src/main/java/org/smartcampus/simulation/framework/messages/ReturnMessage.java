package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

/**
 * The ReturnMessage message gives the answer 'result' of the sensor to the simulationLaw
 */
public class ReturnMessage<R> implements Serializable {
    private static final long serialVersionUID = -5177913038174918875L;
    private final R           result;

    public ReturnMessage(final R v) {
        this.result = v;
    }

    public R getResult() {
        return this.result;
    }
}
