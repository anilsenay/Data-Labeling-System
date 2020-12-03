import java.util.Date;

// Label class for storing label name and id of the given label.
public class Label {
    
    // The variables labelName, labelID are created.
    private int labelID;
    private String labelName;
     
    // No arg constructor.
    Label() {
        
    }
    
    // Label object is defined with arguments.
    public Label(int labelID, String labelName) {
        this.setLabelID(labelID);
        this.setLabelName(labelName);

        // Print the created label to the log file.
        Logger.getInstance().print(new Date(), "[Label] INFO label: created \"" + labelName + "\" with id: " + labelID);
    }

    // Getter and setter methods for Label class.
    public int getLabelID() {
        return this.labelID;
    }

    public String getLabelName() {
        return this.labelName;
    }
     
    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

}
