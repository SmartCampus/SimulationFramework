package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public abstract class DataMaker extends UntypedActor {
    protected LoggingAdapter log;

    public DataMaker() {
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    @Override
    public abstract void onReceive(final Object o) throws Exception;
}
