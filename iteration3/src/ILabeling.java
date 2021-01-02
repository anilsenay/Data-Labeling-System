/* LabelingMechanism is a superclass of each labelingMechanism classes. 
   Each class must have assign method for different labeling types to perform assignment (for future iterations). */
public interface ILabeling {

   public void assign();

   public Assignment assign(Dataset dataset, Instance instance, User user);

}
