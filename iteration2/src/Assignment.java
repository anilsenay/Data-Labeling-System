import java.util.Date;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;

// Assignment class for assign given labels to the instances from.
public class Assignment {

	// Instance and User objects are created.
	private Instance instance = new Instance();
	private User user;

	// AssignedLabels list is created.
	private ArrayList<Label> assignedLabels = new ArrayList<Label>();
	private Date dateTime;

	// No arg constructor.
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

	public Assignment(Instance instance, User user, ArrayList<Label> assignedLabels, String date)
			throws ParseException {
		super();
		this.instance = instance;
		this.user = user;
		this.assignedLabels = assignedLabels;
		this.dateTime = new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss.SS").parse(date);
	}

	// Adds Label object to assignedLabels array list.
	protected void addLabel(Label label) {
		this.assignedLabels.add(label);
	}

	// Format the time for the assigned instance.
	public String getFormattedTime() {
		return new SimpleDateFormat("MM/dd/yyyy, HH:mm:ss.SS").format(this.dateTime);
	}

	// Getter and setter methods for Assignment class.
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
