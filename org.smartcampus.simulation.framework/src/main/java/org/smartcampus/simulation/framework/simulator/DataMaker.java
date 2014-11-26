package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This class allow to write or send data
 */
public abstract class DataMaker extends UntypedActor {
    protected LoggingAdapter log;
    protected String output;

    public DataMaker(final String out) {
        this.log = Logging.getLogger(this.getContext().system(), this);
        this.output = out;
    }

    /**
     * @inheritDoc
     */
    @Override
    public abstract void onReceive(final Object o) throws Exception;
}
