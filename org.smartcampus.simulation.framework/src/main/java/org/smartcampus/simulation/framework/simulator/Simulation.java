/**
 * 
 */
package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

public abstract class Simulation<T> extends UntypedActor {
    /** The current time of the simulation */
    protected long time;
    /** The DataMaker that allow the Simulation to send direct value */
    protected ActorRef dataMaker;

    /** The scheduler which send an update every realTimeFrequency */
    protected Cancellable tick;

    /** The T value to send to the sensors */
    protected T valueToSend;

    /** The real time frequency correspond to the duration */
    protected FiniteDuration realTimeFrequency;

    /** Each real time frequency, the time is increased by the frequency */
    protected long frequency;

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
     * Return the current time of the simulation
     * 
     * @return the current time of the simulation
     */
    public long getTime() {
        return this.time;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void postStop() {
        this.tick.cancel();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof InitTypeSimulation) {
            InitTypeSimulation message = (InitTypeSimulation) o;
            this.initTypeSimulation(message);
        }
        else if (o instanceof InitOutput) {
            this.output = ((InitOutput) o).getOutput();
        }

    }

    /**
     * Handle the message InitTypeSimulation
     * 
     * @param message
     *            contains the initialization of the simulation
     */
    private void initTypeSimulation(final InitTypeSimulation message) {
        this.time = message.getBegin();
        this.realTimeFrequency = message.getRealTimeFrequency();
        this.frequency = message.getFrequency();

    }
}
