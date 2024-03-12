package P2;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Project {

    //Vars
    public static enum status {
        Zaplanowany, Aktywny, Zakonczony
    }

    public static Map<String, Project> projects = new HashMap<>();

    private status projectStatus;
    private Map<User, Comment> comments = new HashMap<>();
    private HashSet<User> workers = new HashSet<>();
    private String name;

    private final int maxWorkers = 2;


    //Constructors
    public Project(String name, User u1) throws IOException {
        if (u1.getUserType() == User.type.MANAGER) throw new IOException("Manager cannot work on projects!");
        else if (projects.containsKey(name)) throw new IOException("This project is already created!");
        else {
            this.name = name;
            this.workers.add(u1);
            projectStatus = status.Zaplanowany;
            projects.put(name, this);
        }
    }

    public Project(String name, User u1, User u2) throws IOException {
        if (u1.getUserType() == User.type.MANAGER || u2.getUserType() == User.type.MANAGER)
            throw new IOException("Manager cannot work on projects!");
        else if (u1 == u2) throw new IOException("Cannot add two same worker to project!");
        else if (projects.containsKey(name)) throw new IOException("This project already exists!");
        else {
            this.name = name;
            this.workers.add(u1);
            this.workers.add(u2);
            projectStatus = status.Zaplanowany;
            projects.put(name, this);
        }
    }

    //Methods
    public void addWorker(User user) throws IOException {
        if (workers.size() >= maxWorkers)
            throw new IOException("Maximum amount of workers already working on this project!");
        else if (user.getUserType() == User.type.MANAGER) throw new IOException("Manager cannot work on projects!");
        else if (workers.contains(user)) throw new IOException("This worker already working on this project!");
        else {
            workers.add(user);
            DataController.modifyProject(this);
        }
    }

    public void setProjectStatus(status stat) {
        projectStatus = stat;
        DataController.modifyProject(this);
    }

    public void silentSetProjectStatus(status stat) {
        projectStatus = stat;
    }

    public void setName(String name) {
        this.name = name;
        DataController.modifyProject(this);
    }

    public void changeWorkers(HashSet<User> usr) {
        workers.clear();
        workers = usr;
        DataController.modifyProject(this);
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectStatus=" + projectStatus +
                ", workers=" + workers.toString() +
                ", name='" + name + '\'' +
                '}';
    }

    public status getProjectStatus() {
        return projectStatus;
    }

    public Map<User, Comment> getComments() {
        return comments;
    }

    public HashSet<User> getWorkers() {
        return workers;
    }

    public String getName() {
        return name;
    }

    public String commentsToString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<User, Comment> e : comments.entrySet()) {
            sb.append(e.getValue().toString() + "|");
        }
        return sb.toString();
    }

    public void silentAddComment(Comment text, User user) {
        comments.put(user, text);
    }

    public void addComment(Comment text, User user) {
        comments.put(user, text);
        DataController.modifyProject(this);
    }

    public static ArrayList<Project> searchUserProjects(User user) {

        ArrayList<Project> tmp = new ArrayList<>();

        if (user.getUserType() == User.type.MANAGER) {
            for (Map.Entry<String, Project> p : projects.entrySet()) {
                tmp.add(p.getValue());
            }
        } else {
            for (Map.Entry<String, Project> p : projects.entrySet()) {
                for (User u : p.getValue().getWorkers())
                    if (u.getName().equals(user.getName())) {
                        tmp.add(p.getValue());
                    }
            }
        }

        return tmp;
    }

    public static ArrayList<Project> searchUserProjects(User user, status pStatus) {

        ArrayList<Project> tmp = new ArrayList<>();

        if (user.getUserType() == User.type.MANAGER) {
            for (Map.Entry<String, Project> p : projects.entrySet()) {
                if (p.getValue().getProjectStatus().equals(pStatus)) tmp.add(p.getValue());
            }
        } else {
            for (Map.Entry<String, Project> p : projects.entrySet()) {
                for (User u : p.getValue().getWorkers())
                    if (u.getName() == user.getName()) {
                        tmp.add(p.getValue());
                    }
            }
        }

        return tmp;
    }

    public Comment searchUserComment(User user) {
        for (Map.Entry<User, Comment> e : comments.entrySet()) {
            if (user.getLogin().equals(e.getValue().getUser().getLogin())) {
                return e.getValue();
            }
        }
        return null;
    }

    public void changeComment(Comment com, String text) {
        com.changeText(text);
        DataController.modifyProject(this);
    }

    public boolean checkEquality(User user) {
        if (user == null) return false;
        for (User s : workers) {
            if (user.getLogin().equals(s.getLogin())) return true;
        }
        return false;
    }

}
