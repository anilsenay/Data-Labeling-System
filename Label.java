import java.util.Date;

public class Label {

    private int labelID;
    private String labelName;

    Label() {
        super();
    }

    public Label(int labelID, String labelName) {
        this.setLabelID(labelID);
        this.setLabelName(labelName);
        Logger.getInstance().print(new Date(), "[Label] INFO label: created \"" + labelName + "\" with id: " + labelID);
    }

    public int getLabelID() {
        return this.labelID;
    }

    public void setLabelID(int labelID) {
        this.labelID = labelID;
    }

    public String getLabelName() {
        return this.labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

}
