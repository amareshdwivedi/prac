package threetier.demo.akka.cluster.node;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class KillerActorMain {
	public static void main(String[] args) {

		String configFile = "application";

		Config config = ConfigFactory.load(configFile);
		System.out.println("" + config);
		String systemName = config.getString("akka.systemName");
		ActorSystem system = ActorSystem.create(systemName, config);
		ActorRef actorRef = system.actorOf(Props.create(KillerActor.class), "Killer");

		ActorRef mediator = DistributedPubSubExtension.get(system).mediator();
		mediator.tell(new DistributedPubSubMediator.Put(actorRef), actorRef);

		//ClusterReceptionistExtension.get(system).registerService(actorRef);
	}
}
