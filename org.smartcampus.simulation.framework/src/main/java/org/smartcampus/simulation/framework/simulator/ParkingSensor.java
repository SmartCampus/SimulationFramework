package org.smartcampus.simulation.framework.simulator;

import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import scala.concurrent.duration.Duration;
import org.smartcampus.simulation.framework.messages.SendRequest;
import org.smartcampus.simulation.framework.messages.StartParkingSimulation;

import java.lang.Exception;import java.lang.Math;import java.lang.Object;import java.lang.Override;import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingSensor extends UntypedActor {

    private int time ;
    private int interval;
    private float value ;
    private Cancellable tick ;
    private LoggingAdapter log;
    
    public ParkingSensor() {
    	this.log = Logging.getLogger(getContext().system(), this);	
    }

    /**
     * @param time
     * @return percentage of occupied parking places in regards of the hour of the day
     */
    private double calculate(long time) {
		Calendar c = new GregorianCalendar();
		c.setTimeInMillis(time);
		double t = c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE)/60;
		if(t<6.5 || t> 18.5) return 0;
		double res=0.01208028907 * Math.pow(t, 6) - 0.8830270156 * Math.pow(t, 5)
				+ 26.18040012 * Math.pow(t, 4) - 401.9522656 * Math.pow(t, 3)
				+ 3359.404392 * Math.pow(t, 2) - 14430.25924 * t + 24839.21865;
		return res>0?res:0;
	}
    
    private boolean isOccupied(double res) {
    	Random rand = new Random();
		return rand.nextInt(100) < res;
	}
    
    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof StartParkingSimulation){
            StartParkingSimulation message = (StartParkingSimulation) o;
            this.time = message.getBegin();
            this.interval = message.getInterval();
            this.value = message.getValue();

            tick  = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(interval, TimeUnit.SECONDS),
                    getSelf(), new SendRequest(), getContext().dispatcher(), null);
        }
        else if(o instanceof SendRequest){
        	double res = calculate(time*3600000);
        	this.log.debug("["+time+","+isOccupied(res)+"]");
            time += interval;
        }
    }

    @Override
    public void postStop(){
        tick.cancel();
    }
}
