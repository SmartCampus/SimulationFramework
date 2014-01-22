package org.smartcampus.simulation.framework.simulator;

import akka.japi.Procedure;
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
       if (o instanceof InitSensorSimulation) {
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
           getContext().become(simulationStarted);
        }

    }

    private Procedure<Object> simulationStarted = new Procedure<Object>() {
        @Override
        public void apply(Object o) {
            if (o instanceof UpdateSensorSimulation) {
                UpdateSensorSimulation<T> message = (UpdateSensorSimulation<T>) o;
                long time = message.getBegin();
                T value = message.getValue();
                R res = transformation.transform(value, lastReturnedValue);

                // saves the value in case it is needed for next calculation
                lastReturnedValue = res;

                getSender().tell(new ReturnMessage<R>(res), getSelf());
                dataMaker.tell(
                        new SendValue(getSelf().path().name(), res.toString(), time),
                        getSelf());
            }
        }
    };
}
