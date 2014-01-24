package org.smartcampus.simulation.framework.simulator;

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
     */
    private int field;

    public Replay() {
        super();

        this.simulationStarted = new ReplayProcedure();
    }

    /**
     * return the value located at the line l
     * 
     * @param l
     *            the number of the line
     */
    protected abstract String getvalue(final int l);

    /**
     * return the number of line in the input file
     */
    protected abstract void getnbLine();

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
        this.tick = this
                .getContext()
                .system()
                .scheduler()
                .schedule(Duration.Zero(), this.realTimeFrequency, this.getSelf(),
                        new UpdateSimulation(), this.getContext().dispatcher(), null);

        this.getContext().become(this.simulationStarted);
    }

    /**
     * @return the input
     */
    protected String getInput() {
        return this.input;
    }

    /**
     * @param input
     *            the input to set
     */
    protected void setInput(final String input) {
        this.input = input;
    }

    /**
     * @return the field
     */
    protected int getField() {
        return this.field;
    }

    /**
     * @param field
     *            the field to set
     */
    protected void setField(final int field) {
        this.field = field;
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
            Replay.this.dataMaker.tell(new SendValue(Replay.this.getSelf().path().name()
                    + " - ", Replay.this.getvalue(0), Replay.this.time),
                    Replay.this.getSelf());
            Replay.this.time += Replay.this.frequency;
        }
    }

}
