package threetier.akka.demo;

import java.util.concurrent.Callable;

import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import akka.actor.UntypedActor;
import static akka.dispatch.Futures.future;

public class AskActor2 extends UntypedActor {

	ExecutionContext ec = getContext().dispatcher();

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof String) {
			
			final String msg = (String) message;
			
			Future<Integer> length = future(new Callable<Integer>() {

				public Integer call() throws Exception {

					return ((String) msg).length();
				}
			}, ec);

			akka.pattern.Patterns.pipe(length, ec).pipeTo(getSender(), getSelf());
			
		} else {
			unhandled(message);
		}

	}

}
