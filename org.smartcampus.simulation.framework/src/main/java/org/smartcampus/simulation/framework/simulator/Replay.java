package org.smartcampus.simulation.framework.simulator;

import java.util.Random;
import org.smartcampus.simulation.framework.messages.InitInput;
import org.smartcampus.simulation.framework.messages.InitTypeSimulation;
import org.smartcampus.simulation.framework.messages.SendValue;
import org.smartcampus.simulation.framework.messages.StartSimulation;
import org.smartcampus.simulation.framework.messages.UpdateSimulation;
import scala.concurrent.duration.Duration;
import akka.actor.Props;
import akka.japi.Procedure;
import akka.routing.DefaultResizer;
import akka.routing.RoundRobinPool;

public abstract class Replay extends Simulation<String> {

    /**
     * The path of the input file
     */
    private String input;

    /**
     * the number of the line that we have to read
     */
    protected int nbLineToRead;

    public Replay() {
        super();
        this.simulationStarted = new ReplayProcedure();
    }

    /**
     * return the value of the next line
     * 
     * @return the value present at the next line
     */
    protected abstract String getNextValue();

    /**
     * return the number of line in the input file
     */
    protected abstract int getnbLine();

    /**
     * closing the Input after reading the file
     */
    protected abstract void close();

    /**
     * going to the first line wanted in the file and returning the value of this line
     * 
     * @param firstLine
     *            the number of the first line of the file
     * @return the value at this line
     */
    protected abstract String beginReplay(int firstLine);

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReceive(final Object o) throws Exception {
        super.onReceive(o);
        if (o instanceof StartSimulation) {
            this.startSimulation();
        }
        else if (o instanceof InitTypeSimulation) {
            this.initTypeSimulation((InitTypeSimulation) o);
        }
        else if (o instanceof InitInput) {
            this.input = ((InitInput) o).getInput();

        }
        // TODO
    }

    /**
     * Handle the message InitTypeSimulation
     * 
     * @param message
     *            contains the initialization of the simulation
     */
    private void initTypeSimulation(final InitTypeSimulation message) {
        if (this.frequency == this.realTimeFrequency.toMillis()) {
            this.dataMaker = this.getContext().actorOf(
                    new RoundRobinPool(15).withResizer(new DefaultResizer(1, 15)).props(
                            Props.create(DataSender.class, this.output)),
                    "simulationDataSender");
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
        this.nbLineToRead = (int) (this.duration / this.frequency);
        this.valueToSend = this.beginReplay(this.getLineToStart());
        this.tick = this
                .getContext()
                .system()
                .scheduler()
                .schedule(Duration.Zero(), this.realTimeFrequency, this.getSelf(),
                        new UpdateSimulation(), this.getContext().dispatcher(), null);

        this.getContext().become(this.simulationStarted);
    }

    /**
     * The first line to start replaying the datas
     * Taking a random number of line in the interval [0; nbLineInFile-nbLineToSend]
     * 
     * @return
     */
    private int getLineToStart() {
        Random rand = new Random();

        return rand.nextInt((int) (this.getnbLine() - (this.duration / this.frequency)));
    }

    /**
     * @return the input
     */
    protected String getInput() {
        return this.input;
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
            // TODO
            Replay.this.dataMaker.tell(new SendValue(Replay.this.getSelf().path().name(),
                    Replay.this.getNextValue(), Replay.this.time), Replay.this.getSelf());
            Replay.this.time += Replay.this.frequency;
        }
    }

    @Override
    public final void postStop() {
        super.postStop();
        this.close();
    }

}
