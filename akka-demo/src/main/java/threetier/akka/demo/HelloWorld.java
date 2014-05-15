package threetier.akka.demo;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class HelloWorld extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void preStart() throws Exception {
		 
		final ActorRef welcome = getContext().actorOf(Props.create(Welcome.class), "welcome");
		welcome.tell("Hello Welcome", getSelf());
	}

	@Override
	public void onReceive(Object message) throws Exception {

		if (message == "welcome") {

			log.info("Recieved String message : {} ", message);
			getContext().stop(getSelf());

		} else {
			unhandled(message);
		}
	}

}
