import java.util.ArrayList;
import java.util.Date;
import java.io.FileReader;
import java.util.Iterator;

// For reading json input and creating objects, this package is used.
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DatasetLoader {

  // Create dataset from given input file.
  public void createDataset(DataLabelingSystem dls) {
    Dataset dataset = null;

    try { // Read and parse the input file.
      JSONParser parser = new JSONParser();

      Object obj = parser.parse(new FileReader(dls.getInputName()));

      JSONObject jsonObject = (JSONObject) obj;

      // Get settings information of dataset.
      int datasetId = ((Long) jsonObject.get("dataset id")).intValue();

      String datasetName = (String) jsonObject.get("dataset name");

      int maxNumberPerInstance = ((Long) jsonObject.get("maximum number of labels per instance")).intValue();

      // Error handling for maximum number of labels per instance.
      if (maxNumberPerInstance < 1) {
        throw new Exception("Maximum number of labels per instance is not valid.");
      }
      // Create dataset from given parameters.
      dataset = new Dataset(datasetId, datasetName, maxNumberPerInstance);
      new DatasetPerformance(dataset);

      // Get labels and instances from input file to the JSON Array.
      JSONArray classLabelList = (JSONArray) jsonObject.get("class labels");
      JSONArray instanceList = (JSONArray) jsonObject.get("instances");

      // Main iterators.

      Iterator<JSONObject> instanceIterator = instanceList.iterator();

      Iterator<JSONObject> labelIterator = classLabelList.iterator();

      // Get instances from Iterator Object and store them to JSON object.
      while (instanceIterator.hasNext()) {
        JSONObject instanceObj = (instanceIterator.next());
        String instanceText = (String) instanceObj.get("instance");
        int instanceID = ((Long) instanceObj.get("id")).intValue();

        // Create instance from given parameters.
        Instance instance = new Instance(instanceText, instanceID);
        dataset.addInstance(instance);
        new InstancePerformance(instance);
      }

      // Get labels from Iterator Object and store them to JSON object.
      while (labelIterator.hasNext()) {
        JSONObject labelObj = (labelIterator.next());
        String labelText = (String) labelObj.get("label text");
        int labelID = ((Long) labelObj.get("label id")).intValue();

        // Create labels from given parameters.
        dataset.addLabel(new Label(labelID, labelText));
      }

    } catch (Exception e) {
      Logger.getInstance().error(new Date(), e.toString());
      System.exit(0);
    }

    dls.setDataset(dataset);
  }

  // Load dataset from output file.
  public void loadDataset(DataLabelingSystem dls) {
    Dataset dataset = null;

    try { // Read and parse the output file.
      JSONParser parser = new JSONParser();

      Object obj = parser.parse(new FileReader(dls.getOutputName()));

      JSONObject jsonObject = (JSONObject) obj;

      // Get settings information of dataset.
      int datasetId = ((Long) jsonObject.get("dataset id")).intValue();

      String datasetName = (String) jsonObject.get("dataset name");

      int maxNumberPerInstance = ((Long) jsonObject.get("maximum number of labels per instance")).intValue();

      // Error handling for maximum number of labels per instance.
      if (maxNumberPerInstance < 1) {
        throw new Exception("Maximum number of labels per instance is not valid.");
      }
      // Create dataset from given parameters.
      dataset = new Dataset(datasetId, datasetName, maxNumberPerInstance);
      new DatasetPerformance(dataset);

      // Get labels and instances from input file to the JSON Array.
      JSONArray classLabelList = (JSONArray) jsonObject.get("class labels");
      JSONArray instanceList = (JSONArray) jsonObject.get("instances");
      JSONArray assignmentList = (JSONArray) jsonObject.get("class label assignments");

      // Main iterators.
      Iterator<JSONObject> instanceIterator = instanceList.iterator();
      Iterator<JSONObject> labelIterator = classLabelList.iterator();
      Iterator<JSONObject> assignmentIterator = assignmentList.iterator();

      // Get instances from Iterator Object and store them to JSON object.
      while (instanceIterator.hasNext()) {
        JSONObject instanceObj = (instanceIterator.next());
        String instanceText = (String) instanceObj.get("instance");
        int instanceID = ((Long) instanceObj.get("id")).intValue();

        // Create instance from given parameters.
        Instance instance = new Instance(instanceText, instanceID);
        dataset.addInstance(instance);
        new InstancePerformance(instance);
      }

      // Get labels from Iterator Object and store them to JSON object.
      while (labelIterator.hasNext()) {
        JSONObject labelObj = (labelIterator.next());
        String labelText = (String) labelObj.get("label text");
        int labelID = ((Long) labelObj.get("label id")).intValue();

        // Create labels from given parameters.
        dataset.addLabel(new Label(labelID, labelText));
      }

      // Restore assignments
      while (assignmentIterator.hasNext()) {
        JSONObject assignmentObj = (assignmentIterator.next());
        int instanceId = ((Long) assignmentObj.get("instance id")).intValue();
        int userId = ((Long) assignmentObj.get("user id")).intValue();
        String date = (String) assignmentObj.get("datetime");

        // find user
        User user = null;
        int userListSize = dls.getUserList().size();
        for (int i = 0; i < userListSize; i++) {
          if (dls.getUserList().get(i).getUserID() == userId)
            user = dls.getUserList().get(i);
        }

        // find instance
        Instance instance = null;
        int instanceListSize = dataset.getInstances().size();
        for (int i = 0; i < instanceListSize; i++) {
          if (dataset.getInstances().get(i).getInstanceID() == instanceId)
            instance = dataset.getInstances().get(i);
        }

        Assignment assignment = new Assignment(instance, user, new ArrayList<Label>(), date);

        // get label ids
        ArrayList<Integer> labelIds = new ArrayList<Integer>();
        JSONArray labels = (JSONArray) assignmentObj.get("class label ids");
        Iterator<Long> labelsIterator = labels.iterator();
        while (labelsIterator.hasNext()) {
          Long id = labelsIterator.next();
          labelIds.add(id.intValue());
        }

        // find labels
        int labelIdsSize = labelIds.size();
        int labelsSize = dataset.getClassLabels().size();
        for (int i = 0; i < labelIdsSize; i++) {
          for (int j = 0; j < labelsSize; j++) {
            Label label = null;
            if (labelIds.get(i) == dataset.getClassLabels().get(j).getLabelID()) {
              label = dataset.getClassLabels().get(j);
            }
            assignment.addLabel(label);
          }
        }

        // Create labels from given parameters.
        dataset.addAssignment(assignment);
      }

    } catch (Exception e) {
      Logger.getInstance().error(new Date(), e.toString());
      System.exit(0);
    }

    dls.setDataset(dataset);
  }
}