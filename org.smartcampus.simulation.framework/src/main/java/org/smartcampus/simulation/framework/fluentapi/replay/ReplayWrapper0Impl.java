package org.smartcampus.simulation.framework.fluentapi.replay;

import org.smartcampus.simulation.framework.fluentapi.SimulationWrapper;
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
    public ReplayWrapper0 withSensor(final String key, final String value) {
        this.controllerRef.tell(new InitReplayParam(key, value), ActorRef.noSender());
        return new ReplayWrapper0Impl(key, this.controllerRef);
    }

    @Override
    public SimulatorReplay setInput(final String input) {
        this.controllerRef.tell(new InitInput(input), ActorRef.noSender());
        return new SimulatorReplayImpl(this.controllerRef);
    }
}
