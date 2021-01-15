import java.util.ArrayList;

public class DatasetMetrics {

    private static DatasetMetrics datasetMetrics;

    private DatasetMetrics() {

    }
    
    // getInstance call for usage of object variable with singleton pattern 
    public static synchronized DatasetMetrics getInstance() {
        if (datasetMetrics == null) {
            datasetMetrics = new DatasetMetrics();
        }
        return datasetMetrics;
    }

    // C-1 Completeness percentage
    public float completenessPercentage() {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        int countInstance = dataset.getInstances().size();
        ArrayList<Integer> labeledInstances = new ArrayList<Integer>();

        int countAssignment = assignmentList.size();
        // gett labeled instances
        for (int i = 0; i < countAssignment; i++) {
            int id = assignmentList.get(i).getInstance().getInstanceID();
            if (!labeledInstances.contains(id)) {
                labeledInstances.add(id);
            }
        }
        // percentage calculation labeled instances / every instance current dataset has
        float perc = (float) ((1.0 * labeledInstances.size()) / countInstance);

        return perc * 100;
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
            if (instanceList.get(i).getfinalLabel() != null) {
                String finalLabel = instanceList.get(i).getfinalLabel().getLabelName();
                int temp = Integer.parseInt(result.get(result.indexOf(finalLabel) + 1)) + 1;
                result.set(result.indexOf(finalLabel) + 1, String.valueOf(temp));
            }
        }

        int instanceListSize = instanceList.size();
        int resultSize = result.size();
        for (int i = 0; i < resultSize; i++) {
            if (i % 2 == 0)
                continue;

            float rate = (float) ((1.0 * Integer.parseInt(result.get(i)) / instanceListSize));

            result.set(i, String.valueOf((double) ((int) (rate * 10000)) / 100.0));

        }

        return result;
    }

    // C-3 List number of unique instances for each class label
    public int numOfUniqueInstance(Label label) {
    	// get current dataset from previous state
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        ArrayList<Integer> uniqueInstanceIDs = new ArrayList<Integer>();

        // addition of unique instance ids  to uniqueInstanceIDs arraylist
        for (int i = 0; i < assignmentList.size(); i++) {
            for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                if (label.getLabelID() == assignmentList.get(i).getAssignedLabels().get(j).getLabelID()) {
                	// if current instance is not added before
                    if (!uniqueInstanceIDs.contains(assignmentList.get(i).getAssignedLabels().get(j).getLabelID())) {
                        uniqueInstanceIDs.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelID());
                    }
                }
            }
        }

        return uniqueInstanceIDs.size();
    }

    // C-4 Number of users assigned to this dataset
    public int numberOfUserAssigned() {
    	// get current dataset from previous state
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        int count = 0;
        ArrayList<Integer> userIDs = new ArrayList<Integer>();

        int countAssignment = dataset.getAssignmentList().size();
        // count number of users assigned to current dataset
        for (int i = 0; i < countAssignment; i++) {
            int userID = assignmentList.get(i).getUser().getUserID();
            if (!userIDs.contains(userID)) {
                userIDs.add(userID);
            }
        }

        count = userIDs.size();

        return count;
    }
}