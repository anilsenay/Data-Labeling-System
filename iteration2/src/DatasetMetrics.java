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

    // C-2 Class distribution based on final instance labels
    public ArrayList<String> distributionInstance() {
        ArrayList<String> result = new ArrayList<String>();
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Instance> instanceList = dataset.getInstances();
        ArrayList<Label> labelList = dataset.getClassLabels();

        // initialize result arraylist
        int size = labelList.size();
        for (int i = 0; i < size; i++) {
            result.add(labelList.get(i).getLabelName());
            result.add("0");
        }

        // count the number of final labels
        size = instanceList.size();
        for (int i = 0; i < size; i++) {
            String finalLabel = instanceList.get(i).getfinalLabel().getLabelName();
            int temp = Integer.parseInt(result.get(result.indexOf(finalLabel) + 1)) + 1;
            result.set(result.indexOf(finalLabel) + 1, String.valueOf(temp));
        }

        int instanceListSize = instanceList.size();
        int resultSize = result.size();
        for (int i = 0; i < resultSize; i++) {
            if (i % 2 == 0)
                continue;

            float rate = (float) (1.0 * Integer.parseInt(result.get(i)) / instanceListSize);
            result.set(i, String.valueOf(rate));
        }

        return result;
    }

    // C-3 List number of unique instances for each class label ()
    public int numOfUniqueInstance(Label label) {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Instance> instanceList = dataset.getInstances();
        int num = 0;

        int size = instanceList.size();
        for (int i = 0; i < size; i++) {
            if (label.getLabelID() == instanceList.get(i).getfinalLabel().getLabelID()) {
                num++;
            }
        }

        return num;
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

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

}