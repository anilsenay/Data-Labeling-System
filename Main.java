import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.util.Iterator;

public class Main {
  public static void main (String args[]) {
      

    JSONParser parser = new JSONParser();
    try {
        Object obj = parser.parse(new FileReader("input.json"));

        JSONObject jsonObject = (JSONObject) obj;
        
        String datasetName = (String) jsonObject.get("dataset name");
        System.out.println(datasetName);

        Long maxNumberPerInstance = (Long) jsonObject.get("maximum number of labels per instance");
        System.out.println(maxNumberPerInstance);

        JSONArray classLabelList = (JSONArray) jsonObject.get("class labels");
        JSONArray instanceList = (JSONArray) jsonObject.get("instances");

        Iterator<JSONObject> instanceIterator = instanceList.iterator();
        Iterator<JSONObject> labelIterator = classLabelList.iterator();

        while (instanceIterator.hasNext()) {
            JSONObject instanceObj = (instanceIterator.next());
            System.out.println(instanceObj.get("instance")); 
            System.out.println(instanceObj.get("id"));
        }

        while (labelIterator.hasNext()) {
            JSONObject labelObj = (labelIterator.next());
            System.out.println(labelObj.get("label text")); 
            System.out.println(labelObj.get("label id"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}





 }
    // try(FileReader reader = new FileReader("input.json") {

    // } catch (FileNotFoundException e) {
    //     e.printStackTrace();
    