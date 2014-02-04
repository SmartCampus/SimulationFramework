package org.smartcampus.simulation.framework.messages;

import java.io.Serializable;

public class InitReplaySimulation implements Serializable {

    private static final long serialVersionUID = 7438121617889255898L;
    private final long start;
    private final boolean isReal;

    public InitReplaySimulation(final long start, final boolean b) {
        this.start = start;
        this.isReal = b;
    }

    public long getStart() {
        return this.start;
    }

    public boolean isReal() {
        return this.isReal;
    }
}
