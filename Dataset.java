import java.util.ArrayList;
import java.util.Date;

public class Dataset {
	// The variables datasetID, datasetName, maxLabelPerInstance are created.
	private int datasetID;
	private String datasetName;
	private int maxLabelPerInstance;

	// ClassLabels, instances, assignmentList Lists are created.
	private ArrayList<Label> classLabels = new ArrayList<Label>();
	private ArrayList<Instance> instances = new ArrayList<Instance>();
	private ArrayList<Assignment> assignmentList = new ArrayList<Assignment>();

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

		Logger.getInstance().print(new Date(),
				"[Dataset] INFO dataset: created \"" + datasetName + "\" with id: " + datasetID);
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
		return this.datasetID;
	}

	public String getDatasetName() {
		return this.datasetName;
	}

	public int getMaxLabelPerInstance() {
		return this.maxLabelPerInstance;
	}

	public ArrayList<Label> getClassLabels() {
		return this.classLabels;
	}

	public ArrayList<Instance> getInstances() {
		return this.instances;
	}

	public ArrayList<Assignment> getAssignmentList() {
		return this.assignmentList;
	}
}