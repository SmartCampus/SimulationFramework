package smart.campus.simulation.simulator;

import smart.campus.simulation.messages.CreateParking;
import smart.campus.simulation.messages.InitParking;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Simulator {
	private ActorRef controller;
	
	public Simulator() {
		ActorSystem system = ActorSystem.create("Simulation");
		controller = system.actorOf(Props.create(SimulationControlor.class), "SimulationControlor");
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
		//TODO
		return this;
	}
	
}
