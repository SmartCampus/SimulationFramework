package org.smartcampus.simulation.framework.fluentapi;

import java.sql.Timestamp;
import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationStartImpl extends SimulatorWrapper implements SimulationStart {

    public SimulationStartImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationDuration start(final String date) {
        return new SimulationDurationImpl(this.controllerRef, Timestamp.valueOf(date)
                .getTime());
    }

    @Override
    public SimulationDuration start(final long date) {
        return new SimulationDurationImpl(this.controllerRef, date);

    }
}
