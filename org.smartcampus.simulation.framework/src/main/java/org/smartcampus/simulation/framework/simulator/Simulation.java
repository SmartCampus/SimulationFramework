/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.InitOutput;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public abstract class Simulation<T> extends UntypedActor {

    /** The DataMaker that allow the Simulation to send direct value */
    protected ActorRef dataMaker;

    /** The scheduler which send an update every realTimeFrequency */
    protected Cancellable tick;

    /** the name of the output (url or file path) */
    protected String output;

    /** Allow to print logs */
    protected LoggingAdapter log;

    /** Context when the simulation start */
    protected Procedure<Object> simulationStarted;

    public Simulation() {
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void postStop() {
        this.tick.cancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof InitOutput) {
            this.output = ((InitOutput) o).getOutput();
        }

    }
}
