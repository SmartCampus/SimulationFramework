package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitSensorSimulation;
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
import akka.actor.Cancellable;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
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
public abstract class SimulationLaw<S, T, R> extends UntypedActor {
    /** This router allows to broadcast to all the sensors */
    private Router           router;

    /** The DataMaker that allow the Simulation to send direct value */
    private ActorRef         dataMaker;

    /** The scheduler which send an update every realTimeFrequency */
    private Cancellable      tick;

    /** The T value to send to the sensors */
    private T                valueToSend;

    /** The real time frequency correspond to the duration */
    private FiniteDuration   realTimeFrequency;

    /** Each real time frequency, the time is increased by the frequency */
    private long             frequency;

    /** The law associated to the SimulationLaw */
    private Law<S, T>        law;

    /** The current time of the simulation */
    private long             time;

    /** This list contains the result of the sensors */
    protected List<R>        values;

    /** Allow to print logs */
    protected LoggingAdapter log;

    /** Default constructor */
    public SimulationLaw() {
        this.values = new LinkedList<R>();
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    /**
     * Return the current time of the simulation
     * 
     * @return the current time of the simulation
     */
    public long getTime() {
        return this.time;
    }

    /**
     * Return an array of values which corresponds to the parameter of the associated
     * Law's method 'evaluate'
     * 
     * @return an array of type S
     */
    protected abstract S[] computeValue();

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

    @SuppressWarnings("unchecked")
    @Override
    /**
     * @inheritDoc
     */
    public final void onReceive(final Object o) throws Exception {
        if (o instanceof InitTypeSimulation) {
            InitTypeSimulation message = (InitTypeSimulation) o;
            this.time = message.getBegin();
            this.realTimeFrequency = message.getRealTimeFrequency();
            this.frequency = message.getFrequency();

            InitSensorSimulation init;
            if (this.frequency == this.realTimeFrequency.toMillis()) {
                this.dataMaker = this.getContext().actorOf(
                        new RoundRobinPool(5).withResizer(new DefaultResizer(1, 5))
                                .props(Props.create(DataSender.class)),
                        "simulationDataSender");
                init = new InitSensorSimulation(null);
            }
            else {
                this.dataMaker = this.getContext().actorOf(
                        Props.create(DataWriter.class), "simulationDataWriter");
                init = new InitSensorSimulation(this.dataMaker);
            }

            this.router.route(init, this.getSelf());
        }
        else if (o instanceof StartSimulation) {
            this.valueToSend = this.law.evaluate(this.computeValue());

            this.tick = this
                    .getContext()
                    .system()
                    .scheduler()
                    .schedule(Duration.Zero(), this.realTimeFrequency, this.getSelf(),
                            new UpdateSimulation(), this.getContext().dispatcher(), null);
        }
        else if (o instanceof AddSensor) {
            AddSensor message = (AddSensor) o;
            if (message.getSensorTransformation() instanceof SensorTransformation<?, ?>) {
                SensorTransformation<S, R> t = (SensorTransformation<S, R>) message
                        .getSensorTransformation();
                this.createSensor(message.getNbSensors(), t);
            }
            else {
                // TODO error
            }
        }
        else if (o instanceof InitSimulationLaw) {
            InitSimulationLaw message = (InitSimulationLaw) o;
            if (message.getLaw() instanceof Law<?, ?>) {
                this.law = (Law<S, T>) message.getLaw();
            }
            else {
                // TODO error
            }
        }
        else if (o instanceof UpdateSimulation) {
            this.router.route(new UpdateSensorSimulation<T>(this.time, this.valueToSend),
                    this.getSelf());
            this.time += this.frequency;
        }
        else if (o instanceof ReturnMessage<?>) {
            ReturnMessage<R> message = (ReturnMessage<R>) o;
            this.values.add(message.getResult());

            if (this.values.size() == this.router.routees().size()) {
                this.valueToSend = this.law.evaluate(this.computeValue());
                this.onComplete();
                this.values.clear();
            }
        }
    }

    @Override
    /**
     * @inheritDoc
     */
    public final void postStop() {
        this.tick.cancel();
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
                value, this.time), this.getSelf());
    }
}
