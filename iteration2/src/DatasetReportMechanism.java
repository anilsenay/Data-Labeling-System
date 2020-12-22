import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class DatasetReportMechanism {

    private DatasetPerformance datasetPerformance;

    public DatasetReportMechanism() {

    }

    public DatasetReportMechanism(Dataset dataset) {
        this.datasetPerformance = new DatasetPerformance(dataset);
    }

    public void updateDataset(Dataset dataset, User user) {
        Report report = ReportingMechanism.getInstance().getReport();

        float completeness = this.datasetPerformance.getCompletenessPercentage();
        int numberOfUsers = this.datasetPerformance.getNumberOfUserAssigned();

        JsonObject reportObject = report.getJsonObject();
        JsonArray datasets = (JsonArray) reportObject.get("datasets");
        Iterator<JsonElement> datasetIterator = datasets.iterator();
        while (datasetIterator.hasNext()) {
            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            if (datasetObj.get("dataset_id").getAsInt() != dataset.getDatasetID())
                continue;

            datasetObj.addProperty("number_of_users", numberOfUsers);
            datasetObj.addProperty("completeness", completeness);

            // final instance labels part
            JsonArray finalLabels = (JsonArray) datasetObj.get("final_instance_labels");
            ArrayList<String> finalLabelsList = datasetPerformance.getDistributionInstance();
            int finalLabelsListSize = finalLabels.size();

            // flush final labels array
            for (int z = finalLabels.size() - 1; z >= 0; z--) {
                finalLabels.remove(z);
            }
            // recreate final labels
            for (int i = 0; i < finalLabelsList.size(); i = i + 2) {
                JsonObject finalLabelObj = new JsonObject();
                finalLabelObj.addProperty("instance", finalLabelsList.get(i));
                finalLabelObj.addProperty("percentage", Double.parseDouble(finalLabelsList.get(i + 1)));
                finalLabels.add(finalLabelObj);
            }

            // unique number of instances for each label -------------------

            JsonArray uniqueInstances = (JsonArray) datasetObj.get("unique_instance_number_for_each_label");

            // flush unique instance labels array
            for (int z = uniqueInstances.size() - 1; z >= 0; z--) {
                uniqueInstances.remove(z);
            }

            ArrayList<Label> labels = datasetPerformance.getDataset().getClassLabels();
            for (int j = 0; j < labels.size(); j++) {
                JsonObject labelObj = new JsonObject();
                int number = datasetPerformance.getNumOfUniqueInstance(labels.get(j));
                labelObj.addProperty("label_id", labels.get(j).getLabelID());
                labelObj.addProperty("unique_instance_number", number);
                uniqueInstances.add(labelObj);
            }

            // --------------------------- unique number of instances for each label

            // find current user's userMetric
            UserPerformance userPerformance = null;
            ArrayList<UserPerformance> userPerformances = ReportingMechanism.getInstance().getUserReportMechanism()
                    .getUserPerformances();
            for (int i = 0; i < userPerformances.size(); i++) {
                if (userPerformances.get(i).getUser().getUserID() == user.getUserID()) {
                    userPerformance = userPerformances.get(i);
                    break;
                }
            }

            // user completeness for datasets in report
            // --------------------------------------------

            JsonArray userCompleteness = (JsonArray) datasetObj.get("user_completeness");
            Iterator<JsonElement> userCompIterator = userCompleteness.iterator();

            if (!userCompIterator.hasNext()) {
                for (int i = 0; i < userPerformances.size(); i++) {
                    JsonObject completenessObject = new JsonObject();
                    completenessObject.addProperty("user_id", userPerformances.get(i).getUser().getUserID());
                    completenessObject.addProperty("percentage", 0);
                    userCompleteness.add(completenessObject);
                }
            }

            userCompIterator = userCompleteness.iterator();
            boolean isFound = false;
            while (userCompIterator.hasNext()) {
                JsonObject completenessObj = (JsonObject) (userCompIterator.next());
                if (completenessObj.get("user_id").getAsInt() == user.getUserID()) {
                    completenessObj.addProperty("percentage",
                            userPerformance.getDatasetCompletenessPer(report.getJsonObject()));
                    isFound = true;
                }
            }
            if (!isFound) {
                JsonObject completenessObject = new JsonObject();
                completenessObject.addProperty("user_id", user.getUserID());
                completenessObject.addProperty("percentage",
                        userPerformance.getDatasetCompletenessPer(report.getJsonObject()));
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
            }

            // -------------------------------------------- user completeness for datasets
            // in report

            // user consistency for datasets in report ----------------------------------

            JsonArray userConsistency = (JsonArray) datasetObj.get("user_consistency");
            Iterator<JsonElement> userConsIterator = userConsistency.iterator();

            if (!userConsIterator.hasNext()) {
                for (int i = 0; i < userPerformances.size(); i++) {
                    JsonObject consistencyObject = new JsonObject();
                    consistencyObject.addProperty("user_id", userPerformances.get(i).getUser().getUserID());
                    consistencyObject.addProperty("consistency_percentages", 0);
                    userConsistency.add(consistencyObject);
                }
            }

            userConsIterator = userConsistency.iterator();
            isFound = false;
            while (userConsIterator.hasNext()) {
                JsonObject consistencyObj = (JsonObject) (userConsIterator.next());
                if (consistencyObj.get("user_id").getAsInt() == user.getUserID()) {
                    JsonArray users = (JsonArray) reportObject.get("users");
                    Iterator<JsonElement> userIterator = users.iterator();
                    while (userIterator.hasNext()) {
                        JsonObject userObj = (JsonObject) (userIterator.next());
                        if (userObj.get("user_id").getAsInt() == user.getUserID()) {
                            consistencyObj.addProperty("consistency_percentages",
                                    userPerformance.getConsistencyPercentagesForUser(
                                            ReportingMechanism.getInstance().getAllDatasets()));
                        }
                    }
                    isFound = true;
                }
            }
            if (!isFound) {
                JsonObject consistencyObject = new JsonObject();

                JsonArray users = (JsonArray) reportObject.get("users");
                Iterator<JsonElement> userIterator = users.iterator();
                while (userIterator.hasNext()) {
                    JsonObject userObj = (JsonObject) (userIterator.next());
                    if (userObj.get("user_id").getAsInt() == user.getUserID()) {
                        consistencyObject.addProperty("user_id", user.getUserID());
                        consistencyObject.addProperty("consistency_percentages", userPerformance
                                .getConsistencyPercentagesForUser(ReportingMechanism.getInstance().getAllDatasets()));
                    }
                }
                userConsistency.add(consistencyObject);
            }

            // ---------------------------------- user consistency for datasets in report

        }

    }

    public DatasetPerformance getDatasetPerformance() {
        return this.datasetPerformance;
    }

    public void setDatasetPerformance(DatasetPerformance datasetPerformance) {
        this.datasetPerformance = datasetPerformance;
    }

}
