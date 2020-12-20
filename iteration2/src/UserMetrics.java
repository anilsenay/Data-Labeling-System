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
        int count = 0;
        ArrayList<Integer> datasetIds = new ArrayList<Integer>();
        while (datasetIterator.hasNext()) {

            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            JsonArray users = (JsonArray) datasetObj.get("users");
            Iterator<JsonElement> usersIterator = users.iterator();

            while (usersIterator.hasNext()) {
                JsonObject userObj = (JsonObject) usersIterator.next();
                if (userObj.get("user_id").getAsInt() == user.getUserID()) {
                    int datasetID = (datasetObj.get("dataset_id").getAsInt());
                    datasetIds.add(datasetID);
                    count++;
                }

            }
        }

        return datasetIds;
    }

    // Buraya rapordan çekilen dataset gönderilmeli /////////////////
    // A-2 List of all datasets with their completeness percentage
    public float datasetCompletenessPer(User user, JsonObject report) {
        JsonArray userObjects = (JsonArray) report.get("users");
        Iterator<JsonElement> userListIterator = userObjects.iterator();
        int instanceCount = 0;
        ArrayList<Integer> assignedInstanceID = new ArrayList<Integer>();
        ArrayList<Instance> instances = new ArrayList<Instance>();

        while (userListIterator.hasNext()) {
            JsonObject userObj = (JsonObject) (userListIterator.next());
            int userID = userObj.get("user_id").getAsInt();

            if (userID != user.getUserID())
                continue;

            JsonArray datasetStatus = (JsonArray) userObj.get("datasets_status");
            Iterator<JsonElement> datasetStatusIterator = datasetStatus.iterator();

            while (datasetStatusIterator.hasNext()) {
                Dataset currentDataset = ReportingMechanism.getInstance().getDataset();
                int currentDatasetID = currentDataset.getDatasetID();

                JsonObject datasetObj = (JsonObject) (datasetStatusIterator.next());
                int datasetId = (datasetObj.get("dataset_id").getAsInt());

                if (datasetId != currentDatasetID)
                    continue;

                ArrayList<Assignment> assignments = currentDataset.getAssignmentList();
                instances = currentDataset.getInstances();

                int size = assignments.size();
                for (int i = 0; i < size; i++) {
                    if (assignments.get(i).getUser().getUserID() == userID) {
                        int instanceID = assignments.get(i).getInstance().getInstanceID();
                        if (!assignedInstanceID.contains(instanceID)) {
                            assignedInstanceID.add(instanceID);
                        }
                    }
                }
            }
        }

        instanceCount = instances.size();

        float compPer = (float) ((1.0 * assignedInstanceID.size()) / instanceCount) * 100;
        return compPer;
    }

    public float datasetCompletenessPerCurrent(User user) {
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
        return compPerCurrent;
    }

    // A-3
    public int numberOfInstancesLabeled() {
        return 1;
    }

    // A-4 TEST EDİLMELİ ???
    public int uniqueNumOfInstancesLabeled(User user, ArrayList<Assignment> assignmentList, Assignment newAssignment) {

        for (int i = 0; i < assignmentList.size(); i++) {
            if (user.getUserID() == assignmentList.get(i).getUser().getUserID()) {
                if (newAssignment.getInstance().getInstanceID() == assignmentList.get(i).getInstance()
                        .getInstanceID()) {
                    return 0;
                }
            }
        }
        return 1;
    }

    // A-5
    public double consistencyPercentagesForUser(User user, ArrayList<Assignment> assignmentList,
            double prevPercentage) {
        ArrayList<Assignment> thisUsersAssignments = new ArrayList<Assignment>();
        HashSet<Integer> uniqueAssignedInstances = new HashSet<Integer>();
        ArrayList<ArrayList<Integer>> labelsOfEachInstance = new ArrayList<ArrayList<Integer>>();
        double newPercentage = prevPercentage;
        int countUserAssignments = 0;

        for (int i = 0; i < assignmentList.size(); i++) {
            if (user.getUserID() == assignmentList.get(i).getUser().getUserID()) {
                thisUsersAssignments.add(assignmentList.get(i));
                countUserAssignments++;
            }

        }
        for (int i = 0; i < thisUsersAssignments.size(); i++) {
            uniqueAssignedInstances.add(thisUsersAssignments.get(i).getInstance().getInstanceID());
        }

        Integer[] uniqueAssignedInstancesArray = (uniqueAssignedInstances
                .toArray(new Integer[uniqueAssignedInstances.size()]));
        Arrays.sort(uniqueAssignedInstancesArray);
        for (int j2 = 0; j2 < uniqueAssignedInstancesArray.length; j2++) {
            labelsOfEachInstance.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < uniqueAssignedInstances.size(); i++) {
            for (int j = 0; j < thisUsersAssignments.size(); j++) {
                if (uniqueAssignedInstancesArray[i] == thisUsersAssignments.get(j).getInstance().getInstanceID()) {

                    for (int j2 = 0; j2 < thisUsersAssignments.get(j).getAssignedLabels().size(); j2++) {

                        labelsOfEachInstance.get(i)
                                .add(thisUsersAssignments.get(j).getAssignedLabels().get(j2).getLabelID());
                    }
                }
            }
        }
        double consistencyPerInstance = 0.0;
        for (int k = 0; k < labelsOfEachInstance.size(); k++) {
            double mostFreq = ((double) mostfrequent(
                    labelsOfEachInstance.get(k).toArray(new Integer[labelsOfEachInstance.get(k).size()])));
            consistencyPerInstance = mostFreq / labelsOfEachInstance.get(k).size();

            if (prevPercentage != 0)
                newPercentage = ((newPercentage * (countUserAssignments + 1)) + consistencyPerInstance) / (k + 2);
            else
                newPercentage = ((newPercentage * (countUserAssignments)) + consistencyPerInstance) / (k + 1);
        }
        BigDecimal bd = new BigDecimal(newPercentage).setScale(2, RoundingMode.HALF_UP);
        System.out.println("CONSISTENCY: " + consistencyPerInstance);
        return bd.doubleValue();
    }

    // A-6
    public double averageTimeSpent(User user, ArrayList<Assignment> assignmentList) {
        ArrayList<Long> seconds = new ArrayList<Long>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getUser().getUserID() == user.getUserID()) {
                Long sec = (assignmentList.get(i).getDateTime().getTime()) / 1000;
                seconds.add(sec);
            }
        }
        double average = seconds.stream().mapToLong(value -> value).average().orElse(0.0);
        return average;
    }

    // A-7
    public double standartDev(User user, ArrayList<Assignment> assignmentList) {

        double standartDeviation = 0.0;
        double sum = 0.0;
        double mean = 0.0;
        double result = 0.0;
        double sq = 0.0;

        ArrayList<Long> seconds = new ArrayList<Long>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getUser().getUserID() == user.getUserID()) {
                Long sec = (assignmentList.get(i).getDateTime().getTime()) / 1000;
                seconds.add(sec);
            }
        }
        for (int i = 0; i < seconds.size(); i++) {
            sum += seconds.get(i);
        }
        mean = sum / seconds.size();

        for (int i = 0; i < seconds.size(); i++) {

            standartDeviation += Math.pow((seconds.get(i) - mean), 2);
        }
        sq = standartDeviation / seconds.size();
        result = Math.sqrt(sq);

        return result;
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
