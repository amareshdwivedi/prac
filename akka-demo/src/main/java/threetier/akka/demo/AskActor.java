package threetier.akka.demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class AskActor extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void preStart() throws Exception {

		log.info(" Prstart of AskActor");

		ActorRef askActor1 = getContext().actorOf(Props.create(AskActor1.class), "askActor1");
		askActor1.tell("ask1", getSelf());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Integer) {

			log.info("Terminating AskMain");
			log.info("Lenght of strung was - {}", message);

			getContext().stop(getSelf());

		} else {

		}

	}

}
