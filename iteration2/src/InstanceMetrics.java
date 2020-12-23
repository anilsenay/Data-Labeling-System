import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

// Singleton Pattern implemented.
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

    // B-1 -> Total number of label assignments.
    public int numberOfLabelAssignment(Instance instance, ArrayList<Assignment> assignmentList) {
        int count = 0;
        for (int i = 0; i < assignmentList.size(); i++) {
            if (assignmentList.get(i).getInstance().getInstanceID() == instance.getInstanceID()) {
                count++;
            }
        }
        return count;
    }

    // B-2 -> Number of unique label assignments.
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

    // B-3 -> Number of unique users.
    public int numberOfUniqueUsers(Instance instance, ArrayList<Assignment> assignmentList) {
        HashSet<Integer> uniqueUsers = new HashSet<Integer>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                uniqueUsers.add(assignmentList.get(i).getUser().getUserID());
            }
        }
        return uniqueUsers.size();
    }

    // B-4 -> Most frequent class label and percentage.
    public HashMap<String, Double> mostFreqLabelAndPerc(Instance instance, ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Double> result = new HashMap<String, Double>();
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

        long frequency = occurrences.entrySet().stream()
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

        double percentage = (double) ((frequency * 1.0 / labels.size()) * 100.0);
        percentage = (int) (percentage * 100);
        percentage = percentage / 100.0;
        result.put(label, percentage);
        return result;
    }

    // B-5 -> List class labels and percentages.
    public HashMap<String, Double> listClassLabels(Instance instance, ArrayList<Assignment> assignmentList) {
        ArrayList<String> labels = new ArrayList<String>();
        HashMap<String, Double> result = new HashMap<String, Double>();
        for (int i = 0; i < assignmentList.size(); i++) {
            if (instance.getInstanceID() == assignmentList.get(i).getInstance().getInstanceID()) {
                for (int j = 0; j < assignmentList.get(i).getAssignedLabels().size(); j++) {
                    labels.add(assignmentList.get(i).getAssignedLabels().get(j).getLabelName());
                }
            }
        }

        Map<String, Long> occurrences = labels.stream().collect(Collectors.groupingBy(w -> w, Collectors.counting()));

        for (int j = 0; j < labels.size(); j++) {
            long frequency = occurrences.get(labels.get(j));
            double percentage = (double) ((frequency * 1.0 / labels.size()) * 100.0);
            percentage = ((int) (percentage * 100) / 100.0);
            result.put(labels.get(j), percentage);
        }

        return result;
    }

    // B-6 -> Entropy.
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
        double result = 0;
        ArrayList<String> uniquelabelList = new ArrayList<String>();
        for (int j = 0; j < labels.size(); j++) {
            if (uniquelabelList.contains(labels.get(j))) {
                continue;
            }

            uniquelabelList.add(labels.get(j));

            long frequency = occurrences.get(labels.get(j));

            if (numberOfUniqueLabelAssignment(instance, assignmentList) == 1) {
                return -1;
            }

            result -= (((1.0 * frequency) / (1.0 * labels.size()))
                    * (1.0 * (Math.log(1.0 * ((1.0 * frequency) / (1.0 * labels.size()))))
                            / (1.0 * Math.log(1.0 * numberOfUniqueLabelAssignment(instance, assignmentList)))));

        }

        return result;
    }

    // Update all final labels.
    public void updateAllFinalLabels(ArrayList<Instance> instanceList, ArrayList<Assignment> assignmentList) {
        for (int i = 0; i < instanceList.size(); i++) {
            mostFreqLabelAndPerc(instanceList.get(i), assignmentList);
        }
    }
}
