package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.InitSensorSimulation;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;

/**
 * Created by foerster on 14/01/14.
 */
public final class Sensor<T, R> extends UntypedActor {

    private R                          lastReturnedValue;
    private SensorTransformation<T, R> transformation;
    private ActorRef                   dataMaker;

    public Sensor(final SensorTransformation<T, R> t) {
        this.transformation = t;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof UpdateSensorSimulation) {
            UpdateSensorSimulation<T> message = (UpdateSensorSimulation<T>) o;
            long time = message.getBegin();
            T value = message.getValue();
            R res = this.transformation.transform(value, this.lastReturnedValue);

            // saves the value in case it is needed for next calculation
            this.lastReturnedValue = res;

            this.getSender().tell(new ReturnMessage<R>(res), this.getSelf());
            this.dataMaker.tell(
                    new SendValue(this.getSelf().path().name(), res.toString(), time),
                    this.getSelf());
        }
        else if (o instanceof InitSensorSimulation) {
            InitSensorSimulation message = (InitSensorSimulation) o;
            ActorRef tmp = message.getDataMaker();
            if (tmp == null) {
                this.dataMaker = this.getContext().actorOf(
                        new RoundRobinPool(5).withResizer(new DefaultResizer(1, 5))
                                .props(Props.create(DataSender.class)),
                        "Sensor" + this.getSelf().path().name());
                this.lastReturnedValue = null;
            }
            else {
                this.dataMaker = tmp;
            }
        }

    }
}
