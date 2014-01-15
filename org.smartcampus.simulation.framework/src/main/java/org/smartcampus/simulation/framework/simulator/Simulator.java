package org.smartcampus.simulation.framework.simulator;

import com.typesafe.config.ConfigFactory;

import org.smartcampus.simulation.framework.messages.CreateParking;
import org.smartcampus.simulation.framework.messages.InitParking;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Simulator {
	private ActorRef controller;
	
	public Simulator() {
		ActorSystem system = ActorSystem.create("Simulation", ConfigFactory.load());
		controller = system.actorOf(Props.create(SimulationController.class), "SimulationControlor");
	}
	
	public Simulator addParkingLot(String name, final int nbsensors){
		controller.tell(new CreateParking(name, nbsensors), ActorRef.noSender());
		return this;
	}
	
	public Simulator initParkingLot(String name, final float initVal){
		controller.tell(new InitParking(name, initVal), ActorRef.noSender());
		return this;
	}
	
	public Simulator simulate(){
		controller.tell(new StartSimulation(10, 3, 1), ActorRef.noSender());
		return this;
	}
	
	public static void main(String[] args) {
		new Simulator().addParkingLot("Parking1", 5).initParkingLot("Parking1", 0).simulate();
	}
	
}