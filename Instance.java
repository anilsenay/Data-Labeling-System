
public class Instance {
	
	// The variables content, instanceID are created.
	String content;
	int instanceID;

	// no arg constructor
	public Instance() {
		super();
	}
	
	// Instance object is defined with arguments.
	public Instance(String content, int instanceID) {
		super();
		this.content = content;
		this.instanceID = instanceID;
	}

	public String getContent() {
		return content;
	}
	public int getInstanceID() {
		return instanceID;
	}
}
