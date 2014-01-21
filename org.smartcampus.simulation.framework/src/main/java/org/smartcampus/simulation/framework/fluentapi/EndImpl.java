package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.messages.StartSimulation;

/**
 * Created by foerster on 21/01/14.
 */
public class EndImpl extends SimulationWrapper implements End {

    public EndImpl(ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public void simulate(final int begin, final int duration, final int interval) {
        controllerRef.tell(new StartSimulation(begin, duration, interval),
                ActorRef.noSender());
    }
}
