import java.util.ArrayList;

public class DatasetPerformance {

    private Dataset dataset;

    public DatasetPerformance(Dataset dataset) {
        this.dataset = dataset;
        ReportingMechanism.getInstance().getDatasetReportMechanism().setDatasetPerformance(this);
    }

    // C-1
    public float getCompletenessPercentage() {
        return DatasetMetrics.getInstance().completenessPercentage();
    }

    // C-2
    public ArrayList<String> getDistributionInstance() {
        return DatasetMetrics.getInstance().distributionInstance();
    }

    // C-3
    public int getNumOfUniqueInstance(Label label) {
        return DatasetMetrics.getInstance().numOfUniqueInstance(label);
    }

    // C-4 Bomb Has Been Planted
    public int getNumberOfUserAssigned() {
        return DatasetMetrics.getInstance().numberOfUserAssigned();
    }

    public Dataset getDataset() {
        return this.dataset;
    }
}
