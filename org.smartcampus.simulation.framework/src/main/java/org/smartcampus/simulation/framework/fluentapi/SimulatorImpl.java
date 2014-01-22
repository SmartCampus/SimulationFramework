package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;
import akka.actor.ActorRef;

public class SimulatorImpl extends SimulationWrapper implements Simulator, Start {

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
            final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass) {
        this.controllerRef.tell(new CreateSimulationLaw(name, simulationLawClass),
                ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name, this.controllerRef);
    }
}
