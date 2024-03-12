package P2;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public abstract class DataController {

    //Vars
    private static FileWriter fw;
    private static BufferedReader fr;

    /**
     * Sending string to log file
     */
    public static void sendLog(String text) {
        try {
            fw = new FileWriter("log.txt", true);
            fw.write(text + " || " + new Timestamp(System.currentTimeMillis()) + "\n");
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Sending log to another file */
    public static void sendLog(String text, String file) {
        try {
            fw = new FileWriter(file, true);
            fw.write(text + " || " + new Timestamp(System.currentTimeMillis()) + "\n");
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Sending user data to file with users */
    public static void sendUser(User user) {
        try {
            fw = new FileWriter("User.txt", true);
            fw.write(user.getLogin() + "|" + user.getPassword() + "|" + user.getName() + "|" + user.getSurname() + "|" + user.getUserType().toString() + "\n");
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Change of user data */
    public static void modifyUser(User user) {
        try {
            System.out.println(user);
            File usrFile = new File("User.txt");
            File usrTmpFile = new File("UserTmp.txt");
            fw = new FileWriter(usrTmpFile);
            fr = new BufferedReader(new FileReader(usrFile));

            String line;
            while ((line = fr.readLine()) != null) {
                String[] vars = line.split("[|]");
                if (vars[0].equals(user.getLogin())) {
                    continue;
                }
                fw.write(line + "\n");
            }
            fw.close();
            fr.close();
            usrFile.delete();
            usrTmpFile.renameTo(usrFile);
            sendUser(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Loads users from files to program */
    public static void loadUsers() {
        User.users.clear();
        try {
            fr = new BufferedReader(new FileReader("User.txt"));
            String data;
            while ((data = fr.readLine()) != null) {
                String[] vars = data.split("[|]");
                User tmp = new User(vars[0], vars[1], vars[2], vars[3], User.type.valueOf(vars[4]));
                User.users.put(vars[0], tmp);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sending project to file with projects
     */
    public static void sendProject(Project proj) {
        try {
            fw = new FileWriter("Projects.txt", true);
            ArrayList<User> usr = new ArrayList<>();
            for (User u : proj.getWorkers()) {
                System.out.println(u);
                if (u != null) usr.add(u);
            }
            if (usr.size() == 1) {
                fw.write(proj.getName() + "|" +
                        proj.getProjectStatus().toString() + "|" +
                        usr.get(0).getLogin() + "|null|" +
                        proj.commentsToString() + "\n"
                );
            } else fw.write(proj.getName() + "|" +
                    proj.getProjectStatus().toString() + "|" +
                    usr.get(0).getLogin() + "|" + usr.get(1).getLogin() + "|" +
                    proj.commentsToString() + "\n"
            );
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change of project data
     */
    public static void modifyProject(Project proj) {
        try {
            System.out.println(proj);
            fw = new FileWriter("ProjectsTmp.txt");
            fr = new BufferedReader(new FileReader("Projects.txt"));
            String line;
            while ((line = fr.readLine()) != null) {
                String[] vars = line.split("[|]");
                if (vars[0].equals(proj.getName())) {
                    continue;
                }
                fw.write(line + "\n");
            }
            fr.close();
            fw.close();
            File tmp = new File("Projects.txt");
            tmp.delete();
            new File("ProjectsTmp.txt").renameTo(tmp);
            sendProject(proj);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /** Load projects from file to program */
    public static void loadPojects() {
        Project.projects.clear();
        System.out.println("E");
        try {
            fr = new BufferedReader(new FileReader("Projects.txt"));
            String data;
            while ((data = fr.readLine()) != null) {
                Project tmp;
                ArrayList<String> vars = new ArrayList<>();
                vars.addAll(List.of(data.split("[|]")));
                if (vars.get(3).equals("null")) {
                    tmp = new Project(vars.get(0), User.users.get(vars.get(2)));
                } else {
                    tmp = new Project(vars.get(0), User.users.get(vars.get(2)), User.users.get(vars.get(3)));
                }
                tmp.silentSetProjectStatus(Project.status.valueOf(vars.get(1)));
                if (vars.size() > 4) {
                    for (int i = 4; i <= vars.size() - 1; i++) {
                        String[] stmp;
                        stmp = vars.get(i).split("[/]");

                        //System.out.println(User.users.get(stmp[1]));
                        //System.out.println(stmp[1]);
                        tmp.silentAddComment(
                                new Comment(stmp[0], User.users.get(stmp[1]), Timestamp.valueOf(stmp[2])),
                                User.users.get(stmp[1]));
                    }
                }
                Project.projects.put(vars.get(0), tmp);
            }
            fr.close();
            System.out.println(Project.projects);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
