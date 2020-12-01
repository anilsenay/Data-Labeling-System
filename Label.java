public class Label {

    private int labelID;
    private String labelName;

    Label() {

    }

    public Label(int labelID, String labelName) {
        this.setLabelID(labelID);
        this.setLabelName(labelName);
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
