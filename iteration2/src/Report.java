import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

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
        } else
            createReport(reportingMechanism);

    }

    public void createReport(ReportingMechanism reportingMechanism) {
        ArrayList<UserPerformance> userMetrics = reportingMechanism.getUserReportMechanism().getUserPerformances();
        ArrayList<InstancePerformance> instanceMetrics = reportingMechanism.getInstanceReportMechanism()
                .getInstancePerformances();
        DatasetPerformance datasetMetrics = reportingMechanism.getDatasetReportMechanism().getDatasetPerformance();

        JsonArray userArray = new JsonArray();
        JsonArray instanceArray = new JsonArray();
        JsonArray datasetArray = new JsonArray();

        int userMetricsSize = userMetrics.size();
        for (int i = 0; i < userMetricsSize; i++) {

            // user part --------------------------------------
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
            // ------------------------- user part

        }

        // instance part --------------------------------
        int instanceMetricsSize = instanceMetrics.size();
        for (int i = 0; i < instanceMetricsSize; i++) {

            // create instance object and add properties
            JsonObject instanceObject = new JsonObject();
            instanceObject.addProperty("instance_id", instanceMetrics.get(i).getInstance().getInstanceID());
            instanceObject.addProperty("instance name", instanceMetrics.get(i).getInstance().getContent());
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
        // -------------------------------- instance part

        // dataset part --------------------------------------
        JsonObject datasetObject = new JsonObject();
        datasetObject.addProperty("dataset_id", datasetMetrics.getDataset().getDatasetID());
        datasetObject.addProperty("dataset_name", datasetMetrics.getDataset().getDatasetName());
        datasetObject.addProperty("completeness", 0);

        // initially there is no label for an instance
        JsonArray finalInstanceLabels = new JsonArray();
        datasetObject.add("final_instance_labels", finalInstanceLabels);

        datasetObject.addProperty("number_of_users", userMetricsSize);

        JsonArray userCompleteness = new JsonArray();
        for (int j = 0; j < userMetrics.get(j).getAssignedDatasets().size(); j++) {
            JsonObject userObject = new JsonObject();
            userObject.addProperty("user_id", userMetrics.get(j).getUser().getUserID());
            userObject.addProperty("percentage", 0);
            userCompleteness.add(userObject);
        }
        datasetObject.add("user_completeness", userCompleteness);

        JsonArray userConsistency = new JsonArray();
        for (int j = 0; j < userMetrics.get(j).getAssignedDatasets().size(); j++) {
            JsonObject userObject = new JsonObject();
            userObject.addProperty("user_id", userMetrics.get(j).getUser().getUserID());
            userObject.addProperty("consistency_percentages", 0);
            userConsistency.add(userObject);
        }
        datasetObject.add("user_consistency", userConsistency);

        JsonArray uniqueInstanceNumberEachLabel = new JsonArray();
        ArrayList<Label> labels = datasetMetrics.getDataset().getClassLabels();
        for (int j = 0; j < labels.size(); j++) {
            JsonObject labelObject = new JsonObject();
            labelObject.addProperty("label_id", labels.get(j).getLabelID());
            labelObject.addProperty("unique_instance_number", 0);
            uniqueInstanceNumberEachLabel.add(labelObject);
        }
        datasetObject.add("unique_instance_number_for_each_label", uniqueInstanceNumberEachLabel);

        datasetArray.add(datasetObject);

        reportObject.add("users", userArray);
        reportObject.add("instances", instanceArray);
        reportObject.add("datasets", datasetArray);
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
