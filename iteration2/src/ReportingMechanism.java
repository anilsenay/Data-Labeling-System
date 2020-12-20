import java.util.ArrayList;

public class ReportingMechanism {
    private static ReportingMechanism reportingMechanism;
    private Report report = new Report();
    private Dataset dataset = null;

    private UserReportMechanism userReportMechanism = new UserReportMechanism();
    private InstanceReportMechanism instanceReportMechanism = new InstanceReportMechanism();
    private DatasetReportMechanism datasetReportMechanism = new DatasetReportMechanism();

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
        userReportMechanism.updateUser(assignment.getUser(), assignment);
        instanceReportMechanism.updateInstance(assignment.getInstance());
        datasetReportMechanism.updateDataset(this.dataset, assignment.getUser());
        this.report.writeReport();
    }

    // public void updateUser(User user, Assignment newAssignment) {
    // UserMetrics userMetric = null;
    // for (int i = 0; i < userMetrics.size(); i++) {
    // if (userMetrics.get(i).getUser().getUserID() == user.getUserID()) {
    // userMetric = userMetrics.get(i);
    // break;
    // }
    // }

    // int numberOfDatasets = userMetric.getAssignedDatasets().size();
    // float currentDatasetStatus =
    // userMetric.datasetCompletenessPer(this.report.getJsonObject());
    // int totalNumberOfInstances = userMetric.numberOfInstancesLabeled();
    // int numberOfUniqueInstances =
    // userMetric.uniqueNumOfInstancesLabeled(dataset.getAssignmentList(),
    // newAssignment);
    // double avgTime = userMetric.averageTimeSpent(dataset.getAssignmentList());
    // double stdDev = userMetric.standartDev(dataset.getAssignmentList());

    // JsonObject reportObject = report.getJsonObject();
    // JsonArray users = (JsonArray) reportObject.get("users");
    // Iterator<JsonElement> userIterator = users.iterator();
    // while (userIterator.hasNext()) {
    // JsonObject userObj = (JsonObject) (userIterator.next());
    // if (userObj.get("user_id").getAsInt() != user.getUserID())
    // continue;

    // userObj.addProperty("number_of_datasets", numberOfDatasets);
    // int labeled_instances = userObj.get("labeled_instances").getAsInt();
    // userObj.addProperty("labeled_instances", totalNumberOfInstances +
    // labeled_instances);
    // int unique_labeled_instances =
    // userObj.get("unique_labeled_instances").getAsInt();
    // userObj.addProperty("unique_labeled_instances", unique_labeled_instances +
    // numberOfUniqueInstances);
    // double consistency_percentages =
    // userObj.get("consistency_percentages").getAsDouble();
    // double consPercentage =
    // userMetric.consistencyPercentagesForUser(dataset.getAssignmentList(),
    // consistency_percentages);
    // userObj.addProperty("consistency_percentages", consPercentage);
    // userObj.addProperty("avg_time", avgTime);
    // userObj.addProperty("std_dev", stdDev);

    // JsonArray userDatasets = (JsonArray) userObj.get("datasets_status");
    // Iterator<JsonElement> userDatasetIterator = userDatasets.iterator();

    // // if there is no dataset added
    // if (!userDatasetIterator.hasNext()) {
    // for (int i = 0; i < userMetric.getAssignedDatasets().size(); i++) {
    // JsonObject datasetStatusObj = new JsonObject();
    // datasetStatusObj.addProperty("dataset_id",
    // userMetric.getAssignedDatasets().get(i));
    // datasetStatusObj.addProperty("status", 0);
    // userDatasets.add(datasetStatusObj);
    // }
    // }
    // // if dataset exists update its status
    // boolean isFound = false;
    // while (userDatasetIterator.hasNext()) {
    // JsonObject datasetObj = (JsonObject) (userDatasetIterator.next());
    // if (this.dataset.getDatasetID() != (datasetObj.get("dataset_id").getAsInt()))
    // continue;
    // datasetObj.addProperty("status", currentDatasetStatus);
    // isFound = true;
    // }
    // // if new dataset added and could not be found in the list
    // if (!isFound) {
    // JsonObject datasetStatusObj = new JsonObject();
    // datasetStatusObj.addProperty("dataset_id", this.dataset.getDatasetID());
    // datasetStatusObj.addProperty("status", currentDatasetStatus);
    // userDatasets.add(datasetStatusObj);
    // }

    // }
    // }

    // public void updateInstance(Instance instance) {
    // InstanceMetrics instanceMetric = null;
    // for (int i = 0; i < instanceMetrics.size(); i++) {
    // if (instanceMetrics.get(i).getInstance().getInstanceID() ==
    // instance.getInstanceID()) {
    // instanceMetric = instanceMetrics.get(i);
    // break;
    // }
    // }

    // int totalNumOfLabels =
    // instanceMetric.numberOfLabelAssignment(dataset.getAssignmentList());
    // int uniqueNumOfLabels =
    // instanceMetric.numberOfUniqueLabelAssignment(dataset.getAssignmentList());
    // int uniqueNumOfUsers =
    // instanceMetric.numberOfUniqueUsers(dataset.getAssignmentList());
    // double entropy = instanceMetric.entropy(dataset.getAssignmentList());

    // JsonObject reportObject = report.getJsonObject();
    // JsonArray instances = (JsonArray) reportObject.get("instances");
    // Iterator<JsonElement> instanceIterator = instances.iterator();
    // while (instanceIterator.hasNext()) {
    // JsonObject instanceObj = (JsonObject) (instanceIterator.next());
    // if (instanceObj.get("user_id").getAsInt() != instance.getInstanceID())
    // continue;

    // instanceObj.addProperty("total_number_of_labels", totalNumOfLabels);
    // instanceObj.addProperty("unique_number_of_labels", uniqueNumOfLabels);
    // instanceObj.addProperty("unique_number_of_users", uniqueNumOfUsers);
    // instanceObj.addProperty("entropy", entropy);

    // // most freq labels for instances -------------------------------

    // HashMap<String, Long> parameters =
    // instanceMetric.mostFreqLabelAndPerc(dataset.getAssignmentList());
    // String key = ((String[]) parameters.keySet().toArray())[0];
    // JsonObject mostFreqLabel = new JsonObject();
    // mostFreqLabel.addProperty("label_name", key);
    // mostFreqLabel.addProperty("frequency", parameters.get(key));
    // instanceObj.add("most_freq_label", mostFreqLabel);

    // // ----------------------------- most freq labels for instances

    // // list labels for instances -------------------------------------

    // JsonArray listLabels = (JsonArray) instanceObj.get("list_labels");
    // HashMap<String, Long> labelsList =
    // instanceMetric.listClassLabels(dataset.getAssignmentList());
    // String[] keys = (String[]) parameters.keySet().toArray();

    // int labelsListSize = keys.length;

    // // flush labels list array
    // Iterator<JsonElement> labelListIterator = listLabels.iterator();
    // while (labelListIterator.hasNext()) {
    // JsonObject labelListObj = (JsonObject) (labelListIterator.next());
    // listLabels.remove(labelListObj);
    // }
    // // recreate labels list
    // for (int i = 0; i < labelsListSize; i++) {
    // JsonObject labelListObject = new JsonObject();
    // labelListObject.addProperty("label_name", keys[i]);
    // labelListObject.addProperty("percentage", labelsList.get(keys[i]));
    // listLabels.add(labelListObject);
    // }

    // // -------------------------------------list labels for instances

    // }

    // }

    // public void updateDataset(Dataset dataset, User user) {
    // float completeness = this.datasetMetrics.completenessPercentage();
    // int numberOfUsers = this.datasetMetrics.numberOfUserAssigned();

    // JsonObject reportObject = report.getJsonObject();
    // JsonArray datasets = (JsonArray) reportObject.get("datasets");
    // Iterator<JsonElement> datasetIterator = datasets.iterator();
    // while (datasetIterator.hasNext()) {
    // JsonObject datasetObj = (JsonObject) (datasetIterator.next());
    // if (datasetObj.get("dataset_id").getAsInt() != dataset.getDatasetID())
    // continue;

    // datasetObj.addProperty("number_of_users", numberOfUsers);
    // datasetObj.addProperty("completeness", completeness);

    // // final instance labels part
    // JsonArray finalLabels = (JsonArray) datasetObj.get("final_instance_labels");
    // ArrayList<String> finalLabelsList = datasetMetrics.distributionInstance();
    // int finalLabelsListSize = finalLabels.size();

    // // flush final labels array
    // Iterator<JsonElement> finalLabelsIterator = finalLabels.iterator();
    // while (finalLabelsIterator.hasNext()) {
    // JsonObject finalLabelObj = (JsonObject) (finalLabelsIterator.next());
    // finalLabels.remove(finalLabelObj);
    // }
    // // recreate final labels
    // for (int i = 0; i < finalLabelsListSize; i = i + 2) {
    // JsonObject finalLabelObj = new JsonObject();
    // finalLabelObj.addProperty("instance", finalLabelsList.get(i));
    // finalLabelObj.addProperty("percentage",
    // Double.parseDouble(finalLabelsList.get(i + 1)));
    // finalLabels.add(finalLabelObj);
    // }

    // // unique number of instances for each label -------------------

    // JsonArray uniqueInstances = (JsonArray)
    // datasetObj.get("unique_instance_number_for_each_label");

    // // flush unique instance labels array
    // Iterator<JsonElement> uniqueInstanceIterator = uniqueInstances.iterator();
    // while (uniqueInstanceIterator.hasNext()) {
    // JsonObject labelObj = (JsonObject) (uniqueInstanceIterator.next());
    // uniqueInstances.remove(labelObj);
    // }

    // ArrayList<Label> labels = datasetMetrics.getDataset().getClassLabels();
    // for (int j = 0; j < labels.size(); j++) {
    // JsonObject labelObj = new JsonObject();
    // int number = datasetMetrics.numOfUniqueInstance(labels.get(j));
    // labelObj.addProperty("label_id", labels.get(j).getLabelID());
    // labelObj.addProperty("unique_instance_number", number);
    // uniqueInstances.add(labelObj);
    // }

    // // --------------------------- unique number of instances for each label

    // // find current user's userMetric
    // UserMetrics userMetric = null;
    // for (int i = 0; i < userMetrics.size(); i++) {
    // if (userMetrics.get(i).getUser().getUserID() == user.getUserID()) {
    // userMetric = userMetrics.get(i);
    // break;
    // }
    // }

    // // user completeness for datasets in report
    // // --------------------------------------------

    // JsonArray userCompleteness = (JsonArray) datasetObj.get("user_completeness");
    // Iterator<JsonElement> userCompIterator = userCompleteness.iterator();

    // if (!userCompIterator.hasNext()) {
    // for (int i = 0; i < userMetrics.size(); i++) {
    // JsonObject completenessObject = new JsonObject();
    // completenessObject.addProperty("user_id",
    // userMetrics.get(i).getUser().getUserID());
    // completenessObject.addProperty("percentage", 0);
    // userCompleteness.add(completenessObject);
    // }
    // }

    // boolean isFound = false;
    // while (userCompIterator.hasNext()) {
    // JsonObject completenessObj = (JsonObject) (userCompIterator.next());
    // if (completenessObj.get("user_id").getAsInt() == user.getUserID()) {
    // completenessObj.addProperty("percentage",
    // userMetric.datasetCompletenessPer(this.report.getJsonObject()));
    // isFound = true;
    // }
    // }
    // if (!isFound) {
    // JsonObject completenessObject = new JsonObject();
    // completenessObject.addProperty("user_id", user.getUserID());
    // completenessObject.addProperty("percentage",
    // userMetric.datasetCompletenessPer(this.report.getJsonObject()));
    // userCompleteness.add(completenessObject);
    // }

    // // -------------------------------------------- user completeness for
    // datasets
    // // in report

    // // user consistency for datasets in report ----------------------------------

    // JsonArray userConsistency = (JsonArray) datasetObj.get("user_consistency");
    // Iterator<JsonElement> userConsIterator = userConsistency.iterator();

    // if (!userConsIterator.hasNext()) {
    // for (int i = 0; i < userMetrics.size(); i++) {
    // JsonObject consistencyObject = new JsonObject();
    // consistencyObject.addProperty("user_id",
    // userMetrics.get(i).getUser().getUserID());
    // consistencyObject.addProperty("consistency_percentages", 0);
    // userConsistency.add(consistencyObject);
    // }
    // }

    // isFound = false;
    // while (userConsIterator.hasNext()) {
    // JsonObject consistencyObj = (JsonObject) (userConsIterator.next());
    // if (consistencyObj.get("user_id").getAsInt() == user.getUserID()) {
    // JsonArray users = (JsonArray) reportObject.get("users");
    // Iterator<JsonElement> userIterator = users.iterator();
    // while (userIterator.hasNext()) {
    // JsonObject userObj = (JsonObject) (userIterator.next());
    // if (userObj.get("user_id").getAsInt() == user.getUserID()) {
    // consistencyObj.addProperty("consistency_percentages",
    // userMetric.consistencyPercentagesForUser(dataset.getAssignmentList(),
    // userObj.get("consistency_percentages").getAsDouble()));
    // }
    // }
    // isFound = true;
    // }
    // }
    // if (!isFound) {
    // JsonObject consistencyObject = new JsonObject();

    // JsonArray users = (JsonArray) reportObject.get("users");
    // Iterator<JsonElement> userIterator = users.iterator();
    // while (userIterator.hasNext()) {
    // JsonObject userObj = (JsonObject) (userIterator.next());
    // if (userObj.get("user_id").getAsInt() == user.getUserID()) {
    // consistencyObject.addProperty("user_id", user.getUserID());
    // consistencyObject.addProperty("consistency_percentages",
    // userMetric.consistencyPercentagesForUser(dataset.getAssignmentList(),
    // userObj.get("consistency_percentages").getAsDouble()));
    // }
    // }
    // userConsistency.add(consistencyObject);
    // }

    // // ---------------------------------- user consistency for datasets in report

    // }

    // }

    public Dataset getDataset() {
        return this.dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        this.datasetReportMechanism = new DatasetReportMechanism(dataset);
    }

    public Report getReport() {
        return this.report;
    }

    public UserReportMechanism getUserReportMechanism() {
        return this.userReportMechanism;
    }

    public InstanceReportMechanism getInstanceReportMechanism() {
        return this.instanceReportMechanism;
    }

    public DatasetReportMechanism getDatasetReportMechanism() {
        return this.datasetReportMechanism;
    }

}
