import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonElement;

public class UserPerformance {

    private User user;
    private ArrayList<Integer> assignedDatasets = new ArrayList<Integer>();

    public UserPerformance(User user) {
        this.user = user;
        ReportingMechanism.getInstance().getUserReportMechanism().addUserPerformance(this);
    }

    // A-1 Call datasetAssign method and find number of datasets assigned
    public ArrayList<Integer> getDatasetAssign(Iterator<JsonElement> datasetIterator) {

        return UserMetrics.getInstance().datasetAssign(this.user, datasetIterator);
    }

    // A-2 call datasetCompletenessPer method and list of all datasets with their completeness percentage
    public float getDatasetCompletenessPer() {
        return UserMetrics.getInstance().datasetCompletenessPer(this.user);
    }

    // A-3  call numberOfInstancesLabeled method and find total number of instances labeled 
    public int getNumberOfInstancesLabeled() {
        return UserMetrics.getInstance().numberOfInstancesLabeled();
    }

    // A-4 call uniqueNumOfInstancesLabeled method and find total number of unique instances labeled 
    public int getUniqueNumOfInstancesLabeled(ArrayList<Assignment> assignmentList, Assignment newAssignment) {
        return UserMetrics.getInstance().uniqueNumOfInstancesLabeled(this.user, assignmentList, newAssignment);
    }

    // A-5 call consistencyPercentagesForUser method and find consistency percentage
    public double getConsistencyPercentagesForUser(ArrayList<Dataset> datasetList) {
        return UserMetrics.getInstance().consistencyPercentagesForUser(this.user, datasetList);
    }

    // A-6 call averageTimeSpent method and find average time spent in labeling an instance in seconds
    public double getAverageTimeSpent(ArrayList<Dataset> datasets) {
        return UserMetrics.getInstance().averageTimeSpent(this.user, datasets);
    }

    // A-7 call method and find standard deviation of time spent in labeling an instance in seconds
    public double getStandartDev(ArrayList<Dataset> datasetList) {
        return UserMetrics.getInstance().standartDev(this.user, datasetList);
    }

    public User getUser() {
        return this.user;
    }
    // get assignedDatasets arraylist for a user
    public ArrayList<Integer> getAssignedDatasets() {
        return this.assignedDatasets;
    }
    // set assignedDatasets arraylist for a user
    public void setAssignedDatasets(ArrayList<Integer> assignedDatasets) {
        this.assignedDatasets = assignedDatasets;
    }
    // update assignedDatasets arraylist for a user
    public void updateAssignedDatasets(Iterator<JsonElement> datasetIterator) {
        this.assignedDatasets = UserMetrics.getInstance().datasetAssign(this.user, datasetIterator);
    }
}
