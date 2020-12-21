import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.FileWriter;

public class Report {

    private JsonObject reportObject = new JsonObject();

    public Report() {
    }

    public void handleReport(ReportingMechanism reportingMechanism) {
        File reportFile = new File("report.json");
        if (reportFile.exists()) { // dataset output u varsa
            loadReport();
            JsonArray users = (JsonArray) this.reportObject.get("users");
            addNewUsers(reportingMechanism.getUserReportMechanism().getUserPerformances(), users);
            JsonArray instances = (JsonArray) this.reportObject.get("instances");
            addNewInstances(reportingMechanism.getInstanceReportMechanism().getInstancePerformances(), instances);
            JsonArray datasets = (JsonArray) this.reportObject.get("datasets");
            addNewDataset(reportingMechanism.getDataset(), reportingMechanism.getUserReportMechanism(), datasets);
        } else
            createReport(reportingMechanism);
    }

    public void createReport(ReportingMechanism reportingMechanism) {
        JsonArray userArray = new JsonArray();
        JsonArray instanceArray = new JsonArray();
        JsonArray datasetArray = new JsonArray();

        addNewUsers(reportingMechanism.getUserReportMechanism().getUserPerformances(), userArray);
        addNewInstances(reportingMechanism.getInstanceReportMechanism().getInstancePerformances(), instanceArray);
        addNewDataset(reportingMechanism.getDataset(), reportingMechanism.getUserReportMechanism(), datasetArray);

        reportObject.add("users", userArray);
        reportObject.add("instances", instanceArray);
        reportObject.add("datasets", datasetArray);
    }

