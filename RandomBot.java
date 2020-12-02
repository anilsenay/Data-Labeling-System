import java.util.ArrayList;
import java.util.Date;

public class RandomBot extends User {

	
    public RandomBot(String userName, int userID, String userType) {
        super(userName, userID, userType);
    }
//Performs userType specified assign process
   public void assign(Dataset dataset, Instance instance) 
   {
	   RandomLabelingMechanism.getInstance().assign(dataset,instance,this);
   }

}
