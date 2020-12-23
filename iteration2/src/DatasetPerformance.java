import java.util.ArrayList;

public class DatasetPerformance {

    private Dataset dataset;

    // Set DatasetPerformance in ReportingMechanism as current object
    public DatasetPerformance(Dataset dataset) {
        this.dataset = dataset;
        ReportingMechanism.getInstance().getDatasetReportMechanism().setDatasetPerformance(this);
    }

    // C-1 call completenessPercentage method and find completeness percentage 
    public float getCompletenessPercentage() {
        return DatasetMetrics.getInstance().completenessPercentage();
    }

    // C-2  call distributionInstance method and find class distribution based on final instance labels
    public ArrayList<String> getDistributionInstance() {
        return DatasetMetrics.getInstance().distributionInstance();
    }

    // C-3 call numOfUniqueInstanc method and find list number of unique instances for each class label
    public int getNumOfUniqueInstance(Label label) {
        return DatasetMetrics.getInstance().numOfUniqueInstance(label);
    }

    // C-4 call numberOfUserAssigned method and find number of users assigned to this dataset
    public int getNumberOfUserAssigned() {
        return DatasetMetrics.getInstance().numberOfUserAssigned();
    }

    public Dataset getDataset() {
        return this.dataset;
    }
}
