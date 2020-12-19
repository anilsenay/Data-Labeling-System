import java.util.ArrayList;
import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;

public class ReportingMechanism {
    private static ReportingMechanism reportingMechanism;
    private Report report = new Report();
    private ArrayList<UserMetrics> userMetrics = new ArrayList<UserMetrics>();
    private DatasetMetrics datasetMetrics;
    private ArrayList<InstanceMetrics> instanceMetrics = new ArrayList<InstanceMetrics>();
    private Dataset dataset = null;

    private ReportingMechanism(Dataset dataset) {
        this.dataset = dataset;
        this.datasetMetrics = new DatasetMetrics(this.dataset);
    }

    private ReportingMechanism() {
    }

    public static synchronized ReportingMechanism getInstance() {
        if (reportingMechanism == null) {
            reportingMechanism = new ReportingMechanism();
        }
        return reportingMechanism;
    }

    public void importReport(Dataset dataset, ArrayList<User> userlist) {
        report.handleReport(this);
    }

    public void updateReport(Assignment assignment) {
        updateUser(assignment.getUser(), assignment);
        updateInstance(assignment.getInstance());
        updateDataset(this.dataset);
        this.report.writeReport();
    }

    public void updateUser(User user, Assignment newAssignment) {
        UserMetrics userMetric = null;
        for (int i = 0; i < userMetrics.size(); i++) {
            if (userMetrics.get(i).getUser().getUserID() == user.getUserID()) {
                userMetric = userMetrics.get(i);
                break;
            }
        }

        int numberOfDatasets = userMetric.getAssignedDatasets().size();
        float currentDatasetStatus = userMetric.datasetCompletenessPer(this.report.getJsonObject());
        int totalNumberOfInstances = userMetric.numberOfInstancesLabeled();
        int numberOfUniqueInstances = userMetric.uniqueNumOfInstancesLabeled(dataset.getAssignmentList(),
                newAssignment);

        double avgTime = userMetric.averageTimeSpent(dataset.getAssignmentList());
        double stdDev = userMetric.standartDev(dataset.getAssignmentList());

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
            double consPercentage = userMetric.consistencyPercentagesForUser(dataset.getAssignmentList(),
                    consistency_percentages);
            userObj.addProperty("consistency_percentages", consPercentage);
            userObj.addProperty("avg_time", avgTime);
            userObj.addProperty("std_dev", stdDev);

            JsonArray userDatasets = (JsonArray) userObj.get("datasets_status");
            Iterator<JsonElement> userDatasetIterator = userDatasets.iterator();
            while (userDatasetIterator.hasNext()) {
                JsonObject datasetObj = (JsonObject) (userDatasetIterator.next());
                if (this.dataset.getDatasetID() != (datasetObj.get("dataset_id").getAsInt()))
                    continue;
                datasetObj.addProperty("status", currentDatasetStatus);

            }

        }
    }

    public void updateInstance(Instance instance) {
        InstanceMetrics instanceMetric = null;
        for (int i = 0; i < instanceMetrics.size(); i++) {
            if (instanceMetrics.get(i).getInstance().getInstanceID() == instance.getInstanceID()) {
                instanceMetric = instanceMetrics.get(i);
                break;
            }
        }

        int totalNumOfLabels = instanceMetric.numberOfLabelAssignment(dataset.getAssignmentList());
        int uniqueNumOfLabels = 0;
        int uniqueNumOfUsers = 0;
        double entropy = 0;

        JsonObject reportObject = report.getJsonObject();
        JsonArray instances = (JsonArray) reportObject.get("instances");
        Iterator<JsonElement> instanceIterator = instances.iterator();
        while (instanceIterator.hasNext()) {
            JsonObject instanceObj = (JsonObject) (instanceIterator.next());
            if (instanceObj.get("user_id").getAsInt() != instance.getInstanceID())
                continue;

            instanceObj.addProperty("total_number_of_labels", totalNumOfLabels);
            instanceObj.addProperty("unique_number_of_labels", uniqueNumOfLabels);
            instanceObj.addProperty("unique_number_of_users", uniqueNumOfUsers);
            instanceObj.addProperty("entropy", entropy);

            // guncellenecek

        }

    }

    public void updateDataset(Dataset dataset) {
        int completeness = 0;
        int numberOfUsers = 0;

        JsonObject reportObject = report.getJsonObject();
        JsonArray datasets = (JsonArray) reportObject.get("datasets");
        Iterator<JsonElement> datasetIterator = datasets.iterator();
        while (datasetIterator.hasNext()) {
            JsonObject datasetObj = (JsonObject) (datasetIterator.next());
            if (datasetObj.get("dataset_id").getAsInt() != dataset.getDatasetID())
                continue;

            datasetObj.addProperty("number_of_users", numberOfUsers);
            datasetObj.addProperty("completeness", completeness);

            // guncellencek

        }

    }

    public void printPartA(Assignment a) {
        // report.getDatasetNumber()
    }

    public void printPartB(Assignment a) {
        // report.getDatasetNumber()
    }

    public void printPartC(Assignment a) {
        // report.getDatasetNumber()

    }

    public void adduserMetrics(UserMetrics userMetric) {
        this.userMetrics.add(userMetric);
    }

    public ArrayList<UserMetrics> getUserMetrics() {
        return this.userMetrics;
    }

    public DatasetMetrics getDatasetMetrics() {
        return this.datasetMetrics;
    }

    public ArrayList<InstanceMetrics> getInstanceMetrics() {
        return this.instanceMetrics;
    }

    public void setUserMetrics(ArrayList<UserMetrics> userMetrics) {
        this.userMetrics = userMetrics;
    }

    public void setDatasetMetrics(DatasetMetrics datasetMetrics) {
        this.datasetMetrics = datasetMetrics;
    }

    public void setInstanceMetrics(ArrayList<InstanceMetrics> instanceMetrics) {
        this.instanceMetrics = instanceMetrics;
    }

    public Dataset getDataset() {
        return this.dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }
}
