import java.util.Date;
import java.util.ArrayList;

public class Assignment {
	// Instance and User objects are created
	Instance instance = new Instance();
	User user;
	
	// assignedLabels list is created.
	ArrayList<Label> assignedLabels = new ArrayList<Label>();
	Date dateTime;

	// no arg constructor
	public Assignment() {
		super();
	}

	// Assignment object is defined with arguments.
	public Assignment(Instance instance, User user, ArrayList<Label> assignedLabels) {
		super();
		this.instance = instance;
		this.user = user;
		this.assignedLabels = assignedLabels;
		this.dateTime = new Date();
	}
	
	// Adds Label object to assignedLabels array list
	protected void addLabel(Label label) {
		this.assignedLabels.add(label);
	}
	
	public Instance getInstance() {
		return instance;
	}

	public User getUser() {
		return user;
	}

	public ArrayList<Label> getAssignedLabel() {
		return assignedLabels;
	}

	public Date getDateTime() {
		return dateTime;
	}

}
