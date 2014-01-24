package org.smartcampus.simulation.framework.fluentapi;

import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;
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
    public void simulateReal() {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                this.frequency, this.frequency.toMillis()), ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }

    @Override
    public void simulateVirtual() {
        this.controllerRef.tell(new InitTypeSimulation(this.start, this.duration,
                Duration.create(100, TimeUnit.MILLISECONDS), this.frequency.toMillis()),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }
}
