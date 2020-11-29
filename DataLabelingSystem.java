import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.Iterator;

public class DataLabelingSystem {

    Dataset dataset = null;
    ArrayList<RandomBot> userList = null;
    
    
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
                dataset.addInstance(new Instance((String) instanceObj.get("instance"),( (Long) instanceObj.get("id")).intValue()));
            }
    
            while (labelIterator.hasNext()) {
                JSONObject labelObj = (labelIterator.next());
                dataset.addLabel(new Label(((Long) labelObj.get("label id")).intValue(), (String) labelObj.get("label text")));
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.dataset = dataset;
    }
    
    public void loadUsers(String fileName) {
        ArrayList<RandomBot> userList = new ArrayList<RandomBot>();
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
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.userList = userList;
    }

    public Dataset getDataset() {
        return this.dataset;
    }

    public ArrayList<RandomBot> getUserList() {
        return this.userList;
    }

    
    public void writeOutputFile() {
      // test
        ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
        for(int i = 0; i < assignmentList.size(); i++) {
          System.out.println(
              "Instance: " + assignmentList.get(i).getInstance().getInstanceID() + " " +
              "User: " + assignmentList.get(i).getUser().getUserID() + " " + 
              "Label: " + assignmentList.get(i).getAssignedLabel().get(0).getLabelName()
          );
        }
    }
}