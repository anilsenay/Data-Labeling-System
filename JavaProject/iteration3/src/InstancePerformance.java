import java.util.ArrayList;
import java.util.HashMap;

// Caller class for UserMetric class.
public class InstancePerformance {

    private Instance instance;

    public InstancePerformance(Instance instance) {
        this.instance = instance;
        ReportingMechanism.getInstance().getInstanceReportMechanism().addInstancePerformance(this);
    }

    // B-1 -> Total number of label assignments (call).
    public int getNumberOfLabelAssignment(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfLabelAssignment(this.instance, assignmentList);
    }

    // B-2 -> Number of unique label assignments (call).
    public int getNumberOfUniqueLabelAssignment(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfUniqueLabelAssignment(this.instance, assignmentList);
    }

    // B-3 -> Number of unique users (call).
    public int getNumberOfUniqueUsers(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfUniqueUsers(this.instance, assignmentList);
    }

    // B-4 -> Most frequent class label and percentage (call).
    public HashMap<String, Double> getMostFreqLabelAndPerc(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().mostFreqLabelAndPerc(this.instance, assignmentList);
    }

    // B-5 -> List class labels and percentages (call).
    public HashMap<String, Double> getListClassLabels(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().listClassLabels(this.instance, assignmentList);
    }

    // B-6 -> Entropy (call).
    public double getEntropy(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().entropy(this.instance, assignmentList);
    }

    public Instance getInstance() {
        return this.instance;
    }
}