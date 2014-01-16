package org.smartcampus.simulation.framework.simulator;

import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;
import org.smartcampus.simulation.framework.messages.SendRequest;
import org.smartcampus.simulation.framework.messages.StartSensorSimulation;

import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.util.concurrent.TimeUnit;

/**
 * Created by foerster on 14/01/14.
 */
public abstract class Sensor<T, S, R> extends UntypedActor {

    protected int time ;
    private int interval;
    private Cancellable tick ;
    protected LoggingAdapter log;
    private Law<T, S> law;
    
    public Sensor() {
    	this.log = Logging.getLogger(getContext().system(), this);	
    }
      
    @SuppressWarnings("unchecked")
	@Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof StartSensorSimulation){
            StartSensorSimulation message = (StartSensorSimulation) o;
            this.time = message.getBegin();
            this.interval = message.getInterval();
            this.law = (Law<T, S>) message.getLaw();
            
            tick  = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(interval, TimeUnit.SECONDS),
                    getSelf(), new SendRequest(), getContext().dispatcher(), null);
        }
        else if(o instanceof SendRequest){
        	S res = law.evaluate(computeValue());
        	this.log.debug("["+time+","+transformResponse(res)+"]");
            time += interval;
        }
    }
    
    public abstract T computeValue();

    protected abstract R transformResponse(S res);

    @Override
    public void postStop(){
        tick.cancel();
    }
}
