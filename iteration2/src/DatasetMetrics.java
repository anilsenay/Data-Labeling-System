import java.util.ArrayList;

public class DatasetMetrics {
    private Dataset dataset;

    public DatasetMetrics(Dataset dataset) {
        this.dataset = dataset;
    }

    // C-1 Completeness percentage
    public float completenessPercentage() {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        int countInstance = dataset.getInstances().size();
        ArrayList<Integer> labeledInstances = new ArrayList<Integer>();

        int countAssignment = assignmentList.size();
        for (int i = 0; i < countAssignment; i++) {
            int id = assignmentList.get(i).getInstance().getInstanceID();
            if (!labeledInstances.contains(id)) {
                labeledInstances.add(id);
            }
        }

        float perc = (float) ((1.0 * labeledInstances.size()) / countInstance);

        return perc;
    }

    // C-4 Number of users assigned to this dataset
    public int numberOfUserAssigned() {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        int count = 0;
        ArrayList<Integer> userIDs = new ArrayList<Integer>();

        int countAssignment = dataset.getAssignmentList().size();
        for (int i = 0; i < countAssignment; i++) {
            int userID = assignmentList.get(i).getUser().getUserID();
            if (!userIDs.contains(userID)) {
                userIDs.add(userID);
            }
        }

        count = userIDs.size();

        return count;
    }

    public Dataset getDataset() {
        return this.dataset;
    }

}