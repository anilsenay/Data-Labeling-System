import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class InstanceMetrics {

    private static InstanceMetrics instanceMetrics;

    private InstanceMetrics() {

    }

    public static synchronized InstanceMetrics getInstance() {
        if (instanceMetrics == null) {
            instanceMetrics = new InstanceMetrics();
        }
        return instanceMetrics;
    }

    // B-1
    public int numberOfLabelAssignment(Instance instance, ArrayList<Assignment> assignmentList) {
        int count = 0;
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getInstance().getInstanceID() == instance.getInstanceID()) {
                count++;
            }
        }
        return count;
    }

    // B-2
    public int numberOfUniqueLabelAssignment(Instance instance, ArrayList<Assignment> assignmentList) {
        HashSet<Integer> uniqueInstanceLabeled = new HashSet<Integer>();
        for (int i = 0; i < assignmentList.size(); i++) {
            // Instance-by-instance gitme.
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    uniqueInstanceLabeled.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelID());
                }
            }
        }
        return uniqueInstanceLabeled.size();
    }

    // B-3
    public int numberOfUniqueUsers(Instance instance, ArrayList<Assignment> assignmentList) {
        HashSet<Integer> uniqueUsers = new HashSet<Integer>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                uniqueUsers.add(assignmentList.get(i).getUser().getUserID());
            }
        }
        return uniqueUsers.size();
    }

    // B-4.1 TESTE EN KADİR(:d) OLACAK METOTLARDAN BİRİ !!!

    public HashMap<String, Long> mostFreqLabelAndPerc(Instance instance, ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Long> result = new HashMap<String, Long>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {

                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    if (assignmentList.get(i).getAssignedLabels().get(j) != null) {
                        labels.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelName());
                    }
                }
            }
        }
        Map<String, Long> occurrences = labels.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        String label = occurrences.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();

        Long frequency = occurrences.entrySet().stream()
                .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getValue();

        int size = ReportingMechanism.getInstance().getDataset().getClassLabels().size();
        Label finalLabel;

        for (int j = 0; j < size; j++) {
            if (label.equals(ReportingMechanism.getInstance().getDataset().getClassLabels().get(j).getLabelName())) {
                finalLabel = ReportingMechanism.getInstance().getDataset().getClassLabels().get(j);
                instance.setfinalLabel(finalLabel);
                break;
            }
        }

        Long percentage = ((frequency / labels.size()) * 100);
        result.put(label, percentage);
        return result;
    }

    // B-5
    public HashMap<String, Long> listClassLabels(Instance instance, ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Long> result = new HashMap<String, Long>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {

                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    labels.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelName());
                }
            }
        }

        Map<String, Long> occurrences = labels.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        for (int j = 0; j < labels.size(); j++) {
            Long frequency = occurrences.get(labels.get(j));
            Long percentage = ((frequency / labels.size()) * 100);
            result.put(labels.get(j), percentage);
        }
        return result;
    }

    // B-6
    public double entropy(Instance instance, ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {

                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    labels.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelName());
                }
            }
        }
        Map<String, Long> occurrences = labels.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));
        // occurrences.forEach((key, value) -> System.out.println(key + ":" + value));

        double result = 0;
        for (int j = 0; j < labels.size(); j++) {
            Long frequency = occurrences.get(labels.get(j));

            result -= (((1.0 * frequency) / labels.size())
                    * ((Math.log((1.0 * frequency) / labels.size())) / (1.0 * Math.log(2))));
        }
        return result;
    }

    // Update all final labels
    public void updateAllFinalLabels(ArrayList<Instance> instanceList, ArrayList<Assignment> assignmentList) {
        for (int i = 0; i < instanceList.size(); i++) {
            mostFreqLabelAndPerc(instanceList.get(i), assignmentList);
        }
    }
}
