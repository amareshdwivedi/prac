package threetier.akka.demo;

import static akka.pattern.Patterns.ask;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.util.Timeout;

public class AskActor1 extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	@Override
	public void onReceive(Object message) throws Exception {
		
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		
		if (message == "ask1") {			
			log.info("Asking to actor 2 - {} : ", message);
			
			ActorRef askActor2 = getContext().actorOf(Props.create(AskActor2.class), "askActor2");
			
			Future<Object> future = ask(askActor2, "ritesh", timeout);
			Integer result = (Integer) Await.result(future, timeout.duration());
			
			getSender().tell(result, getSelf());
			
		} else {

		}
	}
}
