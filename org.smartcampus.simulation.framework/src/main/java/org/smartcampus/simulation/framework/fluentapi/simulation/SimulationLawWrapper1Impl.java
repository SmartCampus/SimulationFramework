package org.smartcampus.simulation.framework.fluentapi.simulation;

import org.smartcampus.simulation.framework.fluentapi.SimulationWrapper;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.simulator.Law;
import akka.actor.ActorRef;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulationLawWrapper1Impl extends SimulationWrapper implements
        SimulationLawWrapper1 {

    public SimulationLawWrapper1Impl(final String simulationLawName,
            final ActorRef controllerRef) {
        super(simulationLawName, controllerRef);
    }

    @Override
    public Simulator withLaw(final Law<?, ?> initVal) {
        this.controllerRef.tell(new InitSimulationLaw(this.simulationLawName, initVal),
                ActorRef.noSender());
        return new SimulatorImpl(this.controllerRef);
    }
}
