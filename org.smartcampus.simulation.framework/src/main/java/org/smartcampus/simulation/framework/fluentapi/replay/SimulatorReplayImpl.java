package org.smartcampus.simulation.framework.fluentapi.replay;

import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import org.smartcampus.simulation.framework.messages.CreateSimulation;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.messages.InitReplay;
import org.smartcampus.simulation.framework.simulator.FileFormator;
import org.smartcampus.simulation.framework.simulator.Replay;
import akka.actor.ActorRef;

public class SimulatorReplayImpl extends SimulatorWrapper implements SimulatorReplay,
        StartReplay {

    public SimulatorReplayImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationStartReplay setOutput(final String output) {
        this.controllerRef.tell(new InitOutput(output), ActorRef.noSender());
        return new SimulationStartReplayImpl(this.controllerRef);
    }

    @Override
    public ReplayWrapper0 createReplay(final String replayName,
            final Class<? extends FileFormator> replayClass) {
        this.controllerRef.tell(new CreateSimulation(replayName, Replay.class),
                ActorRef.noSender());
        this.controllerRef.tell(new InitReplay(replayClass), ActorRef.noSender());
        return new ReplayWrapper0Impl(replayName, this.controllerRef);
    }
}
