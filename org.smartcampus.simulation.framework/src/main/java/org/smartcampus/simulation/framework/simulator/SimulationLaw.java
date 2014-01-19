package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSimulation;

import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class SimulationLaw<S, T, R> extends UntypedActor {

	private Router router;
	protected Law<S, T> law;
	private T valueToSend;
	protected int time;
    private int interval;
    private Cancellable tick;
    protected List<R> values;
    
    protected LoggingAdapter log;    
    
    public SimulationLaw() {
    	this.values = new LinkedList<R>();
    	this.log = Logging.getLogger(getContext().system(), this);	
	}
    
	private void createSensor(int numberOfSensors, SensorTransformation<S, R> t){
				
		List<Routee> routees = new ArrayList<Routee>();
		for (int i = 0; i < numberOfSensors; i++) {
			ActorRef r = getContext().actorOf(Props.create(Sensor.class, t),
					getSelf().path().name() + "-" + i);
			getContext().watch(r);
			routees.add(new ActorRefRoutee(r));
		}
		this.router = new Router(new BroadcastRoutingLogic(), routees);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final void onReceive(Object o) throws Exception {
		if (o instanceof StartSimulation) {
			StartSimulation message = (StartSimulation) o;
			this.time = message.getBegin();
			this.interval = message.getInterval();
			
			valueToSend = this.law.evaluate(this.computeValue());
			
			tick  = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(interval, TimeUnit.SECONDS),
                    getSelf(), new UpdateSimulation(), getContext().dispatcher(), null);
		}
		else if (o instanceof AddSensor) {
			AddSensor message = (AddSensor) o;
			if(message.getSensorTransformation() instanceof SensorTransformation<?, ?>){
				SensorTransformation<S, R> t = (SensorTransformation<S, R>)message.getSensorTransformation();
				this.createSensor(message.getNbSensors(), t);
			}else{
				// TODO error
			}
		}
		else if (o instanceof InitSimulationLaw) {
			InitSimulationLaw message = (InitSimulationLaw) o;
			if(message.getLaw() instanceof Law<?, ?>){
				law = (Law<S, T>) message.getLaw();
			}else{
				// TODO error
			}
		}
		else if (o instanceof UpdateSimulation){			
			router.route(
					new UpdateSensorSimulation<T>(time, valueToSend),
					getSelf());
			time++;
		}
		else if(o instanceof ReturnMessage<?>){
			ReturnMessage<R> message = (ReturnMessage<R>) o;
			this.values.add(message.getResult());
			
			if(this.values.size() == this.router.routees().size()){
				valueToSend = this.law.evaluate(this.computeValue());
				this.values.clear();
			}
		}
		
	}

	protected abstract S[] computeValue();

    @Override
    public final void postStop(){
        tick.cancel();
    }
}
