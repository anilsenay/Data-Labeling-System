import java.util.ArrayList;

public class RandomBot extends User implements Assign {

<<<<<<< HEAD
    Dataset dataset;

=======
>>>>>>> 121017b35ae0ae21fefd5484dcfc996faf02e0f0
    public RandomBot(String userName, int userID, String userType) {
        super(userName, userID, userType);
    }

    public RandomBot() {
        super();
    }

<<<<<<< HEAD
    //Method below assigns labels to an instance by maximum of maxLabelPerInstance
=======

    // Method below assigns labels to an instance by maximum of maxLabelPerInstance
>>>>>>> 121017b35ae0ae21fefd5484dcfc996faf02e0f0
    // then returns to assignmentList in dataset
    @Override
    public void assign(Dataset dataset, Instance instance) {

<<<<<<< HEAD
        //getting a random value between 1 and max labels per instance
        int maxLabelRandom= (int) (1+ (Math.random() * dataset.maxLabelPerInstance));

            ArrayList<Label> labels= new ArrayList<Label>(); //creates a local label arraylist to store labels to assign
            //Chooses a random label from classLabel arraylist in dataset
            for (int j=0; j< maxLabelRandom; j++){
            Label getRandomLabel = dataset.classLabels.get((int)((Math.random() * dataset.classLabels.size())));
            labels.add(getRandomLabel);
            }
            //returns to assignmentList in dataset
            Assignment assignment = new Assignment(instance, this, labels);
            dataset.addAssignment(assignment);
=======
        // getting a random value between 1 and max labels per instance
        int maxLabelRandom = (int) (1 + (Math.random() * dataset.maxLabelPerInstance));

        ArrayList<Label> labels = new ArrayList<Label>(); // creates a local label arraylist to store labels to assign
        // Chooses a random label from classLabel arraylist in dataset
        for (int j = 0; j < maxLabelRandom; j++) {
            Label getRandomLabel = dataset.classLabels.get((int) ((Math.random() * dataset.classLabels.size())));
            labels.add(getRandomLabel);
        }
        // returns to assignmentList in dataset
        Assignment assignment = new Assignment(instance, this, labels);
        dataset.addAssignment(assignment);
>>>>>>> 121017b35ae0ae21fefd5484dcfc996faf02e0f0

    }

}
