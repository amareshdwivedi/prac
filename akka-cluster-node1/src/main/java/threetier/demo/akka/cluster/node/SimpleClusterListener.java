package threetier.demo.akka.cluster.node;

import scala.concurrent.Await;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import threetier.demo.bean.Employee;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class SimpleClusterListener extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	Cluster cluster = Cluster.get(getContext().system());

	final ExecutionContext ec = getContext().system().dispatcher();

	ActorRef mediator = DistributedPubSubExtension.get(getContext().system()).mediator();
	Timeout timeout = new Timeout(Duration.create(10, "seconds"));

	@Override
	public void preStart() throws Exception {

		cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(), MemberEvent.class, UnreachableMember.class);
	}

	@Override
	public void postStop() throws Exception {

		cluster.unsubscribe(getSelf());
	}

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MemberUp) {
			MemberUp mUp = (MemberUp) message;
			log.info("Member is Up: {}", mUp.member());
			System.out.println("I AM A LISTENER....GETTING UP MESSAGE");

		} else if (message instanceof UnreachableMember) {
			UnreachableMember mUnreachable = (UnreachableMember) message;
			log.info("Member detected as unreachable: {}", mUnreachable.member());

		} else if (message instanceof MemberRemoved) {
			MemberRemoved mRemoved = (MemberRemoved) message;
			log.info("Member is Removed: {}", mRemoved.member());

		} else if (message.equals("Hi from CLIENT")) {

			System.out.println("GETTING UP MESSAGE FROM CLIENT => " + message);
			Future<Object> future = Patterns.ask(mediator, new DistributedPubSubMediator.Send("/user/Killer", message, true), timeout);
			// String msg = (String) Await.result(future, timeout.duration());
			// getSender().tell("Got message from Killer actor == " + msg,
			// getSelf());

			Patterns.pipe(future, ec).to(getSender());

		} else if (message instanceof Employee) {
			Employee employee = (Employee) message;
			System.out.println("GETTING UP MESSAGE FROM CLIENT => " + employee.getName());
			Future<Object> future = Patterns.ask(mediator, new DistributedPubSubMediator.Send("/user/Killer", employee.getName(), true), timeout);

			Patterns.pipe(future, ec).to(getSender());
		}

		else {
			unhandled(message);
		}

	}

}
