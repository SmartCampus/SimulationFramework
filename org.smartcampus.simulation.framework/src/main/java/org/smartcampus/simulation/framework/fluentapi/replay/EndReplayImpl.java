package org.smartcampus.simulation.framework.fluentapi.replay;

import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import org.smartcampus.simulation.framework.messages.InitReplaySimulation;
import org.smartcampus.simulation.framework.messages.StartDelayedSimulation;
import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.messages.StartSimulationNow;

/**
 * Created by foerster on 21/01/14.
 */
public class EndReplayImpl extends SimulatorWrapper implements EndReplay {

    private long start;

    public EndReplayImpl(final ActorRef controllerRef, final long start) {
        super(controllerRef);
        this.start = start;
    }

    @Override
    public void simulateReal() {
        this.controllerRef.tell(new InitReplaySimulation(this.start, true),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulationNow(), ActorRef.noSender());
    }

    @Override
    public void simulateVirtual() {
        this.controllerRef.tell(new InitReplaySimulation(this.start, false),
                ActorRef.noSender());
        this.controllerRef.tell(new StartSimulationNow(), ActorRef.noSender());
    }
}
