package P2;

import java.sql.Timestamp;

public class Comment {

    //Vars
    private User user;
    private String text = "";
    private Timestamp tsp;


    //Constructors

    public Comment(String text, User user) {
        tsp = new Timestamp(System.currentTimeMillis());
        this.text = text;
        this.user = user;
    }

    public Comment(String text, User user, Timestamp tsp) {
        this.tsp = tsp;
        this.text = text;
        this.user = user;
    }

    //Methods

    public void changeText(String text) {
        this.text = text;
        tsp = new Timestamp(System.currentTimeMillis());
    }

    @Override
    public String toString() {
        return text + "/" + user.getLogin() + "/" + tsp;
    }

    public User getUser() {
        return user;
    }

    public String getText() {
        return text;
    }

    public Timestamp getTsp() {
        return tsp;
    }
}
