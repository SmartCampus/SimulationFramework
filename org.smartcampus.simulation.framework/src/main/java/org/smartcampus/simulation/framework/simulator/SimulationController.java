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

    /** Default Contructor */
    public SimulationController() {
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    @Override
    public void onReceive(final Object arg0) throws Exception {
        if (arg0 instanceof CreateSimulationLaw) {
            CreateSimulationLaw tmp = (CreateSimulationLaw) arg0;

            this.getContext().actorOf(Props.create(tmp.getSimulationLawClass()),
                    tmp.getName());

            // TODO A mettre dans un systeme de log
            this.log.debug("Je cree un Parking");
        }
        else if (arg0 instanceof AddSensor) {
            AddSensor tmp = (AddSensor) arg0;
            ActorRef actorTmp = this.getContext().getChild(tmp.getName());
            actorTmp.tell(tmp, this.getSelf());

            // TODO A mettre dans un systeme de log
            this.log.debug("J'ajoute des capteurs");
        }
        else if (arg0 instanceof InitSimulationLaw) {
            InitSimulationLaw tmp = (InitSimulationLaw) arg0;
            ActorRef actorTmp = this.getContext().getChild(tmp.getName());
            actorTmp.tell(tmp, this.getSelf());

            // TODO A mettre dans un systeme de log
            this.log.debug("J'initialise un parking");
        }
        else if (arg0 instanceof InitTypeSimulation) {
            InitTypeSimulation tmp = (InitTypeSimulation) arg0;
            this.duration = tmp.getDuration();
            this.frequency = tmp.getFrequency();
            this.realTimeFrequency = tmp.getRealTimeFrequency().toMillis();

            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(tmp, this.getSelf());
            }

            // TODO A mettre dans un systeme de log
            this.log.debug("J'initialise le type de la simulation");
        }
        else if (arg0 instanceof StartSimulation) {
            StartSimulation tmp = (StartSimulation) arg0;
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(tmp, this.getSelf());
            }

            // TODO A mettre dans un systeme de log
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
        else if (arg0 instanceof InitOutput) {
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(arg0, this.getSelf());
            }

            // TODO A mettre dans un systeme de log
            this.log.debug("Je transmet le set de l'output");
        }
        else {

            // TODO A mettre dans un systeme de log
            this.log.debug("J'ai recu un message que je ne comprends pas");
        }
    }

    private Procedure<Object> simulationStarted = new Procedure<Object>() {
        @Override
        public void apply(final Object message) {

        }
    };

    @Override
    public void postStop() throws Exception {
        this.log.debug("Je me suicide");
        this.getContext().parent().tell(PoisonPill.getInstance(), this.getSelf());
    }
}
