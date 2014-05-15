package threetier.akka.cluster;

import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimpleClusterListener extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);

	
	Cluster cluster = Cluster.get(getContext().system());

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

		} else if (message instanceof MemberEvent) {
			// ignore

		} else {
			unhandled(message);
		}

	}

}
