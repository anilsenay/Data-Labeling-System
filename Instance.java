import java.util.Date;

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
		Logger.getInstance().print(new Date(),
				"[Instance] INFO instance: created \"" + content + "\" with id: " + instanceID);
	}

	public String getContent() {
		return this.content;
	}

	public int getInstanceID() {
		return this.instanceID;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setInstanceID(int instanceID) {
		this.instanceID = instanceID;
	}

}
