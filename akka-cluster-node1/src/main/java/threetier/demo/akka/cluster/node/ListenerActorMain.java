package threetier.demo.akka.cluster.node;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.contrib.pattern.ClusterReceptionistExtension;
import akka.contrib.pattern.DistributedPubSubExtension;
import akka.contrib.pattern.DistributedPubSubMediator;
import akka.pattern.Patterns;
import akka.util.Timeout;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ListenerActorMain {

	public static void main(String[] args) throws Exception {

		String configFile = "application";

		Timeout timeout = new Timeout(Duration.create(10, "seconds"));

		Config config = ConfigFactory.load(configFile);
		System.out.println("" + config);
		String systemName = config.getString("akka.systemName");
		ActorSystem system = ActorSystem.create(systemName, config);
		ActorRef actorRef = system.actorOf(Props.create(SimpleClusterListener.class), "master");

		ActorRef mediator = DistributedPubSubExtension.get(system).mediator();
		mediator.tell(new DistributedPubSubMediator.Put(actorRef), actorRef);

		Thread.sleep(30000);
		Future<Object> ask = Patterns.ask(mediator, new DistributedPubSubMediator.Send("/user/Killer", "kill him", true), timeout);
		
		String result = (String) Await.result(ask, timeout.duration());

		System.out.println("PUB SUB RESLUT : " + result);

		ClusterReceptionistExtension.get(system).registerService(actorRef);
	}
}
