package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.messages.*;

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
        getContext().actorOf(Props.create(DataSender.class),"dataSender");
	}

	private void createSensor(int numberOfSensors, SensorTransformation<S, R> t) {

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

			tick = getContext()
					.system()
					.scheduler()
					.schedule(Duration.Zero(),
							Duration.create(interval, TimeUnit.SECONDS),
							getSelf(), new UpdateSimulation(),
							getContext().dispatcher(), null);
		} else if (o instanceof AddSensor) {
			AddSensor message = (AddSensor) o;
			if (message.getSensorTransformation() instanceof SensorTransformation<?, ?>) {
				SensorTransformation<S, R> t = (SensorTransformation<S, R>) message
						.getSensorTransformation();
				this.createSensor(message.getNbSensors(), t);
			} else {
				// TODO error
			}
		} else if (o instanceof InitSimulationLaw) {
			InitSimulationLaw message = (InitSimulationLaw) o;
			if (message.getLaw() instanceof Law<?, ?>) {
				law = (Law<S, T>) message.getLaw();
			} else {
				// TODO error
			}
		} else if (o instanceof UpdateSimulation) {
			router.route(new UpdateSensorSimulation<T>(time, valueToSend),
					getSelf());
			time++;
		} else if (o instanceof ReturnMessage<?>) {
			ReturnMessage<R> message = (ReturnMessage<R>) o;
			this.values.add(message.getResult());

			if (this.values.size() == this.router.routees().size()) {
				valueToSend = this.law.evaluate(this.computeValue());
				this.values.clear();
                getSelf().tell(new Complete(),getSelf());
			}
		} else if ( o instanceof Complete){
            onComplete();
        }

	}

	protected abstract S[] computeValue();

    /**
     * Describe in this method all you want to do when you receive all the result from the sensors
     * E.g : send a new value ( with sendNewValue() ) or calculate the average or whatever
     */
    protected abstract void onComplete();

    /**
     * Send a new value to all the sensor that was calculate when all the result were receive
     */
    private void sendNewValue(){
        getSelf().tell(new UpdateSimulation(),getSelf());
    }

    /**
     * Simulate a virtual sensor that send data
     * @param name the name of the sensor
     * @param value the value of the sensor
     * @param time the time corresponding to the value
     */
    public final void sendValue(String name,String value,String time){
        getContext().getChild("dataSender").tell(new SendValue(name,value,time),getSelf());
    }

	@Override
	public final void postStop() {
		tick.cancel();
	}
}
