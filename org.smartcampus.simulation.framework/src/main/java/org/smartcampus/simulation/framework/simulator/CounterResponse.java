package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.CountRequestsPlusOne;
import org.smartcampus.simulation.framework.messages.CountResponsesPlusOne;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class CounterResponse extends UntypedActor {

    private int            countResponse;
    private int            countRequests;
    private LoggingAdapter log;

    public CounterResponse() {
        this.log = Logging.getLogger(this.getContext().system(), this);
        this.countResponse = 0;
        this.countRequests = 0;
    }

    @Override
    public void onReceive(final Object arg0) throws Exception {
        if (arg0 instanceof CountResponsesPlusOne) {
            this.countResponse++;
        }
        else if (arg0 instanceof CountRequestsPlusOne) {
            this.countRequests++;
        }

    }

    @Override
    public void postStop() {
        this.log.debug("Number of requests : " + this.countRequests);
        this.log.debug("Number of responses : " + this.countResponse);

    }
}
