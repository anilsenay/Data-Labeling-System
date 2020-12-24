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
    // update dataset part of the report
    public void updateDataset(Dataset dataset, User user) {
    	// get current state of the report
        Report report = ReportingMechanism.getInstance().getReport();
        // calculate completeness
        float completeness = this.datasetPerformance.getCompletenessPercentage();
        // calculate number of users
        int numberOfUsers = this.datasetPerformance.getNumberOfUserAssigned();

        JsonObject reportObject = report.getJsonObject();
        JsonArray datasets = (JsonArray) reportObject.get("datasets");
        Iterator<JsonElement> datasetIterator = datasets.iterator();
        while (datasetIterator.hasNext()) {
            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            if (datasetObj.get("dataset_id").getAsInt() != dataset.getDatasetID())
                continue;
            // add number of users and completeness to report
            datasetObj.addProperty("number_of_users", numberOfUsers);
            datasetObj.addProperty("completeness", completeness);

            // final instance labels part
            JsonArray finalLabels = (JsonArray) datasetObj.get("final_instance_labels");
            ArrayList<String> finalLabelsList = datasetPerformance.getDistributionInstance();
         
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

            // starting of unique number of instances for each label part -------------------

            JsonArray uniqueInstances = (JsonArray) datasetObj.get("unique_instance_number_for_each_label");

            // flush unique instance labels array
            for (int z = uniqueInstances.size() - 1; z >= 0; z--) {
                uniqueInstances.remove(z);
            }
            
            //recreate unique instances
            ArrayList<Label> labels = datasetPerformance.getDataset().getClassLabels();
            for (int j = 0; j < labels.size(); j++) {
                JsonObject labelObj = new JsonObject();
                int number = datasetPerformance.getNumOfUniqueInstance(labels.get(j));
                labelObj.addProperty("label_id", labels.get(j).getLabelID());
                labelObj.addProperty("unique_instance_number", number);
                uniqueInstances.add(labelObj);
            }

            // --------------------------- ending of unique number of instances for each label part

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

            // starting of user completeness for datasets in report part -----------------------------------------

            JsonArray userCompleteness = (JsonArray) datasetObj.get("user_completeness");
            Iterator<JsonElement> userCompIterator = userCompleteness.iterator();
            // initially if there is no userCompleteness part, create it with user id and percentage properties 
            if (!userCompIterator.hasNext()) {
                for (int i = 0; i < userPerformances.size(); i++) {
                    JsonObject completenessObject = new JsonObject();
                    completenessObject.addProperty("user_id", userPerformances.get(i).getUser().getUserID());
                    completenessObject.addProperty("percentage", 0);
                    userCompleteness.add(completenessObject);
                }
            }
            // if objects of the userCompleteness exists, find current user and update corresponding properties
            userCompIterator = userCompleteness.iterator();
            boolean isFound = false;
            while (userCompIterator.hasNext()) {
                JsonObject completenessObj = (JsonObject) (userCompIterator.next());
                if (completenessObj.get("user_id").getAsInt() == user.getUserID()) {
                    completenessObj.addProperty("percentage", userPerformance.getDatasetCompletenessPer());
                    isFound = true;
                }
            }
            // if user added in between executions, create and update user's information
            if (!isFound) {
                JsonObject completenessObject = new JsonObject();
                completenessObject.addProperty("user_id", user.getUserID());
                completenessObject.addProperty("percentage", userPerformance.getDatasetCompletenessPer());
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
                userCompleteness.add(completenessObject);
            }

            // -------------------------------------------- ending of user completeness for datasets in report part

            // starting of user consistency for datasets in report part ----------------------------------

            JsonArray userConsistency = (JsonArray) datasetObj.get("user_consistency");
            Iterator<JsonElement> userConsIterator = userConsistency.iterator();
            // if objects of the userConsistency array is not created then create and add user id and consistency percentages 
            if (!userConsIterator.hasNext()) {
                for (int i = 0; i < userPerformances.size(); i++) {
                    JsonObject consistencyObject = new JsonObject();
                    consistencyObject.addProperty("user_id", userPerformances.get(i).getUser().getUserID());
                    consistencyObject.addProperty("consistency_percentages", 0);
                    userConsistency.add(consistencyObject);
                }
            }
            // if objects of the userConsistency array has already created then find current user and update it
            userConsIterator = userConsistency.iterator();
            isFound = false;
            while (userConsIterator.hasNext()) {
                JsonObject consistencyObj = (JsonObject) (userConsIterator.next());
                if (consistencyObj.get("user_id").getAsInt() == user.getUserID()) {
                    JsonArray users = (JsonArray) reportObject.get("users");
                    Iterator<JsonElement> userIterator = users.iterator();
                    while (userIterator.hasNext()) {
                        JsonObject userObj = (JsonObject) (userIterator.next());
                        // if current user is found in all users, update consistency percentage based on all datasets
                        if (userObj.get("user_id").getAsInt() == user.getUserID()) {
                            consistencyObj.addProperty("consistency_percentages",
                                    userPerformance.getConsistencyPercentagesForUser(
                                            ReportingMechanism.getInstance().getAllDatasets()));
                        }
                    }
                    isFound = true;
                }
            }
            // if user added in between executions, create and update user's information
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

            // ---------------------------------- ending of user consistency for datasets in report part

        }

    }
    // getter and setter methods for datasetPerformance
    public DatasetPerformance getDatasetPerformance() {
        return this.datasetPerformance;
    }

    public void setDatasetPerformance(DatasetPerformance datasetPerformance) {
        this.datasetPerformance = datasetPerformance;
    }
    
}
