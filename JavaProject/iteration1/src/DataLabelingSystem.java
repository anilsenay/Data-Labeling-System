import java.util.ArrayList;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;
import java.util.Iterator;

// Gson package is used to print output only
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

// For reading json input and creating objects, this package is used.
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/* DataLabelingSystem class for creating dataset with given instance, 
   user and label from input.json and config.json file
    */
public class DataLabelingSystem {

	private Dataset dataset = null;
	private ArrayList<User> userList = null;
	private Logger logger = Logger.getInstance();
	private String inputName, outputName;

	// Create dataset from given input file.
	public void createDataset() {
		Dataset dataset = null;

		try { // Read and parse the input file.
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(new FileReader(this.inputName));

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
				dataset.addInstance(new Instance(instanceText, instanceID));
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
			logger.error(new Date(), e.toString());
			System.exit(0);
		}

		this.dataset = dataset;
	}

	// Get users from given file and store them to JSON object.
	public void loadConfig(String fileName) {
		ArrayList<User> userList = new ArrayList<User>();

		try { // Read and parse.
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(new FileReader(fileName));

			JSONObject jsonObject = (JSONObject) obj;

			// Get users from input file and store them to the JSON Array.
			JSONArray userObjects = (JSONArray) jsonObject.get("users");

			// Get file names to the class variables.
			this.inputName = (String) jsonObject.get("input_name");
			this.outputName = (String) jsonObject.get("output_name");

			// Get users from Iterator Object and store them to JSON object.

			Iterator<JSONObject> userListIterator = userObjects.iterator();
			while (userListIterator.hasNext()) {

				JSONObject userObj = (userListIterator.next());
				int userID = ((Long) userObj.get("user id")).intValue();
				String userName = (String) userObj.get("user name");
				String userType = (String) userObj.get("user type");

				// Create users from given parameters.
				userList.add(new RandomBot(userName, userID, userType));

			}

		} catch (Exception e) {
			logger.error(new Date(), e.toString());
			System.exit(1);
		}

		this.userList = userList;
	}

	// Print created labels, instances, users and assigned instances to the output
	// file. (Whole content)
	public void writeOutputFile() {
		ArrayList<Assignment> assignmentList = dataset.getAssignmentList();
		ArrayList<Instance> instanceList = dataset.getInstances();
		ArrayList<Label> labelList = dataset.getClassLabels();

		// Take dataset information to the JSON object.
		JsonObject datasetObject = new JsonObject();
		datasetObject.addProperty("dataset id", dataset.getDatasetID());
		datasetObject.addProperty("dataset name", dataset.getDatasetName());
		datasetObject.addProperty("maximum number of labels per instance", dataset.getMaxLabelPerInstance());

		JsonArray classLabelArray = new JsonArray();

		// Take labels information to the JSON object.
		for (int j = 0; j < labelList.size(); j++) {
			JsonObject classLabelObject = new JsonObject();
			classLabelObject.addProperty("label id: ", labelList.get(j).getLabelID());
			classLabelObject.addProperty("label text: ", labelList.get(j).getLabelName());
			classLabelArray.add(classLabelObject);
		}
		datasetObject.add("class labels", classLabelArray);

		JsonArray instanceArray = new JsonArray();

		// Take instance information to the JSON object.
		for (int j = 0; j < instanceList.size(); j++) {
			JsonObject instanceObject = new JsonObject();
			instanceObject.addProperty("id: ", instanceList.get(j).getInstanceID());
			instanceObject.addProperty("instance : ", instanceList.get(j).getContent());
			instanceArray.add(instanceObject);
		}
		datasetObject.add("instances", instanceArray);

		JsonArray assignmentJSONList = new JsonArray();

		// Take assignment information to the JSON object.
		for (int j = 0; j < assignmentList.size(); j++) {
			JsonObject assignmentObject = new JsonObject();
			assignmentObject.addProperty("instance id:", assignmentList.get(j).getInstance().getInstanceID());
			JsonArray classLabelIds = new JsonArray();

			for (int i = 0; i < assignmentList.get(j).getAssignedLabels().size(); i++)
				classLabelIds.add(assignmentList.get(j).getAssignedLabels().get(i).getLabelID());

			String date = assignmentList.get(j).getFormattedTime();

			assignmentObject.add("class label ids:", classLabelIds);
			assignmentObject.addProperty("user id:", assignmentList.get(j).getUser().getUserID());
			assignmentObject.addProperty("datetime:", date);

			assignmentJSONList.add(assignmentObject);
		}
		datasetObject.add("class label assignments", assignmentJSONList);

		JsonArray userArray = new JsonArray();

		// Take users information to the JSON object.
		for (int j = 0; j < userList.size(); j++) {
			JsonObject userObject = new JsonObject();
			userObject.addProperty("user id: ", userList.get(j).getUserID());
			userObject.addProperty("user name: ", userList.get(j).getUserName());
			userObject.addProperty("user type: ", userList.get(j).getUserType());
			userArray.add(userObject);
		}
		datasetObject.add("users", userArray);

		// Write whole JSON Object to the output file.
		try (FileWriter file = new FileWriter(this.outputName)) {

			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String prettyJson = gson.toJson(datasetObject);

			file.write(prettyJson);
			file.flush();

		} catch (IOException e) {
			logger.error(new Date(), e.toString());
			System.exit(2);
		}

	}

	// Getter and setter methods for DataLabelingSystem class.
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