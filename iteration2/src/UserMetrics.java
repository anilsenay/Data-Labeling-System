import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Map.Entry; // HEPSİ İÇİN KESİNLİKLE TEST LAZIMM.
import java.util.Iterator;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class UserMetrics {

    private static UserMetrics userMetrics;

    private UserMetrics() {

    }

    public static synchronized UserMetrics getInstance() {
        if (userMetrics == null) {
            userMetrics = new UserMetrics();
        }
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
                if (userID == user.getUserID()) {
                    int datasetID = (datasetObj.get("dataset_id").getAsInt());
                    datasetIds.add(datasetID);
                }

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
            if (!assignedInstanceID.contains(instanceID)) {
                assignedInstanceID.add(instanceID);
            }
        }

        float compPerCurrent = (float) ((1.0 * assignedInstanceID.size()) / instanceCount) * 100;
        compPerCurrent = (float) (((int) (compPerCurrent * 100)) / 100.0);

        return compPerCurrent;
    }

    // A-3
    public int numberOfInstancesLabeled() {
        return 1;
    }

    // A-4 TEST EDİLMELİ
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
                if (!uniqueAssignedInstanceIDs.contains(assignmentList.get(i).getInstance().getInstanceID())) {
                    uniqueAssignedInstanceIDs.add(assignmentList.get(i).getInstance().getInstanceID());
                }
            }

            for (int i = 0; i < uniqueAssignedInstanceIDs.size(); i++) {
                for (int j = 0; j < thisUsersAssignments.size(); j++) {
                    if (uniqueAssignedInstanceIDs.get(i) == thisUsersAssignments.get(j).getInstance().getInstanceID()) {
                        for (int j2 = 0; j2 < thisUsersAssignments.get(j).getAssignedLabels().size(); j2++) {
                            assignedLabelIDs.add(thisUsersAssignments.get(j).getAssignedLabels().get(j2).getLabelID());
                        }
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
        ArrayList<Assignment> userAssignments = new ArrayList<Assignment>();
        double result = 0;
        int count = 0;

        for (int m = 0; m < datasetList.size(); m++) {
            ArrayList<Assignment> assignmentList = datasetList.get(m).getAssignmentList();

            for (int i = 0; i < assignmentList.size(); i++) {
                if (assignmentList.get(i).getUser() != null
                        && user.getUserID() == assignmentList.get(i).getUser().getUserID()) {
                    userAssignments.add(assignmentList.get(i));
                }
            }

            if (userAssignments.size() > 0) {
                long sec = userAssignments.get(userAssignments.size() - 1).getDateTime().getTime()
                        - userAssignments.get(0).getDateTime().getTime();
                result += sec;
                count++;
            }
        }

        result = result / (count * 1.0);

        return result;
    }

    // A-7
    public double standartDev(User user, ArrayList<Dataset> datasetList) {

        double sum = 0.0, standardDeviation = 0.0;
        ArrayList<Long> seconds = new ArrayList<Long>();

        for (int m = 0; m < datasetList.size(); m++) {
            ArrayList<Assignment> assignmentList = datasetList.get(m).getAssignmentList();
            for (int i = 0; i < assignmentList.size(); i++) {
                if (assignmentList.get(i).getUser() != null
                        && assignmentList.get(i).getUser().getUserID() == user.getUserID()) {
                    Long sec = (assignmentList.get(i).getDateTime().getTime()) / 1000;
                    seconds.add(sec);
                }
            }
        }

        for (long num : seconds) {
            sum += num;
        }

        int length = seconds.size();
        double mean = sum / length;

        for (long num : seconds) {
            standardDeviation += Math.pow(num - mean, 2);
        }

        return Math.sqrt(standardDeviation / length);
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

        // find max frequencyuency.
        int max_count = 0, res = -1;

        for (Entry<Integer, Integer> val : hashmap.entrySet()) {
            if (max_count < val.getValue()) {
                res = val.getKey();
                max_count = val.getValue();
            }
        }
        return max_count;
    }
}
