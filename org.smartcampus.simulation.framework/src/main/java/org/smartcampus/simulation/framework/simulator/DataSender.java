package org.smartcampus.simulation.framework.simulator;

import org.smartcampus.simulation.framework.messages.SendValue;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Created by foerster on 19/01/14.
 */
public class DataSender extends UntypedActor {

    private LoggingAdapter log;

    public DataSender() {
        this.log = Logging.getLogger(this.getContext().system(), this);
    }

    @Override
    public void onReceive(final Object o) throws Exception {
        if (o instanceof SendValue) {
            SendValue sendValue = (SendValue) o;
            this.log.debug("{ n : " + sendValue.getName() + ", v :"
                    + sendValue.getValue() + ", t:" + sendValue.getTime() + "}");
        }
    }
}
