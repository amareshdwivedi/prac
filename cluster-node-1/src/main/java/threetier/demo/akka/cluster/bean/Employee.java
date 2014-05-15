package threetier.demo.akka.cluster.bean;

public class Employee {

	String name;

	public Employee(String string) {
		this.name = string;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
