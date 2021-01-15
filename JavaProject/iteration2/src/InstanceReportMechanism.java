import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

// InstanceReportMechanism for reporting InstancePerformances.
public class InstanceReportMechanism {

    private ArrayList<InstancePerformance> instancePerformances = new ArrayList<InstancePerformance>();

    public InstanceReportMechanism() {

    }

    // Update instance.
    public void updateInstance(Instance instance) {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        Report report = ReportingMechanism.getInstance().getReport();

        InstancePerformance instancePerformance = null;
        for (int i = 0; i < instancePerformances.size(); i++) {
            if (instancePerformances.get(i).getInstance().getInstanceID() == instance.getInstanceID()) {
                instancePerformance = instancePerformances.get(i);
                break;
            }
        }

        int totalNumOfLabels = instancePerformance.getNumberOfLabelAssignment(dataset.getAssignmentList());
        int uniqueNumOfLabels = instancePerformance.getNumberOfUniqueLabelAssignment(dataset.getAssignmentList());
        int uniqueNumOfUsers = instancePerformance.getNumberOfUniqueUsers(dataset.getAssignmentList());
        double entropy = instancePerformance.getEntropy(dataset.getAssignmentList());

        JsonObject reportObject = report.getJsonObject();
        JsonArray instances = (JsonArray) reportObject.get("instances");
        Iterator<JsonElement> instanceIterator = instances.iterator();
        while (instanceIterator.hasNext()) {
            JsonObject instanceObj = (JsonObject) (instanceIterator.next());
            if (instanceObj.get("instance_id").getAsInt() != instance.getInstanceID())
                continue;

            instanceObj.addProperty("total_number_of_labels", totalNumOfLabels);
            instanceObj.addProperty("unique_number_of_labels", uniqueNumOfLabels);
            instanceObj.addProperty("unique_number_of_users", uniqueNumOfUsers);
            instanceObj.addProperty("entropy", entropy);

            // Most frequent labels for instances.

            HashMap<String, Double> parameters = instancePerformance
                    .getMostFreqLabelAndPerc(dataset.getAssignmentList());
            String key = parameters.keySet().toArray(new String[parameters.keySet().size()])[0];

            JsonObject mostFreqLabel = new JsonObject();
            mostFreqLabel.addProperty("label_name", key);
            mostFreqLabel.addProperty("frequency", parameters.get(key));
            instanceObj.add("most_freq_label", mostFreqLabel);

            // Most frequent labels for instances.

            // list labels for instances.

            JsonArray listLabels = (JsonArray) instanceObj.get("list_labels");
            HashMap<String, Double> labelsList = instancePerformance.getListClassLabels(dataset.getAssignmentList());
            String[] keys = labelsList.keySet().toArray(new String[labelsList.keySet().size()]);

            int labelsListSize = keys.length;

            // Flush labels list array.
            for (int z = listLabels.size() - 1; z >= 0; z--) {
                listLabels.remove(z);
            }

            // Recreate labels list.
            for (int i = 0; i < labelsListSize; i++) {
                JsonObject labelListObject = new JsonObject();
                labelListObject.addProperty("label_name", keys[i]);
                labelListObject.addProperty("percentage", labelsList.get(keys[i]));
                listLabels.add(labelListObject);
            }

            // List labels for instances.
        }
    }

    public ArrayList<InstancePerformance> getInstancePerformances() {
        return this.instancePerformances;
    }

    public void setInstancePerformance(ArrayList<InstancePerformance> instancePerformances) {
        this.instancePerformances = instancePerformances;
    }

    public void addInstancePerformance(InstancePerformance instancePerformance) {
        this.instancePerformances.add(instancePerformance);
    }
}
