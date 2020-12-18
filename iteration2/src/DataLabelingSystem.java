import java.util.ArrayList;
import java.util.Date;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
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
	private DatasetLoader datasetLoader = new DatasetLoader();

	public void handleDataset() {
		File outputFile = new File(this.outputName);
		if (outputFile.exists()) { // dataset output u varsa
			datasetLoader.loadDataset(this);
		} else
			datasetLoader.createDataset(this);
	}

	// Get users from given file and store them to JSON object.
	public void loadConfig(String fileName) {
		ArrayList<User> userList = new ArrayList<User>();
		ArrayList<Integer> userIDs = new ArrayList<Integer>();

		try { // Read and parse.
			JSONParser parser = new JSONParser();

			Object obj = parser.parse(new FileReader(fileName));

			JSONObject jsonObject = (JSONObject) obj;

			// Get users from input file and store them to the JSON Array.
			JSONArray userObjects = (JSONArray) jsonObject.get("users");

			int currentDatasetID = ((Long) jsonObject.get("currentDatasetId")).intValue();
			JSONArray datasets = (JSONArray) jsonObject.get("datasets");
			Iterator<JSONObject> datasetIterator = datasets.iterator();

			while (datasetIterator.hasNext()) {

				JSONObject datasetObj = (datasetIterator.next());
				int datasetId = ((Long) datasetObj.get("dataset_id")).intValue();
				if (datasetId != currentDatasetID)
					continue;

				String datasetName = (String) datasetObj.get("dataset_name");
				String datasetPath = (String) datasetObj.get("path");
				this.inputName = datasetPath;
				this.outputName = datasetId + "_" + datasetName + "_output.json";

				JSONArray users = (JSONArray) datasetObj.get("users");
				Iterator<Long> usersIterator = users.iterator();
				while (usersIterator.hasNext()) {
					Long userId = usersIterator.next();
					userIDs.add(userId.intValue());
				}
			}

			// Get users from Iterator Object and store them to JSON object.
			Iterator<JSONObject> userListIterator = userObjects.iterator();
			while (userListIterator.hasNext()) {

				JSONObject userObj = (userListIterator.next());
				int userID = ((Long) userObj.get("user id")).intValue();
				if (!userIDs.contains(userID))
					continue;
				double consistencyCheckProbability = ((Double) userObj.get("ConsistencyCheckProbability")).doubleValue();
				String userName = (String) userObj.get("user name");
				String userType = (String) userObj.get("user type");

				// Create users from given parameters.
				userList.add(new RandomBot(userName, userID, userType));
				// userList.add(new RandomBot(userName, userID, userType,
				// consistencyCheckProbability));
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
			classLabelObject.addProperty("label id", labelList.get(j).getLabelID());
			classLabelObject.addProperty("label text", labelList.get(j).getLabelName());
			classLabelArray.add(classLabelObject);
		}
		datasetObject.add("class labels", classLabelArray);

		JsonArray instanceArray = new JsonArray();

		// Take instance information to the JSON object.
		for (int j = 0; j < instanceList.size(); j++) {
			JsonObject instanceObject = new JsonObject();
			instanceObject.addProperty("id", instanceList.get(j).getInstanceID());
			instanceObject.addProperty("instance", instanceList.get(j).getContent());
			instanceArray.add(instanceObject);
		}
		datasetObject.add("instances", instanceArray);

		JsonArray assignmentJSONList = new JsonArray();

		// Take assignment information to the JSON object.
		for (int j = 0; j < assignmentList.size(); j++) {
			JsonObject assignmentObject = new JsonObject();
			assignmentObject.addProperty("instance id", assignmentList.get(j).getInstance().getInstanceID());
			JsonArray classLabelIds = new JsonArray();

			for (int i = 0; i < assignmentList.get(j).getAssignedLabels().size(); i++)
				classLabelIds.add(assignmentList.get(j).getAssignedLabels().get(i).getLabelID());

			String date = assignmentList.get(j).getFormattedTime();

			assignmentObject.add("class label ids", classLabelIds);
			assignmentObject.addProperty("user id", assignmentList.get(j).getUser().getUserID());
			assignmentObject.addProperty("datetime", date);

			assignmentJSONList.add(assignmentObject);
		}
		datasetObject.add("class label assignments", assignmentJSONList);

		JsonArray userArray = new JsonArray();

		// Take users information to the JSON object.
		for (int j = 0; j < userList.size(); j++) {
			JsonObject userObject = new JsonObject();
			userObject.addProperty("user id", userList.get(j).getUserID());
			userObject.addProperty("user name", userList.get(j).getUserName());
			userObject.addProperty("user type", userList.get(j).getUserType());
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

	public String getInputName() {
		return this.inputName;
	}

	public String getOutputName() {
		return this.outputName;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public void setUserList(ArrayList<User> userList) {
		this.userList = userList;
	}

}