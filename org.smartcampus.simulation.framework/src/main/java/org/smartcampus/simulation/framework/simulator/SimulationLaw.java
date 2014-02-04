package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitSensorRealSimulation;
import org.smartcampus.simulation.framework.messages.InitSensorVirtualSimulation;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSimulation;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import akka.routing.Routee;
import akka.routing.Router;

/**
 * 
 * A SimulationLaw is a manager of sensors.
 * 
 * It can also send some value directly to a DataMaker(eg : average)
 * 
 * You have to implements the method computeValue and optionally the method onComplete
 * 
 * @param <S>
 *            corresponds to the type of the parameter of the associated Law's method
 *            'evaluate'
 * @param <T>
 *            corresponds to the return type of the associated Law's method 'evaluate'
 * @param <R>
 *            corresponds to the HTTP request type value
 */
public abstract class SimulationLaw<S, T, R> extends Simulation<T> {
    /** The T value to send to the sensors */
    protected T valueToSend;

    /** This list contains the result of the sensors */
    protected List<R> values;

    /** This router allows to broadcast to all the sensors */
    private Router router;

    /** The law associated to the SimulationLaw */
    private Law<S, T> law;

    /** The current time of the simulation */
    private long time;

    /** The real time frequency correspond to the duration */
    protected FiniteDuration realTimeFrequency;

    /** Each real time frequency, the time is increased by the frequency */
    protected long frequency;

    /** The duration of the simulation */
    protected long duration;

    /** Default constructor */
    public SimulationLaw() {
        super();
        this.values = new LinkedList<R>();
        this.simulationStarted = new SimulationLawProcedure();
    }

    /**
     * Return an array of values which corresponds to the parameter of the associated
     * Law's method 'evaluate'
     * 
     * @return an array of type S
     */
    protected abstract S[] computeValue();

    /**
     * Return the current time of the simulation
     * 
     * @return the time in milliseconds
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Allows to create sensors.
     * 
     * @param numberOfSensors
     *            the number of sensors to create
     * 
     * @param transformation
     *            the transformation of the sensor
     */
    private void createSensor(final int numberOfSensors,
            final SensorTransformation<S, R> transformation) {

        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < numberOfSensors; i++) {
            ActorRef r = this.getContext().actorOf(
                    Props.create(Sensor.class, transformation),
                    this.getSelf().path().name() + "-" + i);
            this.getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        this.router = new Router(new BroadcastRoutingLogic(), routees);
    }

    /**
     * Describe in this method all you want to do when you receive all the result from the
     * sensors
     * E.g : calculate the average or whatever and send it using sendValue
     */
    protected abstract void onComplete();

    /**
     * {@inheritDoc}
     */
    @Override
    public final void onReceive(final Object o) throws Exception {
        super.onReceive(o);
        if (o instanceof InitTypeSimulation) {
            InitTypeSimulation message = (InitTypeSimulation) o;
            this.initTypeSimulation(message);
        }
        else if (o instanceof StartSimulation) {
            this.startSimulation();
        }
        else if (o instanceof AddSensor) {
            AddSensor message = (AddSensor) o;
            this.addSensor(message);
        }
        else if (o instanceof InitSimulationLaw) {
            InitSimulationLaw message = (InitSimulationLaw) o;
            this.initSimulationLaw(message);
        }
    }

    /**
     * Handle the message InitTypeSimulation
     * 
     * @param message
     *            contains the initialization of the simulation
     */
    private void initTypeSimulation(final InitTypeSimulation message) {
        this.time = message.getBegin();
        this.realTimeFrequency = message.getRealTimeFrequency();
        this.frequency = message.getFrequency();
        this.duration = message.getDuration().toMillis();
        if (this.frequency == this.realTimeFrequency.toMillis()) {
            this.dataMaker = this.getContext().actorOf(
                    new RoundRobinPool(5).withResizer(new DefaultResizer(1, 5)).props(
                            Props.create(DataSender.class, this.output)),
                    "simulationDataSender");
            InitSensorRealSimulation init = new InitSensorRealSimulation(this.output);
            this.router.route(init, this.getSelf());
        }
        else {
            this.dataMaker = this.getContext().actorOf(
                    Props.create(DataWriter.class, this.output), "simulationDataWriter");
            InitSensorVirtualSimulation init = new InitSensorVirtualSimulation(
                    this.dataMaker);
            this.router.route(init, this.getSelf());
        }

    }

