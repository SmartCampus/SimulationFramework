package org.smartcampus.simulation.framework.fluentapi;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;
import org.smartcampus.simulation.framework.simulator.Law;
import org.smartcampus.simulation.framework.simulator.SensorTransformation;
import org.smartcampus.simulation.framework.simulator.SimulationController;
import org.smartcampus.simulation.framework.simulator.SimulationLaw;

public class StartImpl implements Start {

    private ActorRef controller;

    public StartImpl() {
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        this.controller = system.actorOf(Props.create(SimulationController.class),
                "SimulationControlor");
    }

    public StartImpl addSensors(final String name,
            final SensorTransformation<?, ?> transformation, final int nbsensors) {
        this.controller.tell(new AddSensor(name, nbsensors, transformation),
                ActorRef.noSender());
        return this;
    }

    public SimulationLawWrapper0 create(final String name,
                                        final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass) {
        this.controller.tell(new CreateSimulationLaw(name, simulationLawClass),ActorRef.noSender());
        return new SimulationLawWrapper0Impl(name,controller);
    }

    public StartImpl initSimulation(final String name, final Law<?, ?> initVal) {
        this.controller.tell(new InitSimulationLaw(name, initVal), ActorRef.noSender());
        return this;
    }

    public StartImpl simulate(final int begin, final int duration, final int interval) {
        this.controller.tell(new StartSimulation(begin, duration, interval),
                ActorRef.noSender());
        return this;
    }

}
