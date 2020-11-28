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

	// logger will be added later
	
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

		// Adds instance object to instances array list
		protected void addAssignment(Assignment assingment) {
			this.assignmentList.add(assingment);
		}

	public int getDatasetID() {
		return datasetID;
	}

	public String getDatasetName() {
		return datasetName;
	}

	public int getMaxLabelPerInstance() {
		return maxLabelPerInstance;
	}

	public ArrayList<Label> getClassLabels() {
		return classLabels;
	}

	public ArrayList<Instance> getInstances() {
		return instances;
	}

	public ArrayList<Assignment> getAssignmentList() {
		return assignmentList;
	}

}