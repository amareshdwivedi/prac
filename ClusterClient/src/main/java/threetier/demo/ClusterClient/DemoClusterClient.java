package threetier.demo.ClusterClient;
import java.util.HashSet;
import java.util.Set;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import threetier.demo.bean.Employee;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.contrib.pattern.ClusterClient;
import akka.pattern.Patterns;
import akka.util.Timeout;

public class DemoClusterClient {
	public static void main(String[] args) throws Exception {
		Timeout timeout = new Timeout(Duration.create(5, "seconds"));
		ActorSystem  system = ActorSystem.create("OTHERSYSTEM");

		Set<ActorSelection> initialContacts = new HashSet<ActorSelection>();
		
		initialContacts.add(system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2551/user/receptionist"));
		//initialContacts.add(system.actorSelection("akka.tcp://ClusterSystem@127.0.0.1:2552/user/receptionist"));
		
		ActorRef actor = system.actorOf(ClusterClient.props(initialContacts,timeout.duration(),timeout.duration()));
		
		
		/*Future<Object> ask = Patterns.ask(actor, new ClusterClient.Send("/user/master", "Hi from CLIENT", true), timeout);
		String result = (String) Await.result(ask, timeout.duration());*/
		
		Future<Object> ask = Patterns.ask(actor, new ClusterClient.Send("/user/master", new Employee("John"), true), timeout);
		String result = (String) Await.result(ask, timeout.duration());
		
		System.out.println(" result ====== " + result);
	}
}
