import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {

        // Read config file and create dataset.
        DataLabelingSystem DLS = new DataLabelingSystem();
        DLS.loadConfig("config.json"); // Get user from config.json.
        DLS.handleDataset();

        // Random assignment for iteration-1.
        ArrayList<Instance> instanceList = DLS.getDataset().getInstances();
        ArrayList<User> userList = DLS.getUserList();
        Dataset dataset = DLS.getDataset();
        ArrayList<Label> labelList = DLS.getDataset().getClassLabels();
        ArrayList<Assignment> assignmentList = DLS.getDataset().getAssignmentList();

        // we should handle dataset status here, is it restored or newly created

        for (int i = 0; i < instanceList.size(); i++) {
            for (int j = 0; j < userList.size(); j++) {
                if (userList.get(j).getUserType().equalsIgnoreCase("RandomBot")) {
                    RandomBot user = (RandomBot) userList.get(j);
                    Assignment assignment = user.assign(dataset, instanceList.get(i));
                    // Print results to the console and log file.
                    // DLS.writeOutputFile();
                    ReportingMechanism.getInstance().updateReport(assignment);
                }
            }
        }
        DLS.writeOutputFile();
    }
}