package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class Simulator {
    private ActorRef controller;

    public Simulator() {
        ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
        this.controller = system.actorOf(Props.create(SimulationController.class),
                "SimulationControlor");
    }

    public Simulator addSensors(final String name,
            final SensorTransformation<?, ?> transformation, final int nbsensors) {
        this.controller.tell(new AddSensor(name, nbsensors, transformation),
                ActorRef.noSender());
        return this;
    }

    public Simulator create(final String name,
            final Class<? extends SimulationLaw<?, ?, ?>> simulationLawClass) {
        this.controller.tell(new CreateSimulationLaw(name, simulationLawClass),
                ActorRef.noSender());
        return this;
    }

    public Simulator initSimulation(final String name, final Law<?, ?> initVal) {
        this.controller.tell(new InitSimulationLaw(name, initVal), ActorRef.noSender());
        return this;
    }

    public Simulator simulate(final int begin, final int duration, final int interval) {
        this.controller.tell(new StartSimulation(begin, duration, interval),
                ActorRef.noSender());
        return this;
    }
}
