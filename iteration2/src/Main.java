import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {

        // Read config file and create dataset.
        DataLabelingSystem DLS = new DataLabelingSystem();
        DLS.loadConfig("config.json"); // Get user from config.json.
        DLS.handleDataset();

        ArrayList<Instance> instanceList = DLS.getDataset().getInstances();
        ArrayList<User> userList = DLS.getUserList();
        Dataset dataset = DLS.getDataset();
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();

        if (DLS.getCurrentUser() != null) {
            User user = DLS.getCurrentUser();

            // get user's old assignments
            ArrayList<Assignment> usersAssignments = new ArrayList<Assignment>();
            ArrayList<Instance> usersInstances = new ArrayList<Instance>();
            for (int z = 0; z < assignmentList.size(); z++) {
                if (assignmentList.get(z).getUser().getUserID() == user.getUserID()) {
                    usersAssignments.add(assignmentList.get(z));
                    usersInstances.add(assignmentList.get(z).getInstance());
                }
            }

            for (int i = 0; i < instanceList.size(); i++) {
                if (usersInstances.contains(instanceList.get(i)))
                    continue;
                Assignment assignment = null;
                int randomNumber = (int) (Math.random() * 100);
                if (randomNumber < user.getConsistencyCheckProbability() * 100) {

                    // if there is no previous assignment for this user
                    if (usersAssignments.size() == 0) {
                        assignment = user.assign(dataset, instanceList.get(i));
                    } else {
                        int randomAssignment = (int) (Math.random() * usersAssignments.size());
                        Instance randomInstance = usersAssignments.get(randomAssignment).getInstance();
                        assignment = user.assign(dataset, randomInstance);
                    }
                } else {
                    assignment = user.assign(dataset, instanceList.get(i));
                }

                DLS.writeOutputFile();
                ReportingMechanism.getInstance().updateReport(assignment);
            }
        }

        else {
            // Random assignment for iteration-2
            for (int i = 0; i < instanceList.size(); i++) {
                for (int j = 0; j < userList.size(); j++) {
                    Assignment assignment = null;
                    User user = null;
                    if (userList.get(j).getUserType().equalsIgnoreCase("RandomBot"))
                        user = (RandomBot) userList.get(j);
                    else if (userList.get(j).getUserType().equalsIgnoreCase("RelevanceBot"))
                        user = (RelevanceBot) userList.get(j);
                    if (user != null) {
                        int randomNumber = (int) (Math.random() * 100);
                        if (randomNumber < user.getConsistencyCheckProbability() * 100) {
                            // get user's old assignments
                            ArrayList<Assignment> usersAssignments = new ArrayList<Assignment>();
                            for (int z = 0; z < assignmentList.size(); z++) {
                                if (assignmentList.get(z).getUser().getUserID() == user.getUserID()) {
                                    usersAssignments.add(assignmentList.get(z));
                                }
                            }
                            // if there is no previous assignment for this user
                            if (usersAssignments.size() == 0) {
                                assignment = user.assign(dataset, instanceList.get(i));
                            } else {
                                int randomAssignment = (int) (Math.random() * usersAssignments.size());
                                Instance randomInstance = usersAssignments.get(randomAssignment).getInstance();
                                assignment = user.assign(dataset, randomInstance);
                            }

                        } else {
                            assignment = user.assign(dataset, instanceList.get(i));
                        }

                        // Print results to the console and log file.
                        DLS.writeOutputFile();
                        ReportingMechanism.getInstance().updateReport(assignment);
                    }

                }
            }
        }
        DLS.writeOutputFile();
    }
}