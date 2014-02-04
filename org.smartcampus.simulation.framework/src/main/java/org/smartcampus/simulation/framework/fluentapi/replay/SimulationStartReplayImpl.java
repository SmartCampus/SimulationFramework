package org.smartcampus.simulation.framework.fluentapi.replay;

import java.sql.Timestamp;
import org.smartcampus.simulation.framework.fluentapi.SimulatorWrapper;
import akka.actor.ActorRef;

/**
 * Created by foerster on 22/01/14.
 */
public class SimulationStartReplayImpl extends SimulatorWrapper implements
        SimulationStartReplay {

    public SimulationStartReplayImpl(final ActorRef controllerRef) {
        super(controllerRef);
    }

    @Override
    public EndReplay start(final String date) {
        return new EndReplayImpl(this.controllerRef, Timestamp.valueOf(date).getTime());
    }

    @Override
    public EndReplay start(final long date) {
        return new EndReplayImpl(this.controllerRef, date);

    }
}
