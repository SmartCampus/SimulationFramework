package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.CreateSimulation;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.simulator.Replay;
import org.smartcampus.simulation.framework.simulator.Simulation;
import akka.actor.ActorRef;

public class SimulatorImpl extends SimulatorWrapper implements Simulator, Start {

    public SimulatorImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationStart setOutput(final String output) {
        this.controllerRef.tell(new InitOutput(output), ActorRef.noSender());
        return new SimulationStartImpl(this.controllerRef);
    }

    @Override
    public SimulationLawWrapper0 create(final String name,
            final Class<? extends Simulation<?>> simulationClass) {
        this.controllerRef.tell(new CreateSimulation(name, simulationClass),
                ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name, this.controllerRef);
    }

    @Override
    public ReplayWrapper0 replay(final String replayName,
            final Class<? extends Replay> replayClass) {
        this.controllerRef.tell(new CreateSimulation(replayName, replayClass),
                ActorRef.noSender());
        return new ReplayWrapper0Impl(replayName, this.controllerRef);
    }
}
