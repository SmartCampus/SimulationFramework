package org.smartcampus.simulation.framework.simulator;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.smartcampus.simulation.framework.messages.InitInput;
import org.smartcampus.simulation.framework.messages.InitReplay;
import org.smartcampus.simulation.framework.messages.InitReplayParam;
import org.smartcampus.simulation.framework.messages.InitReplaySimulation;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSimulation;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.Procedure;

public final class Replay extends Simulation<String> {

    /**
     * The FileFormator using for specializing the reading of the file
     */
    private FileFormator formator;

    /** The current time of the replay */
    private long time;

    public Replay() {
        super();
        this.simulationStarted = new ReplayProcedure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Object o) throws Exception {
        super.onReceive(o);
        if (o instanceof StartSimulation) {
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
                this.getContext().actorOf(Props.create(DataSender.class, this.output), s);
            }
        }
        else {
            this.dataMaker = this.getContext().actorOf(
                    Props.create(DataWriter.class, this.output), "simulationDataWriter");
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

                Replay.this.tick = Replay.this
                        .getContext()
                        .system()
                        .scheduler()
                        .scheduleOnce(
                                Duration.create(nextFrequency, TimeUnit.MILLISECONDS),
                                Replay.this.getSelf(), new UpdateSimulation(),
                                Replay.this.getContext().dispatcher(), null);
            }
        }
    }

    @Override
    public final void postStop() {
        super.postStop();
        this.formator.close();
    }

}
