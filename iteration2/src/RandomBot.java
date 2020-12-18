import java.util.Date;

// RandomBot class is a kind of User which inherits from User class.
public class RandomBot extends User {

    // RandomBot object is defined with arguments.
    public RandomBot(String userName, int userID, String userType) {
        super(userName, userID, userType);
        // Print the created random bot to the log file.
        Logger.getInstance().print(new Date(), "[User] INFO user: created " + userName + " as " + userType);
    }

    public RandomBot(String userName, int userID, String userType, double consistencyCheckProbability) {
        super(userName, userID, userType, consistencyCheckProbability);
        // Print the created random bot to the log file.
        Logger.getInstance().print(new Date(), "[User] INFO user: created " + userName + " as " + userType);
    }

    // Performs userType specified assign process.
    public void assign(Dataset dataset, Instance instance) {
        RandomLabelingMechanism.getInstance().assign(dataset, instance, this);
    }

}
