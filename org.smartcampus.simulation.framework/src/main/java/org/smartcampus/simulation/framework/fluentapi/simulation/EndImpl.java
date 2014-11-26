package org.smartcampus.simulation.framework.fluentapi.simulation;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartDelayedSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulationNow;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class EndImpl extends SimulatorWrapper implements End {

    private long start;
    private FiniteDuration duration, frequency;

    public EndImpl(final ActorRef controllerRef, final long start,
            final FiniteDuration duration, final FiniteDuration frequency) {
        super(controllerRef);
        this.start = start;
        this.duration = duration;
        this.frequency = frequency;
    }

    @Override
    public void startRealTimeSimulationNow() {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                this.frequency, this.frequency.toMillis()), ActorRef.noSender());
        this.controllerRef.tell(new StartSimulationNow(), ActorRef.noSender());
    }

    @Override
    public void startRealTimeSimulationAt(long time) {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                this.frequency, this.frequency.toMillis()), ActorRef.noSender());
        this.controllerRef.tell(new StartDelayedSimulation(time), ActorRef.noSender());
    }

    @Override
    public void startRealTimeSimulationAt(String date) {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                this.frequency, this.frequency.toMillis()), ActorRef.noSender());
        this.controllerRef.tell(new StartDelayedSimulation(Timestamp.valueOf(date).getTime()), ActorRef.noSender());
    }

    @Override
    public void startVirtualSimulation() {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                Duration.create(100, TimeUnit.MILLISECONDS), this.frequency.toMillis()),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulationNow(), ActorRef.noSender());
    }

}
