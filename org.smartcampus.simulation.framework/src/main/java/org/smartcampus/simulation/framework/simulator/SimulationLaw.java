package org.smartcampus.simulation.framework.simulator;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.*;

import org.smartcampus.simulation.framework.messages.InitParking;
import org.smartcampus.simulation.framework.messages.StartParkingSimulation;
import org.smartcampus.simulation.framework.messages.StartSimulation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foerster on 14/01/14.
 */
public class SimulationLaw extends UntypedActor {

    private Router router;
    private float value;

    public SimulationLaw(int numberOfSensors){
        List<Routee> routees = new ArrayList<Routee>();
        for(int i = 0; i < numberOfSensors ; i++){
            ActorRef r = getContext().actorOf(Props.create(Sensor.class),getSelf().path().name()+"-"+i);
            getContext().watch(r);
            routees.add(new ActorRefRoutee(r));
        }
        router = new Router(new BroadcastRoutingLogic(), routees);
    }

    @Override
    public void onReceive(Object o) throws Exception {
        if(o instanceof InitParking){
            value = ((InitParking)o).getInitVal();
        }
        if(o instanceof StartSimulation){
            StartSimulation message = (StartSimulation)o;
            router.route(new StartParkingSimulation(message.getBegin(),message.getDuration(),message.getInterval(),value),getSelf());
        }
    }
}
