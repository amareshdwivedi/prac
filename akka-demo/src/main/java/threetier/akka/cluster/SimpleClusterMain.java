package threetier.akka.cluster;

public class SimpleClusterMain {
	public static void main(String[] args) {
		akka.Main.main(new String[] { SimpleClusterListener.class.getName() });
	}
}
