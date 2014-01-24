package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.InitSensorRealSimulation;
import org.smartcampus.simulation.framework.messages.InitSensorVirtualSimulation;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.japi.Procedure;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;

/**
 * A Sensor is an akka Actor. It transforms the value receive by the SimulationLaw in the
 * type of the HTTP request
 * 
 * @param <T>
 *            corresponds to the return type of the associated Law's method 'evaluate'
 * @param <R>
 *            corresponds to the HTTP request type value
 */
public final class Sensor<T, R> extends UntypedActor {

    /**
     * This class is a new procedure used in the context of 'Simulation Started'
     */
    private class ProcedureSimulationStarted implements Procedure<Object> {

        /**
         * @inheritDoc
         */
        @SuppressWarnings("unchecked")
        @Override
        public void apply(final Object o) {
            if (o instanceof UpdateSensorSimulation) {
                UpdateSensorSimulation<T> message = (UpdateSensorSimulation<T>) o;
                long time = message.getBegin();
                T value = message.getValue();
                R res = Sensor.this.transformation.transform(value,
                        Sensor.this.lastReturnedValue);

                // saves the value in case it is needed for next calculation
                Sensor.this.lastReturnedValue = res;

                Sensor.this.getSender().tell(new ReturnMessage<R>(res),
                        Sensor.this.getSelf());
                Sensor.this.dataMaker.tell(new SendValue(Sensor.this.getSelf().path()
                        .name(), res.toString(), time), Sensor.this.getSelf());
            }
        }
    }

    /** The last returned value */
    private R lastReturnedValue;
    /** The transformation */
    private SensorTransformation<T, R> transformation;
    /** Context when the simulation start */
    private Procedure<Object> simulationStarted;

    /**
     * The output Actor. It can be a DataSender(HTTP Request) or a DataWriter(File). if it
     * is a DataWriter, it is shared by every sensors
     */
    private ActorRef dataMaker;

    /** default constructor */
    public Sensor(final SensorTransformation<T, R> t) {
        this.transformation = t;
        this.simulationStarted = new ProcedureSimulationStarted();
    }

    @Override
    /**
     * @inheritDoc
     */
    public void onReceive(final Object o) throws Exception {
        if (o instanceof InitSensorRealSimulation) {
            InitSensorRealSimulation message = (InitSensorRealSimulation) o;
            this.initSensorRealSimulation(message);

        }
        else if (o instanceof InitSensorVirtualSimulation) {
            InitSensorVirtualSimulation message = (InitSensorVirtualSimulation) o;
            this.initSensorVirtualSimulation(message);
        }
    }

    /**
     * Handle the message initSensorRealSimulation
     * 
     * @param message
     *            the message initSensorRealSimulation
     */
    private void initSensorRealSimulation(final InitSensorRealSimulation message) {
        String s = message.getUrl();
        ActorRef counter = message.getCounter();
        this.dataMaker = this.getContext().actorOf(
                new RoundRobinPool(5).withResizer(new DefaultResizer(1, 5)).props(
                        Props.create(DataSender.class, s, counter)),
                "Sensor" + this.getSelf().path().name());
        this.lastReturnedValue = null;
        this.getContext().become(this.simulationStarted);
    }

    /**
     * Handle the message initSensorVirtualSimulation
     * 
     * @param message
     *            the message initSensorVirtualSimulation
     */
    private void initSensorVirtualSimulation(final InitSensorVirtualSimulation message) {
        ActorRef tmp = message.getDataMaker();
        this.dataMaker = tmp;
        this.lastReturnedValue = null;
        this.getContext().become(this.simulationStarted);
    }
}
