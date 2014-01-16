package org.smartcampus.simulation.framework.simulator;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.ActorRefRoutee;
import akka.routing.BroadcastRoutingLogic;
import akka.routing.Routee;
import akka.routing.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foerster on 16/01/14.
 */
public class SimulationLaw extends UntypedActor{

    private Law law ;
    private Router router;

    public SimulationLaw(int numberOfSensors){
        List<Routee> routees = new ArrayList<Routee>();
        for(int i = 0; i < numberOfSensors ; i++){
            ActorRef r = getContext().actorOf(Props.create(ParkingSensor.class),getSelf().path().name()+"-"+i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new BroadcastRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object o) throws Exception {

    }
}
