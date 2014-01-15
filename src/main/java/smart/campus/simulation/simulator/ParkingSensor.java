package smart.campus.simulation.simulator;

import akka.actor.Cancellable;
import akka.actor.UntypedActor;
import scala.concurrent.duration.Duration;
import smart.campus.simulation.messages.SendRequest;
import smart.campus.simulation.messages.StartParkingSimulation;

import java.util.concurrent.TimeUnit;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingSensor extends UntypedActor {

    private int time ;
    private int interval;
    private float value ;
    private Cancellable tick ;


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
        if(o instanceof SendRequest){
            System.out.println("["+getSelf().path().name()+","+time+","+value+"]");
            time += interval;
        }
    }
}
