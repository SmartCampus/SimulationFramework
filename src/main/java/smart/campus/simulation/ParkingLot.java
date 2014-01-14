package smart.campus.simulation;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foerster on 14/01/14.
 */
public class ParkingLot extends UntypedActor {

    Router router;

    public ParkingLot(int numberOfSensors){
        List<Routee> routees = new ArrayList<Routee>();
        for(int i = 0; i < numberOfSensors ; i++){
            ActorRef r = getContext().actorOf(Props.create(ParkingSensor.class),getSender().path().name()+"-"+i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new BroadcastRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object o) throws Exception {
    }
}
