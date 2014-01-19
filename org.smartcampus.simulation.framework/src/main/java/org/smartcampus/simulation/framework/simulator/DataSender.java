package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.smartcampus.simulation.framework.messages.SendValue;

/**
 * Created by foerster on 19/01/14.
 */
public class DataSender extends UntypedActor {

    private LoggingAdapter log ;

    public DataSender(){
        this.log = Logging.getLogger(getContext().system(),this);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof SendValue){
            SendValue sendValue = (SendValue) o;
            log.debug("{ n : " + sendValue.getName()+", v :" + sendValue.getValue()+", t:" + sendValue.getTime() + "}");
        }
    }
}
