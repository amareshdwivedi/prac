package threetier.akka.cluster.node;

import java.util.concurrent.Callable;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.UntypedActor;
import akka.pattern.Patterns;
import static akka.dispatch.Futures.future;

public class KillerActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		final ExecutionContext ec = getContext().system().dispatcher();
		if (message.equals("Hi from CLIENT")) {
			System.out.println("Got Message : " + message);
			Future<String> f = future(new Callable<String>() {
				public String call() {
					return "\nMessage from killer actor piped to Listener\n";
				}
			}, ec);
			Patterns.pipe(f, ec).to(getSender());

		} else if (message.equals("kill him")) {
			System.out.println("Got Message : " + message);
			getSender().tell("I killed him...", getSelf());
		} else if (message instanceof String) {
			System.out.println("Got Message : " + message);
			getSender().tell(message.toString().toUpperCase(), getSelf());
		} else {
			unhandled(message);
		}
	}
}
