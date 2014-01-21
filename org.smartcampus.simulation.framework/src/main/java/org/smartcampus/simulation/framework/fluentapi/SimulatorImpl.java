package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulatorImpl extends SimulationWrapper implements Simulator,Start {

    public SimulatorImpl(ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public End setUrl(String url) {
        return new EndImpl(controllerRef);
    }

    @Override
    public SimulationLawWrapper0 create(final String name,
                                        final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass) {
        controllerRef.tell(new CreateSimulationLaw(name, simulationLawClass), ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name,controllerRef);
    }
}
