package org.smartcampus.simulation.framework.fluentapi.simulation;

import java.sql.Timestamp;
import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationStartImpl extends SimulatorWrapper implements SimulationStart {

    public SimulationStartImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public SimulationDuration startAt(final String date) {
        return new SimulationDurationImpl(this.controllerRef, Timestamp.valueOf(date)
                .getTime());
    }

    @Override
    public SimulationDuration startAt(final long date) {
        return new SimulationDurationImpl(this.controllerRef, date);

    }
}
