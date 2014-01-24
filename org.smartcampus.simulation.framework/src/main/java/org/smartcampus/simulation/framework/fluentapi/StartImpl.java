package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.CreateSimulation;
import org.smartcampus.simulation.framework.simulator.Replay;
import org.smartcampus.simulation.framework.simulator.Simulation;
import org.smartcampus.simulation.framework.simulator.SimulationController;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class StartImpl implements Start {

    private ActorRef controller;

    public StartImpl() {
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        this.controller = system.actorOf(Props.create(SimulationController.class),
                "SimulationControlor");
    }

    @Override
    public SimulationLawWrapper0 create(final String simulationName,
            final Class<? extends Simulation<?>> simulationClass) {
        this.controller.tell(new CreateSimulation(simulationName, simulationClass),
                ActorRef.noSender());
        return new SimulationLawWrapper0Impl(simulationName, this.controller);
    }

    @Override
    public ReplayWrapper0 replay(final String replayName,
            final Class<? extends Replay> replayClass) {
        this.controller.tell(new CreateSimulation(replayName, replayClass),
                ActorRef.noSender());
        return new ReplayWrapper0Impl(replayName, this.controller);
    }

}
