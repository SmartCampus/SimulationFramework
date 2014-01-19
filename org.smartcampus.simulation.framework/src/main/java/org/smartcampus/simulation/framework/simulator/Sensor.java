package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;

/**
 * Created by foerster on 14/01/14.
 */
public final class Sensor<S, R> extends UntypedActor {

	private int time;
	private S value;
	private LoggingAdapter log;
	private R lastReturnedValue;
	private SensorTransformation<S, R> transformation;

	public Sensor(SensorTransformation<S, R> t) {
		this.log = Logging.getLogger(getContext().system(), this);
		this.transformation = t;
		this.lastReturnedValue = null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Object o) throws Exception {
		if (o instanceof UpdateSensorSimulation) {
			UpdateSensorSimulation<S> message = (UpdateSensorSimulation<S>) o;
			this.time = message.getBegin();
			this.value = message.getValue();
			R res = this.transformation.transform(this.value,this.lastReturnedValue);
			// saves the value in case it is needed for next calculation
			lastReturnedValue = res;
			this.log.debug("[" + time + "," + (res) + "]");

			this.getSender().tell(new ReturnMessage<R>(res), this.getSelf());
		}
	}

}
