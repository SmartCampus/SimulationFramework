package org.smartcampus.simulation.framework.simulator;

import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Created by foerster on 14/01/14.
 */
public final class Sensor<T, R> extends UntypedActor {

    private int                        time;
    private T                          value;
    private R                          lastReturnedValue;
    private SensorTransformation<T, R> transformation;
    private ActorRef                   dataSender;

    public Sensor(final SensorTransformation<T, R> t) {
        this.transformation = t;
        this.dataSender = getContext().actorOf(new RoundRobinPool(5).withResizer(new DefaultResizer(1,10) {
        }).props(
                Props.create(DataSender.class)), "sensorDataSender");
        this.lastReturnedValue = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof UpdateSensorSimulation) {
            UpdateSensorSimulation<T> message = (UpdateSensorSimulation<T>) o;
            this.time = message.getBegin();
            this.value = message.getValue();
            R res = this.transformation.transform(this.value, this.lastReturnedValue);

            // saves the value in case it is needed for next calculation
            this.lastReturnedValue = res;

            this.getSender().tell(new ReturnMessage<R>(res), this.getSelf());
            this.dataSender
                    .tell(new SendValue(this.getSelf().path().name(), res.toString(),
                            this.time), this.getSelf());
        }
    }
}
