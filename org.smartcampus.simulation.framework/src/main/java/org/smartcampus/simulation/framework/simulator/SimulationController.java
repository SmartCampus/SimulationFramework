package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.AddSensor;
import org.smartcampus.simulation.framework.messages.CountRequestsPlusOne;
import org.smartcampus.simulation.framework.messages.CountResponsesPlusOne;
import org.smartcampus.simulation.framework.messages.CreateSimulation;
import org.smartcampus.simulation.framework.messages.InitInput;
import org.smartcampus.simulation.framework.messages.InitOutput;
import org.smartcampus.simulation.framework.messages.InitReplay;
import org.smartcampus.simulation.framework.messages.InitReplayParam;
import org.smartcampus.simulation.framework.messages.InitReplaySimulation;
import org.smartcampus.simulation.framework.messages.InitSimulationLaw;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Terminated;
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
    /** Context when the simulation start */
    private Procedure<Object> simulationStarted;
    /** Number of simulation created */
    private int nbSimulationCreated;

    /** Default Constructor */
    public SimulationController() {
        this.log = Logging.getLogger(this.getContext().system(), this);
        this.simulationStarted = new SimulationControlerProcedure();

        this.nbSimulationCreated = 0;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onReceive(final Object arg0) throws Exception {
        if (arg0 instanceof CreateSimulation) {
            CreateSimulation tmp = (CreateSimulation) arg0;
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
        else if (arg0 instanceof InitReplaySimulation) {
            InitReplaySimulation tmp = (InitReplaySimulation) arg0;
            this.initReplaySimulation(tmp);
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
        else if (arg0 instanceof InitInput) {
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(arg0, this.getSelf());
            }
            this.log.debug("Je transmet le set de l'input");
        }
        else if (arg0 instanceof InitReplayParam) {
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(arg0, this.getSelf());
            }
            this.log.debug("Je transmet une paire sensor/colonne");
        }
        else if (arg0 instanceof InitReplay) {
            for (ActorRef a : this.getContext().getChildren()) {
                a.tell(arg0, this.getSelf());
            }
            this.log.debug("Je transmet le FileFormator");
        }
    }

    /**
     * Handle the message CreateSimulationLaw
     * 
     * @param tmp
     *            the message CreateSimulationLaw
     */
    private void createSimulationLaw(final CreateSimulation tmp) {
        ActorRef r = this.getContext().actorOf(Props.create(tmp.getSimulationClass()),
                tmp.getName());

        this.getContext().watch(r);
        this.nbSimulationCreated++;
        this.log.debug("Je cree une Simulation");
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
        InitTypeSimulation mess = tmp;
        if (mess.getFrequency() == mess.getRealTimeFrequency().toMillis()) {
            this.getContext().actorOf(Props.create(CounterResponse.class),
                    "CounterResponses");
        }
        for (ActorRef a : this.getContext().getChildren()) {
            a.tell(tmp, this.getSelf());
        }

        this.log.debug("J'initialise le type de la simulation");
    }

    /**
     * Handle the message InitReplaySimulation
     * 
     * @param tmp
     *            the message InitReplaySimulation
     */
    private void initReplaySimulation(final InitReplaySimulation tmp) {
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

        this.getContext().become(this.simulationStarted);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void postStop() throws Exception {
        this.log.debug("Je me suicide et je tue l'acteur system");
        this.getContext().parent().tell(PoisonPill.getInstance(), this.getSelf());
    }

    /**
     * This class is a new procedure used in the context of 'Simulation Started'
     */
    private class SimulationControlerProcedure implements Procedure<Object> {

        /**
         * @inheritDoc
         */
        @Override
        public void apply(final Object message) {
            if (message instanceof Terminated) {
                SimulationController.this.nbSimulationCreated--;
                if (SimulationController.this.nbSimulationCreated == 0) {
                    SimulationController.this.log.debug("Tous mes fils sont morts !");
                    SimulationController.this.getSelf().tell(PoisonPill.getInstance(),
                            ActorRef.noSender());
                }
            }
            else if (message instanceof CountRequestsPlusOne) {
                SimulationController.this.getContext().getChild("CounterResponses")
                        .tell(message, SimulationController.this.getSelf());
            }
            else if (message instanceof CountResponsesPlusOne) {
                SimulationController.this.getContext().getChild("CounterResponses")
                        .tell(message, SimulationController.this.getSelf());
            }
        }
    }

}