    /**
     * Handle the message StartSimulation
     */
    private void startSimulation() throws Exception {
        if (this.law != null) {
            this.valueToSend = this.law.evaluate(this.computeValue());
        }
        else {
            this.valueToSend = null;
        }

        this.tick = this
                .getContext()
                .system()
                .scheduler()
                .schedule(Duration.Zero(), this.realTimeFrequency, this.getSelf(),
                        new UpdateSimulation(), this.getContext().dispatcher(), null);

        this.getContext().become(this.simulationStarted);
    }

    /**
     * Handle the message AddSensor
     * 
     * @param message
     *            contains the number of sensors to add
     */
    @SuppressWarnings("unchecked")
    private void addSensor(final AddSensor message) {
        if (message.getSensorTransformation() instanceof SensorTransformation<?, ?>) {
            SensorTransformation<S, R> t = (SensorTransformation<S, R>) message
                    .getSensorTransformation();
            this.createSensor(message.getNbSensors(), t);
        }
        else {
            // TODO error
        }
    }

    /**
     * Handle the message InitSimulationLaw
     * 
     * @param message
     *            contains the initialization of the simulation law
     */
    @SuppressWarnings("unchecked")
    private void initSimulationLaw(final InitSimulationLaw message) {
        if (message.getLaw() instanceof Law<?, ?>) {
            this.law = (Law<S, T>) message.getLaw();
        }
        else {
            // TODO error
        }
    }

    /**
     * Simulate a virtual sensor that send data
     * 
     * @param name
     *            the name of the sensor
     * @param value
     *            the value of the sensor
     */
    public final void sendValue(final String name, final String value) {
        this.dataMaker.tell(new SendValue(this.getSelf().path().name() + " - " + name,
                value, this.time - this.frequency), this.getSelf());
    }

    /**
     * This class is a new procedure used in the context of 'Simulation Started'
     */
    private class SimulationLawProcedure implements Procedure<Object> {

        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public void apply(final Object o) throws Exception {
            if (o instanceof UpdateSimulation) {
                this.updateSimulation();
            }
            else if (o instanceof ReturnMessage<?>) {
                ReturnMessage<R> message = (ReturnMessage<R>) o;
                this.returnMessage(message);
            }
        }

        /**
         * Handle the message returnMessage
         * 
         * @param message
         *            contains the value of a sensor
         */
        private void returnMessage(final ReturnMessage<R> message) throws Exception {
            SimulationLaw.this.values.add(message.getResult());

            if (SimulationLaw.this.values.size() == SimulationLaw.this.router.routees()
                    .size()) {
                if (SimulationLaw.this.law != null) {
                    SimulationLaw.this.valueToSend = SimulationLaw.this.law
                            .evaluate(SimulationLaw.this.computeValue());
                }
                else {
                    SimulationLaw.this.valueToSend = null;
                }
                SimulationLaw.this.onComplete();
                SimulationLaw.this.values.clear();
            }
        }

        /**
         * Handle the message UpdateSimulation
         */
        private void updateSimulation() {
            SimulationLaw.this.router.route(new UpdateSensorSimulation<T>(
                    SimulationLaw.this.time, SimulationLaw.this.valueToSend),
                    SimulationLaw.this.getSelf());
            SimulationLaw.this.time += SimulationLaw.this.frequency;
        }

    }

}
