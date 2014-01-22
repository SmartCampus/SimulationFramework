package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.SendValue;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public abstract class DataMaker extends UntypedActor {
    protected LoggingAdapter log;
    protected String output;

    public DataMaker() {
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    public DataMaker(final String out) {
        super();
        this.output = out;
    }

    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            // this.output = "http://localhost:8000/value";
        }
    }
}
