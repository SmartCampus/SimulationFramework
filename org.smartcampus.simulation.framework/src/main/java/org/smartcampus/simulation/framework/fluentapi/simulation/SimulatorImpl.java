package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import org.smartcampus.simulation.framework.messages.CreateSimulation;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.simulator.Simulation;
import akka.actor.ActorRef;

public class SimulatorImpl extends SimulatorWrapper implements Simulator {

    public SimulatorImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationStart setOutput(final String output) {
        this.controllerRef.tell(new InitOutput(output), ActorRef.noSender());
        return new SimulationStartImpl(this.controllerRef);
    }

    @Override
    public SimulationLawWrapper0 createSimulation(final String name,
            final Class<? extends Simulation<?>> simulationClass) {
        this.controllerRef.tell(new CreateSimulation(name, simulationClass),
                ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name, this.controllerRef);
    }
}
