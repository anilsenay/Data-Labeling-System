import java.util.Date;

// RelevanceBot class is a kind of User which inherits from User class.
public class RelevanceBot extends User {

  // RelevanceBot object is defined with arguments.
  public RelevanceBot(String userName, int userID, String userType) {
    super(userName, userID, userType);
    // Print the created relevance bot to the log file.
    Logger.getInstance().print(new Date(), "[User] INFO user: created " + userName + " as " + userType);
  }

  public RelevanceBot(String userName, int userID, String userType, double consistencyCheckProbability) {
    super(userName, userID, userType, consistencyCheckProbability);
    // Print the created relevance bot to the log file.
    Logger.getInstance().print(new Date(), "[User] INFO user: created " + userName + " as " + userType);
  }

  // Performs userType specified assign process.
  public Assignment assign(Dataset dataset, Instance instance) {
    return RelevanceLabelingMechanism.getInstance().assign(dataset, instance, this);
  }

}
