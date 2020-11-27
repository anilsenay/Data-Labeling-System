import java.util.ArrayList;

public class Dataset {
	// The variables datasetID, datasetName, maxLabelPerInstance are created.
	int datasetID; 
	String datasetName;
	int maxLabelPerInstance;
	
	// ClassLabels, instances, assignmentList Lists are created.
	ArrayList<Label> classLabels = new ArrayList<Label>();
	ArrayList<Instance> instances = new ArrayList<Instance>();
	ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();
	
	// Logger object is created
	Logger log;

	// no arg constructor
	public Dataset() {
		super();
	}

	// Dataset object is defined with arguments.
	public Dataset(int datasetID, String datasetName, int maxLabelPerInstance) {
		super();
		this.datasetID = datasetID;
		this.datasetName = datasetName;
		this.maxLabelPerInstance = maxLabelPerInstance;
	}

	// Adds instance object to instances array list
	protected void addInstance(Instance instance) {
		this.instances.add(instance);
	}
	
	// Adds label object to classLabels array list
	protected void addLabel(Label label) {
		this.classLabels.add(label);
	}

	private int getDatasetID() {
		return datasetID;
	}

	private String getDatasetName() {
		return datasetName;
	}

	private int getMaxLabelPerInstance() {
		return maxLabelPerInstance;
	}

	private ArrayList<Label> getClassLabels() {
		return classLabels;
	}

	private ArrayList<Instance> getInstances() {
		return instances;
	}

	private ArrayList<Assignment> getAssignmentList() {
		return assignmentList;
	}

}