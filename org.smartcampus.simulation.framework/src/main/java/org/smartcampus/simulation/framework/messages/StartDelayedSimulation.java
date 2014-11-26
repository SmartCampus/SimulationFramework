package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

public class StartDelayedSimulation implements Serializable {
    private static final long serialVersionUID = -5753992270424623782L;

    private long timestamp;

    public StartDelayedSimulation(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
