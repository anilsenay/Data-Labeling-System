public class User {

    private String userName;
    private int userID;
    private String userType;

    public User() {

    }

    public User(String userName, int userID, String userType) {
        this.userName = userName;
        this.userID = userID;
        this.userType = userType;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserType() {
        return this.userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getUserID() {
        return this.userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
