import java.util.ArrayList;
import java.util.HashMap;

public class InstancePerformance {

    private Instance instance;

    public InstancePerformance(Instance instance) {
        this.instance = instance;
        ReportingMechanism.getInstance().getInstanceReportMechanism().addInstancePerformance(this);
    }

    // B-1
    public int getNumberOfLabelAssignment(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfLabelAssignment(this.instance, assignmentList);
    }

    // B-2
    public int getNumberOfUniqueLabelAssignment(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfUniqueLabelAssignment(this.instance, assignmentList);
    }

    // B-3
    public int getNumberOfUniqueUsers(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().numberOfUniqueUsers(this.instance, assignmentList);
    }

    // B-4
    public HashMap<String, Double> getMostFreqLabelAndPerc(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().mostFreqLabelAndPerc(this.instance, assignmentList);
    }

    // B-5
    public HashMap<String, Double> getListClassLabels(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().listClassLabels(this.instance, assignmentList);
    }

    // B-6
    public double getEntropy(ArrayList<Assignment> assignmentList) {
        return InstanceMetrics.getInstance().entropy(this.instance, assignmentList);
    }

    public Instance getInstance() {
        return this.instance;
    }
}