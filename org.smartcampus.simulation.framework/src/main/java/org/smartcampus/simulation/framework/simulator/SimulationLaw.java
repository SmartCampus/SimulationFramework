package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitParking;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSimulation;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class SimulationLaw<S, T> extends UntypedActor {

	private Router router;
	protected Law<S, T> value;
	protected int time;
    private int interval;
    private Cancellable tick ;
    
	private void createSensor(int numberOfSensors, Class<? extends Sensor<T,?>> sensorClass){
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
			value = (Law<S, T>) ((InitParking) o).getInitVal();
		}
		else if (o instanceof StartSimulation) {
			StartSimulation message = (StartSimulation) o;
			this.time = message.getBegin();
			this.interval = message.getInterval();
			
			tick  = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(interval, TimeUnit.SECONDS),
                    getSelf(), new UpdateSimulation(), getContext().dispatcher(), null);
		}
		else if (o instanceof UpdateSimulation){
			T res = this.value.evaluate(this.computeValue());
			
			router.route(
					new UpdateSensorSimulation(time, res),
					getSelf());
			time++;
		}
		else if (o instanceof AddSensor) {
			AddSensor message = (AddSensor) o;
			this.createSensor(message.getNbSensors(), (Class<? extends Sensor<T,?>>)message.getSensorClass());
		}
	}

	protected abstract S computeValue();
	

    @Override
    public void postStop(){
        tick.cancel();
    }
}
