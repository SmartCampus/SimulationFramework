package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.InitInput;
import org.smartcampus.simulation.framework.messages.InitReplayParam;
import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class ReplayWrapper0Impl extends SimulationWrapper implements ReplayWrapper0 {

    public ReplayWrapper0Impl(final String simulationLawName, final ActorRef controllerRef) {
        super(simulationLawName, controllerRef);
    }

    @Override
    public ReplayWrapper0 setParam(final String key, final Object value) {
        this.controllerRef.tell(new InitReplayParam(key, value), ActorRef.noSender());
        return new ReplayWrapper0Impl(key, this.controllerRef);
    }

    @Override
    public Simulator setInput(final String input) {
        this.controllerRef.tell(new InitInput(input), ActorRef.noSender());
        return new SimulatorImpl(this.controllerRef);
    }
}
