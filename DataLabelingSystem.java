import java.util.ArrayList;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DataLabelingSystem {

    private Dataset dataset = null;
    private ArrayList<User> userList = null;
    private Logger logger = Logger.getInstance();

    public void createDataset(String fileName) {
        Dataset dataset = null;

        try {
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(fileName));

            JSONObject jsonObject = (JSONObject) obj;

            int datasetId = ((Long) jsonObject.get("dataset id")).intValue();

            String datasetName = (String) jsonObject.get("dataset name");

            int maxNumberPerInstance = ((Long) jsonObject.get("maximum number of labels per instance")).intValue();

            dataset = new Dataset(datasetId, datasetName, maxNumberPerInstance);

            JSONArray classLabelList = (JSONArray) jsonObject.get("class labels");
            JSONArray instanceList = (JSONArray) jsonObject.get("instances");

            @SuppressWarnings("unchecked")
            Iterator<JSONObject> instanceIterator = instanceList.iterator();
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> labelIterator = classLabelList.iterator();

            while (instanceIterator.hasNext()) {
                JSONObject instanceObj = (instanceIterator.next());
                String instanceText = (String) instanceObj.get("instance");
                int instanceID = ((Long) instanceObj.get("id")).intValue();
                logger.print(new Date(),
                        "[Instance] INFO instance: created \"" + instanceText + "\" with id: " + instanceID);
                dataset.addInstance(new Instance(instanceText, instanceID));
            }

            while (labelIterator.hasNext()) {
                JSONObject labelObj = (labelIterator.next());
                String labelText = (String) labelObj.get("label text");
                int labelID = ((Long) labelObj.get("label id")).intValue();
                logger.print(new Date(), "[Label] INFO label: created \"" + labelText + "\" with id: " + labelID);
                dataset.addLabel(new Label(labelID, labelText));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dataset = dataset;
    }

    public void loadUsers(String fileName) {
        ArrayList<User> userList = new ArrayList<User>();
        try {
            JSONParser parser = new JSONParser();

            Object obj = parser.parse(new FileReader(fileName));

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray userObjects = (JSONArray) jsonObject.get("users");

            @SuppressWarnings("unchecked")
            Iterator<JSONObject> userListIterator = userObjects.iterator();
            while (userListIterator.hasNext()) {

                JSONObject userObj = (userListIterator.next());
                int userID = ((Long) userObj.get("user id")).intValue();
                String userName = (String) userObj.get("user name");
                String userType = (String) userObj.get("user type");

                userList.add(new RandomBot(userName, userID, userType));
                logger.print(new Date(), "[User] INFO user: created " + userName + " as " + userType);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        this.userList = userList;
    }

    public void writeOutputFile(String fileName) {
        // test
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        ArrayList<Instance> instanceList = dataset.getInstances();
        ArrayList<Label> labelList = dataset.getClassLabels();
        

        JSONObject datasetObject = new JSONObject();
        datasetObject.put("dataset id", dataset.getDatasetID());
        datasetObject.put("dataset name", dataset.getDatasetName());
        datasetObject.put("maximum number of labels per instance", dataset.getMaxLabelPerInstance());

        JSONArray classLabelArray = new JSONArray();

        for (int j = 0; j < labelList.size(); j++) {
            JSONObject classLabelObject = new JSONObject();
            classLabelObject.put("label id: ", labelList.get(j).getLabelID());
            classLabelObject.put("label text: ", labelList.get(j).getLabelName());
            classLabelArray.add(classLabelObject);
        }
        datasetObject.put("class labels", classLabelArray);

        JSONArray instanceArray = new JSONArray();

        for (int j = 0; j < instanceList.size(); j++) {
            JSONObject instanceObject = new JSONObject();
            instanceObject.put("id: ", instanceList.get(j).getInstanceID());
            instanceObject.put("instance : ", instanceList.get(j).getContent());
            instanceArray.add(instanceObject);
        }
        datasetObject.put("instances", instanceArray);

        JSONArray assignmentJSONList = new JSONArray();

        for (int j = 0; j < assignmentList.size(); j++) {
            JSONObject assignmentObject = new JSONObject();
            assignmentObject.put("instance id:", assignmentList.get(j).getInstance().getInstanceID());
            JSONArray classLabelIds = new JSONArray();

            for (int i = 0; i < assignmentList.get(j).getAssignedLabels().size(); i++)                
            	classLabelIds.add(assignmentList.get(j).getAssignedLabels().get(i).getLabelID());
            
            String date = assignmentList.get(j).getFormattedTime();
            String formattedDate = date.replace('/','.');
   
            assignmentObject.put("class label ids:", classLabelIds);
            assignmentObject.put("user id:", assignmentList.get(j).getUser().getUserID());
            assignmentObject.put("datetime:", formattedDate);
            

            assignmentJSONList.add(assignmentObject);
        }
        datasetObject.put("class label assignments", assignmentJSONList);

        JSONArray userArray = new JSONArray();

        for (int j = 0; j < userList.size(); j++) {
            JSONObject userObject = new JSONObject();
            userObject.put("user id: ", userList.get(j).getUserID());
            userObject.put("user name: ", userList.get(j).getUserName());
            userObject.put("user type: ", userList.get(j).getUserType());
            userArray.add(userObject);
        }
        datasetObject.put("users", userArray);
        
        
        
        
        
        

        try (FileWriter file = new FileWriter(fileName)) {

            file.write(datasetObject.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < assignmentList.size(); i++) {
            System.out.println("Instance: " + assignmentList.get(i).getInstance().getInstanceID() + " " + "User: "
                    + assignmentList.get(i).getUser().getUserID() + " " + "Label: "
                    + assignmentList.get(i).getAssignedLabels().get(0).getLabelName());
        }
    }

    public Dataset getDataset() {
        return this.dataset;
    }

    public ArrayList<User> getUserList() {
        return this.userList;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public void setUserList(ArrayList<User> userList) {
        this.userList = userList;
    }

}