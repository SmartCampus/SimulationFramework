package org.smartcampus.simulation.framework.simulator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.smartcampus.simulation.framework.messages.*;
import org.smartcampus.simulation.framework.messages.StartDelayedSimulation;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.japi.Procedure;

public final class Replay extends Simulation<String> {

    /**
     * The FileFormator using for specializing the reading of the file
     */
    private FileFormator formator;

    /** The current time of the replay */
    private long time;

    /** The number of dataSender created */
    private int nbDataSenderCreated;

    public Replay() {
        super();
        this.simulationStarted = new ReplayProcedure();
        this.nbDataSenderCreated = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Object o) throws Exception {
        super.onReceive(o);
        if (o instanceof StartSimulationNow) {
            this.startSimulation();
        }
        else if (o instanceof InitReplay) {
            Class<? extends FileFormator> tmp = ((InitReplay) o).getFormator();
            this.formator = tmp.newInstance();
        }
        else if (o instanceof InitReplaySimulation) {
            this.initReplaySimulation((InitReplaySimulation) o);
        }
        else if (o instanceof InitInput) {
            String input = ((InitInput) o).getInput();
            this.formator.setInput(input);
        }
        else if (o instanceof InitReplayParam) {
            InitReplayParam message = (InitReplayParam) o;
            this.formator.initReplayParam(message.getKey(), message.getValue());
        }
    }

    /**
     * Handle the message InitTypeSimulation
     * 
     * @param o
     *            contains the initialization of the simulation
     */
    private void initReplaySimulation(final InitReplaySimulation o) {
        this.time = o.getStart();
        if (o.isReal()) {
            this.dataMaker = null;
            for (String s : this.formator.params.keySet()) {
                ActorRef tmp = this.getContext().actorOf(
                        Props.create(DataSender.class, this.output), s);
                this.getContext().watch(tmp);
                this.nbDataSenderCreated++;
            }
        }
        else {
            this.dataMaker = this.getContext().actorOf(
                    Props.create(DataWriter.class, this.output), "simulationDataWriter");
            this.getContext().watch(this.dataMaker);
        }
    }

    /**
     * Handle the message StartSimulation
     */
    private void startSimulation() throws Exception {
        this.formator.beginReplay();
        this.getContext().become(this.simulationStarted);
        this.getSelf().tell(new UpdateSimulation(), this.getSelf());
    }

    /**
     * This class is a new procedure used in the context of 'Simulation Started'
     */
    private class ReplayProcedure implements Procedure<Object> {

        /**
         * {@inheritDoc}
         */
        @Override
        public void apply(final Object o) throws Exception {
            if (o instanceof UpdateSimulation) {
                this.updateSimulation();
            }
            else if (o instanceof Terminated) {
                if (Replay.this.dataMaker != null) {
                    Replay.this.log.debug("Tous mes fils sont mort, je me suicide");

                    Replay.this.getSelf().tell(PoisonPill.getInstance(),
                            ActorRef.noSender());
                }
                else {
                    this.terminated();
                }
            }
            else if (o instanceof CountRequestsPlusOne) {
                Replay.this.getContext().parent().tell(o, Replay.this.getSelf());
            }
            else if (o instanceof CountResponsesPlusOne) {
                Replay.this.getContext().parent().tell(o, Replay.this.getSelf());
            }
        }

        /**
         * Handle the message Terminated
         */
        private void terminated() {
            Replay.this.nbDataSenderCreated--;
            if (Replay.this.nbDataSenderCreated == 0) {
                Replay.this.log.debug("Tous mes fils sont mort, je me suicide");

                Replay.this.getSelf().tell(PoisonPill.getInstance(), ActorRef.noSender());
            }
        }

        /**
         * Handle the message UpdateSimulation
         */
        private void updateSimulation() {
            Map<String, String> nextValue = Replay.this.formator.getNextValue();

            long nextFrequency = Replay.this.formator.getNextFrequency();

            if (Replay.this.dataMaker == null) {
                for (String sensor : nextValue.keySet()) {
                    ActorRef actorTmp = Replay.this.getContext().getChild(sensor);

                    actorTmp.tell(new SendValue(sensor, nextValue.get(sensor),
                            Replay.this.time), Replay.this.getSelf());
                }
            }
            else {
                for (String sensor : nextValue.keySet()) {
                    Replay.this.dataMaker
                            .tell(new SendValue(sensor, nextValue.get(sensor),
                                    Replay.this.time), Replay.this.getSelf());
                }
            }

            if (Replay.this.formator.hasNextLine()) {
                Replay.this.time += nextFrequency;

                if (Replay.this.dataMaker == null) {
                    Replay.this.tick = Replay.this
                            .getContext()
                            .system()
                            .scheduler()
                            .scheduleOnce(
                                    Duration.create(nextFrequency, TimeUnit.MILLISECONDS),
                                    Replay.this.getSelf(), new UpdateSimulation(),
                                    Replay.this.getContext().dispatcher(), null);
                }
                else {
                    Replay.this.getSelf().tell(new UpdateSimulation(),
                            Replay.this.getSelf());
                }
            }
            else {
                Replay.this.log.debug("Fin du replay, Je tue mes fils");
                if (Replay.this.dataMaker == null) {
                    for (String s : Replay.this.formator.params.keySet()) {
                        ActorRef actorTmp = Replay.this.getContext().getChild(s);

                        actorTmp.tell(PoisonPill.getInstance(), Replay.this.getSelf());
                    }
                }
                else {
                    Replay.this.dataMaker.tell(PoisonPill.getInstance(),
                            Replay.this.getSelf());
                }
            }

        }
    }

    @Override
    public final void postStop() {
        super.postStop();
        this.formator.close();
    }

}