    public void addNewDataset(Dataset dataset, UserReportMechanism userReportMechanism, JsonArray datasetArray) {
        JsonObject reportObject = this.reportObject;
        JsonArray datasets = (JsonArray) reportObject.get("datasets");
        Iterator<JsonElement> datasetIterator = datasets != null ? datasets.iterator() : null;

        boolean isFound = false;
        while (datasetIterator != null && datasetIterator.hasNext()) {
            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            if (datasetObj.get("dataset_id").getAsInt() == dataset.getDatasetID()) {
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            JsonObject datasetObject = new JsonObject();

            datasetObject.addProperty("dataset_id", dataset.getDatasetID());
            datasetObject.addProperty("dataset_name", dataset.getDatasetName());
            datasetObject.addProperty("completeness", 0);

            JsonArray finalInstanceLabels = new JsonArray();
            datasetObject.add("final_instance_labels", finalInstanceLabels);

            datasetObject.addProperty("number_of_users", userReportMechanism.getUserPerformances().size());

            JsonArray userCompleteness = new JsonArray();
            for (int j = 0; j < userReportMechanism.getUserPerformances().size(); j++) {
                JsonObject userObject = new JsonObject();
                userObject.addProperty("user_id",
                        userReportMechanism.getUserPerformances().get(j).getUser().getUserID());
                userObject.addProperty("percentage", 0);
                userCompleteness.add(userObject);
            }
            datasetObject.add("user_completeness", userCompleteness);

            JsonArray userConsistency = new JsonArray();
            for (int j = 0; j < userReportMechanism.getUserPerformances().size(); j++) {
                JsonObject userObject = new JsonObject();
                userObject.addProperty("user_id",
                        userReportMechanism.getUserPerformances().get(j).getUser().getUserID());
                userObject.addProperty("consistency_percentages", 0);
                userConsistency.add(userObject);
            }
            datasetObject.add("user_consistency", userConsistency);

            JsonArray uniqueInstanceNumberEachLabel = new JsonArray();
            ArrayList<Label> labels = dataset.getClassLabels();
            for (int j = 0; j < labels.size(); j++) {
                JsonObject labelObject = new JsonObject();
                labelObject.addProperty("label_id", labels.get(j).getLabelID());
                labelObject.addProperty("unique_instance_number", 0);
                uniqueInstanceNumberEachLabel.add(labelObject);
            }
            datasetObject.add("unique_instance_number_for_each_label", uniqueInstanceNumberEachLabel);

            datasetArray.add(datasetObject);
        }
    }

    public void addNewInstances(ArrayList<InstancePerformance> instanceMetrics, JsonArray instanceArray) {
        JsonObject reportObject = this.reportObject;
        JsonArray instances = (JsonArray) reportObject.get("instances");
        Iterator<JsonElement> instanceIterator = instances != null ? instances.iterator() : null;

        int instanceMetricsSize = instanceMetrics.size();
        for (int i = 0; i < instanceMetricsSize; i++) {

            boolean isFound = false;
            while (instanceIterator != null && instanceIterator.hasNext()) {
                JsonObject instanceObj = (JsonObject) (instanceIterator.next());
                if (instanceObj.get("instance_id").getAsInt() == instanceMetrics.get(i).getInstance().getInstanceID()
                        && instanceObj.get("instance_name").getAsString()
                                .equals(instanceMetrics.get(i).getInstance().getContent())) {
                    isFound = true;
                    break;
                }
            }
            if (isFound)
                continue;
            // create instance object and add properties
            JsonObject instanceObject = new JsonObject();
            instanceObject.addProperty("instance_id", instanceMetrics.get(i).getInstance().getInstanceID());
            instanceObject.addProperty("instance_name", instanceMetrics.get(i).getInstance().getContent());
            instanceObject.addProperty("total_number_of_labels", 0);
            instanceObject.addProperty("unique_number_of_labels", 0);
            instanceObject.addProperty("unique_number_of_users", 0);

            // initially there is no label for an instance
            JsonObject mostFreqLabel = new JsonObject();
            instanceObject.add("most_freq_label", mostFreqLabel);

            // initially there is no label for an instance
            JsonArray listLabels = new JsonArray();
            instanceObject.add("list_labels", listLabels);

            instanceObject.addProperty("entropy", 0);

            instanceArray.add(instanceObject);
        }
    }

    public void addNewUsers(ArrayList<UserPerformance> userMetrics, JsonArray userArray) {
        JsonObject reportObject = this.reportObject;
        JsonArray users = (JsonArray) reportObject.get("users");
        Iterator<JsonElement> usersIterator = users != null ? users.iterator() : null;

        int userMetricsSize = userMetrics.size();
        for (int i = 0; i < userMetricsSize; i++) {

            boolean isFound = false;
            while (usersIterator != null && usersIterator.hasNext()) {
                JsonObject userObj = (JsonObject) (usersIterator.next());
                if (userObj.get("user_id").getAsInt() == userMetrics.get(i).getUser().getUserID()) {
                    isFound = true;
                    break;
                }
            }
            if (isFound)
                continue;

            JsonObject userObject = new JsonObject();
            userObject.addProperty("user_id", userMetrics.get(i).getUser().getUserID());
            userObject.addProperty("user name", userMetrics.get(i).getUser().getUserName());
            userObject.addProperty("number_of_datasets", userMetrics.get(i).getAssignedDatasets().size());

            JsonArray userDatasets = new JsonArray();
            for (int j = 0; j < userMetrics.get(i).getAssignedDatasets().size(); j++) {
                JsonObject datasetObject = new JsonObject();
                datasetObject.addProperty("dataset_id", userMetrics.get(i).getAssignedDatasets().get(j));
                datasetObject.addProperty("status", 0);
                userDatasets.add(datasetObject);
            }
            userObject.add("datasets_status", userDatasets);

            userObject.addProperty("labeled_instances", 0);
            userObject.addProperty("unique_labeled_instances", 0);
            userObject.addProperty("consistency_percentages", 0);
            userObject.addProperty("avg_time", 0);
            userObject.addProperty("std_dev", 0);

            userArray.add(userObject);
        }
    }

    // read report if it has already created
    public void loadReport() {
        JsonElement json = null;
        try {
            json = JsonParser.parseReader(new InputStreamReader(new FileInputStream("report.json"), "UTF-8"));
        } catch (Exception e) {
            Logger.getInstance().error(new Date(), e.toString());
        }

        this.reportObject = json.getAsJsonObject();
    }

    public void writeReport() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("report.json")) {
            gson.toJson(this.reportObject, writer);
            writer.close();
        } catch (Exception e) {
            Logger.getInstance().error(new Date(), e.toString());
        }
    }

    public JsonObject getJsonObject() {
        return this.reportObject;
    }

}
