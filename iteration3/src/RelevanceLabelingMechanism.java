import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class RelevanceLabelingMechanism implements ILabeling {

    private static RelevanceLabelingMechanism relevanceLabelingMechanism;

    private RelevanceLabelingMechanism() {

    }

    public static synchronized RelevanceLabelingMechanism getInstance() {

        if (relevanceLabelingMechanism == null) {

            relevanceLabelingMechanism = new RelevanceLabelingMechanism();
        }

        return relevanceLabelingMechanism;
    }

    @Override
    public void assign() {
    }

    public Assignment assign(Dataset dataset, Instance instance, User user) {
        Assignment assignment = new Assignment(instance, user, new ArrayList<Label>());

        ArrayList<Label> assignedLabels = new ArrayList<Label>();
        ArrayList<Label> labels = dataset.getClassLabels();

        String parseInstance[] = instance.getContent().replaceAll("[^\\dA-Za-z ]", "").split(" ");

        for (int i = 0; i < parseInstance.length; i++) {
            // System.out.println("parse: " + parseInstance[i]);
            ArrayList<String> relevants = getRelevants(parseInstance[i]);
            // System.out.println("RELEVANT:" + relevants.size());
            if (relevants.size() == 0)
                continue;
            for (int j = 0; j < labels.size(); j++) {
                for (int j2 = 0; j2 < relevants.size(); j2++) {
                    // System.out.print(relevants.get(j2) + "|");
                    if (relevants.get(j2).equalsIgnoreCase(labels.get(j).getLabelName())) {
                        assignedLabels.add(labels.get(j));
                        System.out.println(parseInstance[i] + "-> " + labels.get(j).getLabelName());
                    }

                }
                // System.out.println();
            }
        }
        System.out.println("Before occurrences");
        Map<Label, Long> occurrences = assignedLabels.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        System.out.println("Before sortedOccurrences");
        // Map<Label, Long> sortedOccurrences = new TreeMap<Label, Long>(occurrences);
        Map<Label, Long> sortedOccurrences = occurrences.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        System.out.println("KEYSET LENGTH " + sortedOccurrences.keySet().toArray().length);

        for (int i = 0; i < dataset.getMaxLabelPerInstance(); i++) {
            // Label label = (Label) sortedOccurrences.keySet().toArray()[i];
            System.out.println(sortedOccurrences.keySet().toArray().length);
            if (sortedOccurrences.size() >= i && sortedOccurrences.keySet().toArray().length > 0) {
                assignment.addLabel((Label) sortedOccurrences.keySet().toArray()[i]);
                Logger.getInstance().print(new Date(),
                        "[Assignment] INFO user id:" + user.getUserID() + " " + user.getUserName()
                                + " tagged instance id:" + instance.getInstanceID() + " " + "with class label "
                                + ((Label) sortedOccurrences.keySet().toArray()[i]).getLabelID() + ":"
                                + ((Label) sortedOccurrences.keySet().toArray()[i]).getLabelName() + " "
                                + " instance: \"" + instance.getContent() + "\"");
            }
        }

        // Rule Based Assignment.
        if (assignment.getAssignedLabels().size() == 0) {
            for (Label label : labels) {
                if (label.getLabelName().equals("Notr")) {
                    assignedLabels.add(label);
                    assignment.addLabel(label);
                }
            }
        }
        if (assignment.getAssignedLabels().size() > 0) {
            assignment.setAssingmentDuration(new Date().getTime() - assignment.getDateTime().getTime());
            dataset.addAssignment(assignment);
        }
        return assignment;
    }

    public ArrayList<String> getRelevants(String word) {
        // System.out.println("WORDD::" + word);
        int responseCode = 0;
        ArrayList<String> words = new ArrayList<String>();
        try {
            String res = "";
            URL url = new URL("https://api.datamuse.com/words?ml=" + word + "&max=1000");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                Scanner sc = new Scanner(url.openStream());
                while (sc.hasNext()) {
                    res += sc.nextLine();
                }
                // jsonArray keeps relevant words
                JsonArray jsonArray = new Gson().fromJson(res, JsonArray.class);
                int jsonArraySize = jsonArray.size();
                for (int i = 0; i < jsonArraySize; i++) {
                    JsonObject obj = (JsonObject) jsonArray.get(i);
                    words.add(obj.get("word").getAsString());
                }
                sc.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("HttpResponseCode: " + responseCode);
        }
        return words;
    }

}