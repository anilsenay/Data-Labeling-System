import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class InstanceMetrics {

    private Instance instance;

    public InstanceMetrics(Instance instance) {
        this.instance = instance;
    }

    // B-1
    public int numberOfLabelAssignment(ArrayList<Assignment> assignmentList) {
        int count = 0;
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getInstance().getInstanceID() == this.instance.getInstanceID()) {
                count++;
            }
        }
        return count;
    }

    // B-2
    public int numberOfUniqueLabelAssignment(ArrayList<Assignment> assignmentList) {
        HashSet<Integer> uniqueInstanceLabeled = new HashSet<Integer>();
        for (int i = 0; i < assignmentList.size(); i++) {
            // Instance-by-instance gitme.
            if (this.instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    uniqueInstanceLabeled.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelID());
                }
            }
        }
        return uniqueInstanceLabeled.size();
    }

    // B-3
    public int numberOfUniqueUsers(ArrayList<Assignment> assignmentList) {
        HashSet<Integer> uniqueUsers = new HashSet<Integer>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (this.instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                uniqueUsers.add(assignmentList.get(i).getUser().getUserID());
            }
        }
        return uniqueUsers.size();
    }

    // B-4.1 TESTE EN KADİR(:d) OLACAK METOTLARDAN BİRİ !!!
    public HashMap<String, Long> mostFreqLabelAndPerc(ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Long> result = new HashMap<String, Long>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (this.instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {

                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    labels.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelName());
                }

                Map<String, Long> occurrences = labels.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()));
                String label = occurrences.entrySet().stream()
                        .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
                Long frequency = occurrences.entrySet().stream()
                        .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();

                Long percentage = ((frequency / labels.size()) * 100);
                result.put(label, percentage);

            }

        }
        return result;
    }

    // 5-B
    public void listClassLabels() {
    }

    // 6-B
    public double entropy() {

        return 0;

    }

    public Instance getInstance() {
        return this.instance;
    }

}
