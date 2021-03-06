package org.smartcampus.simulation.framework.fluentapi.simulation;

import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.fluentapi.SimulationWrapper;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulationLawWrapper0Impl extends SimulationWrapper implements SimulationLawWrapper0 {

    public SimulationLawWrapper0Impl(String simulationLawName, ActorRef controllerRef) {
        super(simulationLawName, controllerRef);
    }

    @Override
    public SimulationLawWrapper1 withSensors(int nbsensors, SensorTransformation<?, ?> transformation) {
        controllerRef.tell(new AddSensor(simulationLawName, nbsensors, transformation),
                ActorRef.noSender());
        return new SimulationLawWrapper1Impl(simulationLawName, controllerRef);
    }
}
