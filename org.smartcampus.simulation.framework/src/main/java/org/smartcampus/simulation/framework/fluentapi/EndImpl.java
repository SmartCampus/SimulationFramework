package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class EndImpl extends SimulationWrapper implements End {

    private int start,duration,frequency;

    public EndImpl(final ActorRef controllerRef,int start,int duration,int frequency) {
        super(controllerRef);
        this.start = start;
        this.duration = duration;
        this.frequency = frequency;
    }

    @Override
    public void simulateReal() {
        this.controllerRef.tell(new InitTypeSimulation(start, duration, frequency,
                frequency), ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }

    @Override
    public void simulateVirtual() {
        this.controllerRef.tell(new InitTypeSimulation(start, duration, frequency, 1),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }
}
