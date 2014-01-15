package smart.campus.simulation.simulator;

import java.util.concurrent.TimeUnit;

import scala.concurrent.duration.Duration;
import smart.campus.simulation.messages.CreateParking;
import smart.campus.simulation.messages.InitParking;
import smart.campus.simulation.messages.StartSimulation;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimulationController extends UntypedActor{

	private LoggingAdapter log;
	
	public SimulationController() {
		this.log = Logging.getLogger(getContext().system(), this);
	}
	
	@Override
	public void onReceive(Object arg0) throws Exception {
		if (arg0 instanceof CreateParking){
			CreateParking tmp = (CreateParking) arg0;
			this.getContext().actorOf(Props.create(ParkingLot.class, tmp.getNbSensors()), tmp.getName());
			
			// TODO A mettre dans un systeme de log
			this.log.debug("Je cree un Parking");
		}
		else if (arg0 instanceof InitParking){
			InitParking tmp = (InitParking) arg0;
			ActorRef actorTmp = this.getContext().getChild(tmp.getName());
			actorTmp.tell(tmp, this.getSelf());
			
			// TODO A mettre dans un systeme de log
			this.log.debug("J'initialise un parking");
		}
		else if (arg0 instanceof StartSimulation){
			StartSimulation tmp = (StartSimulation) arg0;
			for(ActorRef a : this.getContext().getChildren()){
				a.tell(tmp, this.getSelf());
			}
			
			// TODO A mettre dans un systeme de log
			this.log.debug("Je lance la simulation");
			
			getContext().system().scheduler().scheduleOnce(Duration.create(tmp.getDuration(), TimeUnit.SECONDS),
                    getSelf(), PoisonPill.getInstance(), getContext().dispatcher(), null);
		}
		else{
			
			// TODO A mettre dans un systeme de log
			this.log.debug("J'ai recu un message que je ne comprends pas");
		}
	}
	
	@Override
	public void postStop() throws Exception {
		this.log.debug("Je me suicide");
	}

}
