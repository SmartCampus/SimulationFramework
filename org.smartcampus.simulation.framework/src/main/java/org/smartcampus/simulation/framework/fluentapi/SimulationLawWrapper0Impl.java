package org.smartcampus.simulation.framework.fluentapi;

import akka.actor.ActorRef;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;

/**
 * Created by foerster on 21/01/14.
 */
public class SimulationLawWrapper0Impl extends SimulationLawWrapper implements SimulationLawWrapper0 {

    public SimulationLawWrapper0Impl(String simulationLawName, ActorRef controllerRef) {
        super(simulationLawName, controllerRef);
    }

    @Override
    public SimulationLawWrapper1 add(int nbsensors, SensorTransformation<?, ?> transformation) {
        controllerRef.tell(new AddSensor(simulationLawName, nbsensors, transformation),
                ActorRef.noSender());
        return new SimulationLawWrapper1Impl(simulationLawName,controllerRef);
    }
}
