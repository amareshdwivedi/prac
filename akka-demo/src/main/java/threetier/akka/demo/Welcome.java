package threetier.akka.demo;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Welcome extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			log.info("Recieved message - {}", message);
			getSender().tell("welcome", getSelf());
		} else {
			unhandled(message);
		}

	}

}
