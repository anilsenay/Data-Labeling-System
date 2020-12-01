import java.util.Date;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

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
		return this.instance;
	}

	public User getUser() {
		return this.user;
	}

	public ArrayList<Label> getAssignedLabels() {
		return this.assignedLabels;
	}

	public Date getDateTime() {
		return this.dateTime;
	}

	public String getFormattedTime() {
		return new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss.SS").format(this.dateTime);
	}

	public void setInstance(Instance instance) {
		this.instance = instance;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setAssignedLabels(ArrayList<Label> assignedLabels) {
		this.assignedLabels = assignedLabels;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}
