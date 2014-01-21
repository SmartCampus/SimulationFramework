package org.smartcampus.simulation.framework.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.SendValue;
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
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;
import akka.routing.Routee;
import akka.routing.Router;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class SimulationLaw<S, T, R> extends UntypedActor {

    private Router           router;
    private ActorRef         dataSender;
    protected Law<S, T>      law;
    private T                valueToSend;
    protected int            time;
    private int              interval;
    private Cancellable      tick;
    protected List<R>        values;

    protected LoggingAdapter log;

    public SimulationLaw() {
        this.values = new LinkedList<R>();
        this.log = Logging.getLogger(this.getContext().system(), this);
        this.dataSender = this.getContext().actorOf(
                new RoundRobinPool(5).withResizer(new DefaultResizer(1, 10)).props(
                        Props.create(DataSender.class)), "simulationDataSender");
    }

    protected abstract S[] computeValue();

    private void createSensor(final int numberOfSensors,
            final SensorTransformation<S, R> t) {

        List<Routee> routees = new ArrayList<Routee>();
        for (int i = 0; i < numberOfSensors; i++) {
            ActorRef r = this.getContext().actorOf(Props.create(Sensor.class, t),
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
    public final void onReceive(final Object o) throws Exception {
        if (o instanceof StartSimulation) {
            StartSimulation message = (StartSimulation) o;
            this.time = message.getBegin();
            this.interval = message.getInterval();

            this.valueToSend = this.law.evaluate(this.computeValue());

            this.tick = this
                    .getContext()
                    .system()
                    .scheduler()
                    .schedule(Duration.Zero(),
                            Duration.create(this.interval, TimeUnit.SECONDS),
                            this.getSelf(), new UpdateSimulation(),
                            this.getContext().dispatcher(), null);
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
            this.time++;
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
        this.dataSender.tell(new SendValue(this.getSelf().path().name() + " - " + name,
                value, this.time), this.getSelf());
    }
}
