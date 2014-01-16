package org.smartcampus.simulation.framework.simulator;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.*;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitParking;
import org.smartcampus.simulation.framework.messages.StartSensorSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class SimulationLaw extends UntypedActor {

	private Router router;
	private Law<?, ?> value;

	private void createSensor(int numberOfSensors, Class<? extends Sensor<?,?,?>> sensorClass){
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfSensors; i++) {
			ActorRef r = getContext().actorOf(Props.create(sensorClass),
					getSelf().path().name() + "-" + i);
			getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		this.router = new Router(new BroadcastRoutingLogic(), routees);
	}
	
	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof InitParking) {
			value = ((InitParking) o).getInitVal();
		}
		else if (o instanceof StartSimulation) {
			StartSimulation message = (StartSimulation) o;
			router.route(
					new StartSensorSimulation(message.getBegin(), message
							.getDuration(), message.getInterval(), value),
					getSelf());
		}
		else if (o instanceof AddSensor) {
			AddSensor message = (AddSensor) o;
			this.createSensor(message.getNbSensors(), message.getSensorClass());
		}
	}
}
