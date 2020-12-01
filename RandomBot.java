import java.util.ArrayList;
import java.util.Date;

public class RandomBot extends User {

	
    public RandomBot(String userName, int userID, String userType) {
        super(userName, userID, userType);
    }

    public RandomBot() {
        super();
    }

   public void assign(Dataset dataset, Instance instance) 
   {
	   RandomLabelingMechanism.getInstance().assign(dataset,instance,this);
   }

}
