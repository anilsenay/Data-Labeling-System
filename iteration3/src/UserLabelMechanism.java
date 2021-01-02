import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class UserLabelMechanism implements ILabeling {

    private static UserLabelMechanism userLabelingMechanism;
    private Scanner sc = new Scanner(System.in);

    // No arg constructor.
    private UserLabelMechanism() {

    }

    // getInstance method for singleton pattern.
    public static synchronized UserLabelMechanism getInstance() {

        if (userLabelingMechanism == null) {

            userLabelingMechanism = new UserLabelMechanism();
        }

        return userLabelingMechanism;
    }

    public void execute(Dataset dataset, User user) {
        ArrayList<Instance> instances = dataset.getInstances();

        int instanceListSize = instances.size();

        for (int i = 0; i < instanceListSize; i++) {
            assign(dataset, instances.get(i), user);
        }

    }

    @Override
    public void assign() {

    }

    @Override
    public Assignment assign(Dataset dataset, Instance instance, User user) {
        Assignment assignment = new Assignment(instance, user, new ArrayList<Label>());

        ArrayList<Label> labels = dataset.getClassLabels();

        int labelListSize = labels.size();

        System.out.println("\nInstance = " + "\u001B[36m" + instance.getContent() + "\u001B[0m");
        System.out.println("\nChoose one of these labels:");
        for (int j = 0; j < labelListSize; j++)
            System.out
                    .println("[ " + (j + 1) + " ] Label: " + "\u001B[32m" + labels.get(j).getLabelName() + "\u001B[0m");

        int labelIndex = Integer.MAX_VALUE;
        while (labelIndex <= 0 || labelIndex > labelListSize) {
            if (labelIndex != Integer.MAX_VALUE)
                System.out.println("\nPlease select a correct label id");
            System.out.print("\nWrite the id of label you choose: ");
            labelIndex = this.sc.nextInt();
        }
        Label label = labels.get(labelIndex - 1);
        assignment.addLabel(label);
        assignment.setAssingmentDuration(new Date().getTime() - assignment.getDateTime().getTime());
        dataset.addAssignment(assignment);
        System.out.println();
        Logger.getInstance().print(new Date(),
                "[Assignment] INFO user id:" + user.getUserID() + " " + user.getUserName() + " tagged instance id:"
                        + instance.getInstanceID() + " " + "with class label " + label.getLabelID() + ":"
                        + label.getLabelName() + " " + " instance: \"" + instance.getContent() + "\"");

        return assignment;
    }

}
