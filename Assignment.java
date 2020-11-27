import java.sql.Date;
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
	public Assignment(Instance instance, User user, ArrayList<Label> assignedLabel, Date dateTime) {
		super();
		this.instance = instance;
		this.user = user;
		this.assignedLabels = assignedLabel;
		this.dateTime = dateTime;
	}
	
	// Adds Label object to assignedLabels array list
	protected void addLabel(Label label) {
		this.assignedLabels.add(label);
	}
	
	private Instance getInstance() {
		return instance;
	}

	private User getUser() {
		return user;
	}

	private ArrayList<Label> getAssignedLabel() {
		return assignedLabels;
	}

	private Date getDateTime() {
		return dateTime;
	}
	
}
