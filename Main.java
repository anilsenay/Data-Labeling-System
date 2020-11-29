import java.util.ArrayList;

public class Main {

    public static void main(String args[]) {

        DataLabelingSystem DLS = new DataLabelingSystem();
        DLS.createDataset("input.json");
        DLS.loadUsers("config.json");

        // Random assignment for iteration-1

        ArrayList<Instance> instanceList = DLS.getDataset().getInstances();
        ArrayList<User> userList = DLS.getUserList();
        Dataset dataset = DLS.getDataset();

        for (int i = 0; i < instanceList.size(); i++) {
            for (int j = 0; j < userList.size(); j++) {
                RandomBot user = (RandomBot) userList.get(j);
                user.assign(dataset, instanceList.get(i));
            }
        }

        DLS.writeOutputFile();

    }
}
