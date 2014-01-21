package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class EndImpl extends SimulationWrapper implements End {

    public EndImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public void simulateReal(final int begin, final int duration, final int frequence) {
        this.controllerRef.tell(new InitTypeSimulation(begin, duration, frequence,
                frequence), ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }

    @Override
    public void simulateVirtual(final int begin, final int duration, final int frequence) {
        this.controllerRef.tell(new InitTypeSimulation(begin, duration, frequence, 1),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulation(), ActorRef.noSender());
    }
}
