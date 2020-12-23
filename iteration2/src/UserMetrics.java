import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class UserMetrics {

    private static UserMetrics userMetrics;

    private UserMetrics() {
    }

    public static synchronized UserMetrics getInstance() {
        if (userMetrics == null)
            userMetrics = new UserMetrics();

        return userMetrics;
    }

    // A-1 Number of datasets assigned
    public ArrayList<Integer> datasetAssign(User user, Iterator<JsonElement> datasetIterator) {
        ArrayList<Integer> datasetIds = new ArrayList<Integer>();
        while (datasetIterator.hasNext()) {
            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            JsonArray users = (JsonArray) datasetObj.get("users");
            Iterator<JsonElement> usersIterator = users.iterator();

            while (usersIterator.hasNext()) {
                int userID = usersIterator.next().getAsInt();
                if (userID == user.getUserID())
                    datasetIds.add((datasetObj.get("dataset_id").getAsInt()));
            }
        }

        return datasetIds;
    }

    // A-2 List of all datasets with their completeness percentage
    public float datasetCompletenessPer(User user) {
        Dataset currentDataset = ReportingMechanism.getInstance().getDataset();
        ArrayList<Assignment> assignments = currentDataset.getAssignmentList();
        ArrayList<Instance> instances = currentDataset.getInstances();
        ArrayList<Integer> assignedInstanceID = new ArrayList<Integer>();
        int instanceCount = instances.size();

        int size = assignments.size();
        for (int i = 0; i < size; i++) {
            if (assignments.get(i).getUser().getUserID() != user.getUserID())
                continue;

            int instanceID = assignments.get(i).getInstance().getInstanceID();

            if (!assignedInstanceID.contains(instanceID))
                assignedInstanceID.add(instanceID);
        }

        float compPerCurrent = (float) ((1.0 * assignedInstanceID.size()) / instanceCount) * 100;

        return ((float) (((int) (compPerCurrent * 100)) / 100.0));
    }

    // A-3
    public int numberOfInstancesLabeled() {
        return 1;
    }

    // A-4
    public int uniqueNumOfInstancesLabeled(User user, ArrayList<Assignment> assignmentList, Assignment newAssignment) {

        for (int i = 0; i < assignmentList.size() - 1; i++) {
            if (assignmentList.get(i).getUser() != null
                    && user.getUserID() == assignmentList.get(i).getUser().getUserID()) {
                if (newAssignment.getInstance().getInstanceID() == assignmentList.get(i).getInstance()
                        .getInstanceID()) {
                    return 0;
                }
            }
        }
        return 1;
    }

    // A-5 Consistency percentage
    public double consistencyPercentagesForUser(User user, ArrayList<Dataset> datasetList) {
        int countTotal = 0;
        double result = 0.0;

        for (int m = 0; m < datasetList.size(); m++) {
            double total = 0;
            ArrayList<Assignment> assignmentList = datasetList.get(m).getAssignmentList();
            ArrayList<Assignment> thisUsersAssignments = new ArrayList<Assignment>();
            ArrayList<Integer> uniqueAssignedInstanceIDs = new ArrayList<Integer>();
            ArrayList<Integer> assignedLabelIDs = new ArrayList<Integer>();

            int size = assignmentList.size();
            for (int i = 0; i < size; i++) {
                if (assignmentList.get(i).getUser() != null
                        && user.getUserID() == assignmentList.get(i).getUser().getUserID()) {
                    thisUsersAssignments.add(assignmentList.get(i));
                }
            }

            size = thisUsersAssignments.size();
            for (int i = 0; i < size; i++) {
                if (!uniqueAssignedInstanceIDs.contains(assignmentList.get(i).getInstance().getInstanceID()))
                    uniqueAssignedInstanceIDs.add(assignmentList.get(i).getInstance().getInstanceID());
            }

            for (int i = 0; i < uniqueAssignedInstanceIDs.size(); i++) {
                for (int j = 0; j < thisUsersAssignments.size(); j++) {
                    if (uniqueAssignedInstanceIDs.get(i) == thisUsersAssignments.get(j).getInstance().getInstanceID()) {
                        for (int j2 = 0; j2 < thisUsersAssignments.get(j).getAssignedLabels().size(); j2++)
                            assignedLabelIDs.add(thisUsersAssignments.get(j).getAssignedLabels().get(j2).getLabelID());
                    }
                }

                Integer[] assignedLabelIDsArray = assignedLabelIDs.toArray(new Integer[assignedLabelIDs.size()]);
                total += (1.0 * mostfrequent(assignedLabelIDsArray)) / (1.0 * uniqueAssignedInstanceIDs.size());
                assignedLabelIDs.clear();

            }
            countTotal += uniqueAssignedInstanceIDs.size();
            result += 1.0 * total;
        }

        result = (result * 1.0) / (countTotal * 1.0);
        result = ((int) (result * 10000)) / 100.0;
        return result;
    }

    // A-6
    public double averageTimeSpent(User user, ArrayList<Dataset> datasetList) {

        double sum = 0.0;
        ArrayList<Double> seconds = getUserAssignmentDurations(user, datasetList);

        for (double num : seconds)
            sum += num;

        return sum / seconds.size();
    }

    // A-7
    public double standartDev(User user, ArrayList<Dataset> datasetList) {

        double sum = 0.0, standardDeviation = 0.0;
        ArrayList<Double> seconds = getUserAssignmentDurations(user, datasetList);

        for (double num : seconds)
            sum += num;

        int length = seconds.size();
        double mean = sum / length;

        for (double num : seconds)
            standardDeviation += Math.pow(num - mean, 2);

        return Math.sqrt(standardDeviation / length);
    }

    private ArrayList<Double> getUserAssignmentDurations(User user, ArrayList<Dataset> datasetList) {
        ArrayList<Double> seconds = new ArrayList<Double>();

        for (int m = 0; m < datasetList.size(); m++) {
            ArrayList<Assignment> assignmentList = datasetList.get(m).getAssignmentList();
            for (int i = 0; i < assignmentList.size(); i++) {
                if (assignmentList.get(i).getUser() != null
                        && assignmentList.get(i).getUser().getUserID() == user.getUserID()) {
                    seconds.add(assignmentList.get(i).getAssingmentDuration() / 1000.0);
                }
            }
        }
        return seconds;
    }

    private int mostfrequent(Integer array[]) {
        // Insert all elements in hash
        Map<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

        for (int i = 0; i < array.length; i++) {
            int key = array[i];
            if (hashmap.containsKey(key)) {
                int frequency = hashmap.get(key);
                frequency++;
                hashmap.put(key, frequency);
            } else {
                hashmap.put(key, 1);
            }
        }

        // find max frequency
        int max_count = 0;

        for (Entry<Integer, Integer> val : hashmap.entrySet()) {
            if (max_count < val.getValue())
                max_count = val.getValue();
        }
        return max_count;
    }
}
