package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

import org.smartcampus.simulation.framework.messages.ReturnMessage;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class Sensor<S, R> extends UntypedActor {

    private int time ;
    protected LoggingAdapter log;
    private S value;
    
    public Sensor() {
    	this.log = Logging.getLogger(getContext().system(), this);	
    }
      
	@Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof UpdateSensorSimulation){
            UpdateSensorSimulation<S> message = (UpdateSensorSimulation<S>) o;
            this.time = message.getBegin();
            this.value = message.getValue();
            
        	R res = this.transformResponse(this.value);
        	this.log.debug("["+time+","+(res)+"]");
        	
        	this.getSender().tell(new ReturnMessage<R>(res), this.getSelf());
        }
    }
    
    protected abstract R transformResponse(S res);

}
