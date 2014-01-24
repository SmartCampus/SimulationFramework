package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.simulator.Law;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulationLawWrapper1Impl extends SimulationWrapper implements SimulationLawWrapper1 {

    public SimulationLawWrapper1Impl(String simulationLawName, ActorRef controllerRef) {
        super(simulationLawName, controllerRef);
    }

    @Override
    public Simulator withLaw(Law<?, ?> initVal) {
        controllerRef.tell(new InitSimulationLaw(simulationLawName, initVal), ActorRef.noSender());
        return new SimulatorImpl(controllerRef);
    }
}
