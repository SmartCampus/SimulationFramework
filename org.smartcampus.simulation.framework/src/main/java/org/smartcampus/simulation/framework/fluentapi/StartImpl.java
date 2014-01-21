package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.smartcampus.simulation.framework.simulator.SimulationController;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;

public class StartImpl implements Start {

    private ActorRef controller;

    public StartImpl() {
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        this.controller = system.actorOf(Props.create(SimulationController.class),
                "SimulationControlor");
    }

    public SimulationLawWrapper0 create(final String name,
                                        final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass) {
        this.controller.tell(new CreateSimulationLaw(name, simulationLawClass),ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name,controller);
    }

}
