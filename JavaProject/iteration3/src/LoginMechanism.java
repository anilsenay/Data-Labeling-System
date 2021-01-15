import java.io.FileReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoginMechanism {
  public User execute(ArrayList<User> userlist) {
    boolean isLogin = false;
    Scanner sc = new Scanner(System.in); // Create a Scanner object
    while (!isLogin) {
      System.out.print("Enter username: ");
      String username = sc.nextLine();
      String password = new String(System.console().readPassword("Enter password: "));
      if (username.length() == 0 && password.length() == 0) {
        System.out.println("Starting random mechanism...");
        return null;
      }

      if (login(username, createMd5(password))) {
        isLogin = true;
        for (int i = 0; i < userlist.size(); i++) {
          if (userlist.get(i).getUserName().equals(username)) {
            System.out.println("Login successful");
            return userlist.get(i);
          }
        }
        System.out.println("\u001B[31m" + "\nYou do not have permission to access to this dataset!\n" + "\u001B[0m");
        isLogin = false;
      } else {
        System.out.println("\u001B[31m" + "\nYour username/password do not match! Please try again.\n" + "\u001B[0m");
      }

    }
    sc.close();
    return null;
  }

  public boolean login(String username, String pass) {
    try { // Read and parse.
      JsonParser parser = new JsonParser();
      JsonElement jsonElement = parser.parse(new FileReader("config.json"));
      JsonObject jsonObject = jsonElement.getAsJsonObject();

      // Get users from input file and store them to the JSON Array.
      JsonArray userObjects = (JsonArray) jsonObject.get("users");

      // Get users from Iterator Object and store them to JSON object.
      Iterator<JsonElement> userListIterator = userObjects.iterator();
      while (userListIterator.hasNext()) {
        JsonObject userObj = (JsonObject) (userListIterator.next());
        int userID = userObj.get("user id").getAsInt();
        String userName = userObj.get("user name").getAsString();
        if (!userName.equals(username))
          continue;

        String userType = userObj.get("user type").getAsString();
        String password = userObj.get("password").getAsString();

        if (pass.equals(password))
          return true;

      }
    } catch (Exception e) {
      Logger.getInstance().error(new Date(), e.toString());
      System.exit(1);
    }

    return false;
  }

  public String createMd5(String input) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
      // digest() method is called to calculate message digest
      // of an input digest() return array of byte
      byte[] messageDigest = md.digest(input.getBytes());

      // Convert byte array into signum representation
      BigInteger no = new BigInteger(1, messageDigest);

      // Convert message digest into hex value
      String hashtext = no.toString(16);
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return null;
  }
}
