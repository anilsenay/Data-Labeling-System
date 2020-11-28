import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.Iterator;

public class Main {
    
    public static void main (String args[]) {
          
    createUsers("config.json");
    Dataset dataset = createDataset("input.json");

    if(dataset == null) {
        System.err.println("Dataset could not create");
        System.exit(1);
    } 
    
    System.out.println("Dataset id : " + dataset.getDatasetID());
    ArrayList<Instance> instanceList = dataset.getInstances();
    ArrayList<Label> labelList = dataset.getClassLabels();

    for(int i = 0; i < instanceList.size(); i++) {
        System.out.println(instanceList.get(i).getContent());
    }
    
    for(int i = 0; i < labelList.size(); i++) {
        System.out.println(labelList.get(i).getLabelName());
    }
 }
    public static void createUsers(String fileName) {
        try {
            JSONParser parser = new JSONParser();
    
            Object obj = parser.parse(new FileReader(fileName));
    
            JSONObject jsonObject = (JSONObject) obj;
    
            JSONArray userList = (JSONArray) jsonObject.get("users");
            
            @SuppressWarnings("unchecked")
            Iterator<JSONObject> userListIterator = userList.iterator();
            while (userListIterator.hasNext()) {
                
                JSONObject userObj = (userListIterator.next());
                System.out.println(userObj.get("user name")); 
                System.out.println(userObj.get("user id"));
                System.out.println(userObj.get("user type"));
                // create user
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Dataset createDataset(String fileName) {
        Dataset dataset = null;
        try {
            JSONParser parser = new JSONParser();
    
            Object obj = parser.parse(new FileReader(fileName));
    
            JSONObject jsonObject = (JSONObject) obj;
            
            int datasetId = ((Long) jsonObject.get("dataset id")).intValue();
            System.out.println(datasetId);
    
            String datasetName = (String) jsonObject.get("dataset name");
            System.out.println(datasetName);
    
            int maxNumberPerInstance = ((Long) jsonObject.get("maximum number of labels per instance")).intValue();
            System.out.println(maxNumberPerInstance);
    
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
        return dataset;
    }


}
    

 
    

 
    
