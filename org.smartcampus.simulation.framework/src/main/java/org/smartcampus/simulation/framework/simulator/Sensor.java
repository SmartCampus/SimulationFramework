package org.smartcampus.simulation.framework.simulator;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.smartcampus.simulation.framework.messages.UpdateSensorSimulation;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class Sensor<S, R> extends UntypedActor {

    private int time ;
    private S value;
    protected LoggingAdapter log;
    protected R lastValue;
    
    public Sensor() {
    	this.log = Logging.getLogger(getContext().system(), this);	
    	lastValue=null;
    }
      
	@Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof UpdateSensorSimulation){
            UpdateSensorSimulation message = (UpdateSensorSimulation) o;
            this.time = message.getBegin();
            this.value= (S) message.getValue();
            
        	R res = this.transformResponse(this.value);
        	//saves the value in case it is needed for next calculation
        	lastValue = res;
        	this.log.debug("["+time+","+(res)+"]");
        }
    }
    
    protected abstract R transformResponse(S res);

}
