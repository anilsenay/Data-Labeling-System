
public class Instance {
	// Dataset object is created
	Dataset dataset;
	
	// The variables content, instanceID are created.
	String content;
	int instanceID;

	// no arg constructor
	public Instance() {
		super();
	}
	
	// Instance object is defined with arguments.
	public Instance(Dataset dataset, String content, int instanceID) {
		super();
		this.dataset = dataset;
		this.content = content;
		this.instanceID = instanceID;
	}

	private Dataset getDataset() {
		return dataset;
	}

	private String getContent() {
		return content;
	}
	
}
