import java.util.ArrayList;

public class ReportingMechanism {
    private static ReportingMechanism reportingMechanism;
    private Report report = new Report();
    private Dataset dataset = null;
    private ArrayList<Dataset> allDatasets = new ArrayList<Dataset>();

    private UserReportMechanism userReportMechanism = new UserReportMechanism();
    private InstanceReportMechanism instanceReportMechanism = new InstanceReportMechanism();
    private DatasetReportMechanism datasetReportMechanism = new DatasetReportMechanism();

    // No-arg constructor
    private ReportingMechanism() {
    }

    // getInstance call for usage of object variable with singleton pattern 
    public static synchronized ReportingMechanism getInstance() {
        if (reportingMechanism == null) {
            reportingMechanism = new ReportingMechanism();
        }
        return reportingMechanism;
    }

    public void importReport(Dataset dataset, ArrayList<User> userlist) {
        report.handleReport(this);
    }

    // Updates our report with the help of reporting mechanisms
    public void updateReport(Assignment assignment) {
        userReportMechanism.updateUser(assignment.getUser(), assignment);
        instanceReportMechanism.updateInstance(assignment.getInstance());
        datasetReportMechanism.updateDataset(this.dataset, assignment.getUser());
        this.report.writeReport();
    }

    // Getters & Setters
    public Dataset getDataset() {
        return this.dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
        this.datasetReportMechanism = new DatasetReportMechanism(dataset);
        this.allDatasets.add(dataset);
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

    public void addOldDataset(Dataset dataset) {
        this.allDatasets.add(dataset);
    }

    public ArrayList<Dataset> getAllDatasets() {
        return this.allDatasets;
    }

}
