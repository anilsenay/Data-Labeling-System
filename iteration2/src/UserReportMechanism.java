import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class UserReportMechanism {

    private ArrayList<UserPerformance> userPerformances = new ArrayList<UserPerformance>();

    public UserReportMechanism() {

    }

    public void updateUser(User user, Assignment newAssignment) {
        Dataset dataset = ReportingMechanism.getInstance().getDataset();
        Report report = ReportingMechanism.getInstance().getReport();

        UserPerformance userPerformance = null;
        for (int i = 0; i < userPerformances.size(); i++) {
            if (userPerformances.get(i).getUser().getUserID() == user.getUserID()) {
                userPerformance = userPerformances.get(i);
                break;
            }
        }

        int numberOfDatasets = userPerformance.getAssignedDatasets().size();
        float currentDatasetStatus = userPerformance.getDatasetCompletenessPer(report.getJsonObject());
        int totalNumberOfInstances = userPerformance.getNumberOfInstancesLabeled();
        int numberOfUniqueInstances = userPerformance.getUniqueNumOfInstancesLabeled(dataset.getAssignmentList(),
                newAssignment);
        double avgTime = userPerformance.getAverageTimeSpent(dataset.getAssignmentList());
        double stdDev = userPerformance.getStandartDev(ReportingMechanism.getInstance().getAllDatasets());

        JsonObject reportObject = report.getJsonObject();
        JsonArray users = (JsonArray) reportObject.get("users");
        Iterator<JsonElement> userIterator = users.iterator();
        while (userIterator.hasNext()) {
            JsonObject userObj = (JsonObject) (userIterator.next());
            if (userObj.get("user_id").getAsInt() != user.getUserID())
                continue;

            userObj.addProperty("number_of_datasets", numberOfDatasets);
            int labeled_instances = userObj.get("labeled_instances").getAsInt();
            userObj.addProperty("labeled_instances", totalNumberOfInstances + labeled_instances);
            int unique_labeled_instances = userObj.get("unique_labeled_instances").getAsInt();
            userObj.addProperty("unique_labeled_instances", unique_labeled_instances + numberOfUniqueInstances);
            double consistency_percentages = userObj.get("consistency_percentages").getAsDouble();
            double consPercentage = userPerformance
                    .getConsistencyPercentagesForUser(ReportingMechanism.getInstance().getAllDatasets());
            userObj.addProperty("consistency_percentages", consPercentage);
            userObj.addProperty("avg_time", avgTime);
            userObj.addProperty("std_dev", stdDev);

            JsonArray userDatasets = (JsonArray) userObj.get("datasets_status");
            Iterator<JsonElement> userDatasetIterator = userDatasets.iterator();

            // if there is no dataset added
            if (!userDatasetIterator.hasNext()) {
                for (int i = 0; i < userPerformance.getAssignedDatasets().size(); i++) {
                    JsonObject datasetStatusObj = new JsonObject();
                    datasetStatusObj.addProperty("dataset_id", userPerformance.getAssignedDatasets().get(i));
                    datasetStatusObj.addProperty("status", 0);
                    userDatasets.add(datasetStatusObj);
                }
            }
            // if dataset exists update its status
            boolean isFound = false;
            while (userDatasetIterator.hasNext()) {
                JsonObject datasetObj = (JsonObject) (userDatasetIterator.next());
                if (dataset.getDatasetID() != (datasetObj.get("dataset_id").getAsInt()))
                    continue;
                datasetObj.addProperty("status", currentDatasetStatus);
                isFound = true;
            }
            // if new dataset added and could not be found in the list
            if (!isFound) {
                JsonObject datasetStatusObj = new JsonObject();
                datasetStatusObj.addProperty("dataset_id", dataset.getDatasetID());
                datasetStatusObj.addProperty("status", currentDatasetStatus);

                userDatasets.add(datasetStatusObj);
            }

        }
    }

    public void addUserPerformance(UserPerformance userPerformance) {
        this.userPerformances.add(userPerformance);
    }

    public ArrayList<UserPerformance> getUserPerformances() {
        return this.userPerformances;
    }
}
