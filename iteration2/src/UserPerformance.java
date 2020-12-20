import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class UserPerformance {

    private User user;
    private ArrayList<Integer> assignedDatasets = new ArrayList<Integer>();

    public UserPerformance(User user) {
        this.user = user;
        ReportingMechanism.getInstance().getUserReportMechanism().addUserPerformance(this);
    }

    // A-1 Number of datasets assigned
    public ArrayList<Integer> getDatasetAssign(Iterator<JsonElement> datasetIterator) {

        return UserMetrics.getInstance().datasetAssign(this.user, datasetIterator);
    }

    // A-2 List of all datasets with their completeness percentage
    public float getDatasetCompletenessPer(JsonObject report) {

        return UserMetrics.getInstance().datasetCompletenessPer(this.user, report);
    }

    // A-2.1
    public float getDatasetCompletenessPerCurrent() {
        return UserMetrics.getInstance().datasetCompletenessPerCurrent(this.user);
    }

    // A-3
    public int getNumberOfInstancesLabeled() {
        return UserMetrics.getInstance().numberOfInstancesLabeled();
    }

    // A-4
    public int getUniqueNumOfInstancesLabeled(ArrayList<Assignment> assignmentList, Assignment newAssignment) {
        return UserMetrics.getInstance().uniqueNumOfInstancesLabeled(this.user, assignmentList, newAssignment);
    }

    // A-5
    public double getConsistencyPercentagesForUser(ArrayList<Assignment> assignmentList, double prevPercentage) {
        return UserMetrics.getInstance().consistencyPercentagesForUser(this.user, assignmentList, prevPercentage);
    }

    // A-6
    public double getAverageTimeSpent(ArrayList<Assignment> assignmentList) {
        return UserMetrics.getInstance().averageTimeSpent(this.user, assignmentList);
    }

    // A-7
    public double getStandartDev(ArrayList<Assignment> assignmentList) {
        return UserMetrics.getInstance().standartDev(this.user, assignmentList);
    }

    public User getUser() {
        return this.user;
    }

    public ArrayList<Integer> getAssignedDatasets() {
        return this.assignedDatasets;
    }

    public void setAssignedDatasets(ArrayList<Integer> assignedDatasets) {
        this.assignedDatasets = assignedDatasets;
    }

    public void updateAssignedDatasets(Iterator<JsonElement> datasetIterator) {
        this.assignedDatasets = UserMetrics.getInstance().datasetAssign(this.user, datasetIterator);
    }
}
