package org.smartcampus.simulation.framework.simulator;

import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.CreateSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * The SimulationController allows to transfer a message to a specific son or to
 * broadcast it.
 * It also kills all the actors once the 'duration' time arrives to an end.
 */
public final class SimulationController extends UntypedActor {

    /** Allow to print logs */
    private LoggingAdapter log;
    /** The duration of the simulation */
    private FiniteDuration duration;
    /** The update frequency to the virtual time */
    private long frequency;
    /** The sensor's request frequency */
    private long realTimeFrequency;
    /** Context when the simulation start */
    private Procedure<Object> simulationStarted;

    /** Default Constructor */
    public SimulationController() {
        this.log = Logging.getLogger(this.getContext().system(), this);
        this.simulationStarted = new Procedure<Object>() {
            @Override
            public void apply(final Object message) {
            }
        };
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onReceive(final Object arg0) throws Exception {
        if (arg0 instanceof CreateSimulationLaw) {
            CreateSimulationLaw tmp = (CreateSimulationLaw) arg0;
            this.createSimulationLaw(tmp);
        }
        else if (arg0 instanceof AddSensor) {
            AddSensor tmp = (AddSensor) arg0;
            this.addSensor(tmp);
        }
        else if (arg0 instanceof InitSimulationLaw) {
            InitSimulationLaw tmp = (InitSimulationLaw) arg0;
            this.initSimulationLaw(tmp);
        }
        else if (arg0 instanceof InitTypeSimulation) {
            InitTypeSimulation tmp = (InitTypeSimulation) arg0;
            this.initTypeSimulation(tmp);
        }
        else if (arg0 instanceof StartSimulation) {
            StartSimulation tmp = (StartSimulation) arg0;
            this.startSimulation(tmp);
        }
        else if (arg0 instanceof InitOutput) {
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(arg0, this.getSelf());
            }
            this.log.debug("Je transmet le set de l'output");
        }

    }

    /**
     * Handle the message CreateSimulationLaw
     * 
     * @param tmp
     *            the message CreateSimulationLaw
     */
    private void createSimulationLaw(final CreateSimulationLaw tmp) {
        this.getContext().actorOf(Props.create(tmp.getSimulationLawClass()),
                tmp.getName());

        this.log.debug("Je cree un Parking");
    }

    /**
     * Handle the message AddSensor
     * 
     * @param tmp
     *            the message AddSensor
     */
    private void addSensor(final AddSensor tmp) {
        ActorRef actorTmp = this.getContext().getChild(tmp.getName());
        actorTmp.tell(tmp, this.getSelf());

        this.log.debug("J'ajoute des capteurs");
    }

    /**
     * Handle the message InitSimulationLaw
     * 
     * @param tmp
     *            the message InitSimulationLaw
     */
    private void initSimulationLaw(final InitSimulationLaw tmp) {
        ActorRef actorTmp = this.getContext().getChild(tmp.getName());
        actorTmp.tell(tmp, this.getSelf());

        this.log.debug("J'initialise un parking");
    }

    /**
     * Handle the message InitTypeSimulation
     * 
     * @param tmp
     *            the message InitTypeSimulation
     */
    private void initTypeSimulation(final InitTypeSimulation tmp) {
        this.duration = tmp.getDuration();
        this.frequency = tmp.getFrequency();
        this.realTimeFrequency = tmp.getRealTimeFrequency().toMillis();

        for (ActorRef a : this.getContext().getChildren()) {
            a.tell(tmp, this.getSelf());
        }

        this.log.debug("J'initialise le type de la simulation");
    }

    /**
     * Handle the message StartSimulation
     * 
     * @param tmp
     *            the message StartSimulation
     */
    private void startSimulation(final StartSimulation tmp) {
        for (ActorRef a : this.getContext().getChildren()) {
            a.tell(tmp, this.getSelf());
        }

        this.log.debug("Je lance la simulation");

        if (this.frequency == this.realTimeFrequency) {
            this.getContext()
                    .system()
                    .scheduler()
                    .scheduleOnce(this.duration, this.getSelf(),
                            PoisonPill.getInstance(), this.getContext().dispatcher(),
                            null);
        }
        else {

            long virtualDuration = ((this.duration.toMillis() / this.frequency) * this.realTimeFrequency)
                    + (this.realTimeFrequency / 4);

            this.getContext()
                    .system()
                    .scheduler()
                    .scheduleOnce(
                            Duration.create(virtualDuration, TimeUnit.MILLISECONDS),
                            this.getSelf(), PoisonPill.getInstance(),
                            this.getContext().dispatcher(), null);
        }

        this.getContext().become(this.simulationStarted);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void postStop() throws Exception {
        this.log.debug("Je me suicide");
        this.getContext().parent().tell(PoisonPill.getInstance(), this.getSelf());
    }
}
