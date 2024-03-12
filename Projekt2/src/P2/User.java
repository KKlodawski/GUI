package P2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {

    //Vars
    public enum type {
        MANAGER, DEVELOPER
    }

    private String login, password, name, surname;
    private type userType;
    public static Map<String, User> users = new HashMap<>();


    //Constructors
    public User(String login, String password, String name, String surname, type userType) throws IOException {

        if (users.containsKey(login)) throw new IOException("This login already exists!");
        else {
            this.login = login;
            this.password = password;
            this.name = name;
            this.surname = surname;
            this.userType = userType;

            users.put(login, this);
        }
    }

    //Methods
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", userType=" + userType +
                '}';
    }

    /** Returns user login */
    public String getLogin() {
        return login;
    }

    /** Returns user password */
    public String getPassword() {
        return password;
    }

    /** Setting new user's password without sending it to file */
    public void silentSetPassword(String passwd) {
        this.password = passwd;
    }

    /** Returns user name */
    public String getName() {
        return name;
    }

    /** Setting new user's name without sending it to file */
    public void silentSetName(String n) {
        this.name = n;
    }

    /** Returns user's surname */
    public String getSurname() {
        return surname;
    }

    /** Setting new user's surname without sending it to file */
    public void silentSetSurname(String surn) {
        this.surname = surn;
    }

    /** Returns type of User */
    public type getUserType() {
        return userType;
    }

    /** Returns list of users that are developers */
    public static ArrayList<User> getDevelopers() {
        ArrayList<User> tmp = new ArrayList<>();

        for (Map.Entry<String, User> e : users.entrySet()) {
            if (e.getValue().getUserType().equals(type.DEVELOPER)) tmp.add(e.getValue());
        }
        return tmp;
    }


}
