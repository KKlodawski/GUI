package P2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class FunctionalWindow extends JFrame implements Runnable {

    //Vars
    private User activeUser;
    private Project selectedProj;

    private JPanel
            mainPanel = new JPanel(),
            projectPanel = new JPanel(),
            projectDataPanel = new JPanel(),
            jScroll = new JPanel(),
            pdpStatus = new JPanel();
    private JScrollPane
            projectListPanel = new JScrollPane(jScroll);
    private JMenu
            options = new JMenu("Options");
    private JMenuItem
            logoutItem = new JMenuItem("Logout"),
            userDataItem,
            createProject = new JMenuItem("Create"),
            checkData = new JMenuItem("Dane");
    private JMenuBar
            jb = new JMenuBar();
    private JComboBox
            filtrProj = new JComboBox(),
            dev1 = new JComboBox<>(),
            dev2 = new JComboBox<>(),
            commentList = new JComboBox();
    private JComboBox<Project.status>
            status = new JComboBox();
    private JTextField
            name = new JTextField(),
            currentComment = new JTextField();
    private JLabel
            namePDP = new JLabel("Nazwa projektu: "),
            statusPDP = new JLabel("Status projektu: "),
            dev1PDP = new JLabel("Pierwszy developer: "),
            dev2PDP = new JLabel("Drugi developer: "),
            commentPDP = new JLabel("Komentarz: "),
            commentListPDP = new JLabel("Lista komentarzy: "),
            commentTimePDP = new JLabel("Godzina wpisu: "),
            commentTime = new JLabel(""),
            filtrProjL = new JLabel("Filtr: ");
    private JButton
            save = new JButton("Save"),
            addComment = new JButton("Dodaj komentarz");
    private ButtonGroup
            statusG = new ButtonGroup();
    private JRadioButton
            startB = new JRadioButton("Zacznij"),
            stopB = new JRadioButton("Zakończ");

    private ArrayList<JButton> projsB = new ArrayList<>();
    private StringBuilder commentSB;
    private long timer = System.currentTimeMillis();
    private Thread thread = new Thread(this);

    //Constructors

    public FunctionalWindow(User activeUser) throws HeadlessException {
        this.activeUser = activeUser;

        userDataItem = new JMenuItem("Dane: " + activeUser.getName() + " " + activeUser.getSurname() + " Stanowisko: " + activeUser.getUserType().toString());

        thread.start();

        jb.add(userDataItem);

        if (activeUser.getUserType().equals(User.type.MANAGER)) {
            options.add(createProject);
        }
        options.add(checkData);
        options.add(logoutItem);


        this.setLayouts();
        this.addBlankProjectData();
        this.setVisuals();
        this.printProjects();
        jb.add(options);
        this.listeners();

        mainPanel.add(projectListPanel, BorderLayout.WEST);
        mainPanel.add(projectPanel);
        this.setJMenuBar(jb);
        this.setContentPane(mainPanel);
        setSize(1080, 720);

        setResizable(false);

        setVisible(true);
    }


    //Methods

    /** Prints list of projects that active user is assigned to */
    private void printProjects() {
        DataController.loadPojects();
        ArrayList<Project> projs = Project.searchUserProjects(activeUser);
        System.out.println(activeUser.getLogin());
        System.out.println(projs.size());
        if (activeUser.getUserType().equals(User.type.MANAGER)) {
            filtrProj.setPreferredSize(new Dimension(100, 30));
            filtrProj.setMaximumSize(new Dimension(100, 30));
            filtrProj.setMinimumSize(new Dimension(100, 30));
            filtrProj.setModel(new DefaultComboBoxModel<>(Project.status.values()));
            filtrProj.addItem("All");
            filtrProj.setSelectedItem("All");
            jb.add(filtrProjL);
            jb.add(filtrProj);

        }

        for (Project p : projs) {
            printSingleProj(p);
        }
        filtrProj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jScroll.removeAll();
                projsB.removeAll(projsB);
                for (Project p : projs) {
                    if (filtrProj.getSelectedItem().toString().equals("All")) printSingleProj(p);
                    else if (Project.status.valueOf(filtrProj.getSelectedItem().toString()).equals(p.getProjectStatus()))
                        printSingleProj(p);
                    SwingUtilities.invokeLater(
                            () -> {
                                jScroll.repaint();
                                projectListPanel.repaint();
                                mainPanel.repaint();
                                repaint();
                            }
                    );
                    timer = System.currentTimeMillis();
                }
            }
        });
    }

    /** Prints a single given project to project panel */
    private void printSingleProj(Project p) {
        //System.out.println(p);
        JButton tmp = new JButton(p.getName());
        tmp.setPreferredSize(new Dimension(200, 60));
        tmp.setMinimumSize(new Dimension(200, 60));
        tmp.setMaximumSize(new Dimension(200, 60));

        tmp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(e.getActionCommand());
                System.out.println(Project.projects.get(e.getActionCommand()));
                selectedProj = Project.projects.get(e.getActionCommand());
                setProjectData();
                timer = System.currentTimeMillis();
            }
        });
        jScroll.add(tmp);
        projsB.add(tmp);
    }

    /** Setting layouts of panels */
    private void setLayouts() {
        mainPanel.setLayout(new BorderLayout());
        projectDataPanel.setLayout(new GridBagLayout());
        jScroll.setLayout(new BoxLayout(jScroll, BoxLayout.Y_AXIS));
        projectPanel.setLayout(new BorderLayout());
        pdpStatus.setLayout(new GridLayout(1, 7));
    }

    /** Sets visual data of objects */
    private void setVisuals() {
        projectDataPanel.setBackground(Color.GRAY);
        jScroll.setBackground(Color.LIGHT_GRAY);
        pdpStatus.setBackground(Color.GRAY);
        startB.setBackground(Color.GRAY);
        stopB.setBackground(Color.GRAY);

        projectListPanel.setPreferredSize(new Dimension(200, 720));

        projectListPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        projectListPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /** Adds listeners to objects */
    private void listeners() {
        //Logout menu Item listener
        logoutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(activeUser.getLogin() + " wylogowano");
                DataController.sendLog(activeUser.getLogin() + " wylogowano");
                thread.interrupt();
                SwingUtilities.invokeLater(
                        () -> new LoginWindow()
                );
                dispose();
            }
        });

        //User data Item listener
        checkData.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(
                        () -> new UserDataWindow(activeUser)
                );
                thread.interrupt();
                dispose();
            }
        });

        //Create project listener
        createProject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(
                        () -> {
                            new AddProjectWindow(activeUser);
                        }

                );
                thread.interrupt();
                dispose();
            }
        });

        //Start button listener
        startB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer = System.currentTimeMillis();
                if (selectedProj != null) {
                    DataController.sendLog(activeUser.getLogin() + " zaczal prace nad " + selectedProj.getName());
                    DataController.sendLog(activeUser.getLogin() + " zaczal prace nad " + selectedProj.getName(), "ProjectActivity.txt");
                    PopUpController.pop("Rozpoczeto prace nad " + selectedProj.getName());
                }
            }
        });

        //Stop button listener
        stopB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer = System.currentTimeMillis();
                if (selectedProj != null) {
                    DataController.sendLog(activeUser.getLogin() + " zakończyl prace nad " + selectedProj.getName());
                    DataController.sendLog(activeUser.getLogin() + " zakończyl prace nad " + selectedProj.getName(), "ProjectActivity.txt");
                    PopUpController.pop("Zakonczono prace nad " + selectedProj.getName());
                }
            }
        });


        //Window close listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataController.sendLog(activeUser.getLogin() + " wylogowano");
                thread.interrupt();
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });

        //Status comboBox listener
        status.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer = System.currentTimeMillis();
            }
        });

        //First developer comboBox listener
        dev1.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer = System.currentTimeMillis();
            }
        });

        //Second developer comboBox listener
        dev2.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer = System.currentTimeMillis();
            }
        });

        //CommentList comboBox listener
        commentList.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer = System.currentTimeMillis();
            }
        });

        //Options jMenu listener
        options.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //currentComment textField listener
        currentComment.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                timer = System.currentTimeMillis();
            }

            @Override
            public void focusLost(FocusEvent e) {
                timer = System.currentTimeMillis();
            }
        });
    }

    /** Adds element to project panel with blank data */
    void addBlankProjectData() {

        name.setPreferredSize(new Dimension(700, 40));
        status.setPreferredSize(new Dimension(700, 40));
        dev1.setPreferredSize(new Dimension(700, 40));
        dev2.setPreferredSize(new Dimension(700, 40));
        currentComment.setPreferredSize(new Dimension(700, 40));
        commentTime.setPreferredSize(new Dimension(700, 40));
        commentList.setPreferredSize(new Dimension(700, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        projectDataPanel.add(namePDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(statusPDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(dev1PDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(dev2PDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(commentListPDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(commentPDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(commentTimePDP, gbc);
        gbc.gridy++;
        projectDataPanel.add(addComment, gbc);
        gbc.gridy = 0;
        gbc.gridx++;
        projectDataPanel.add(name, gbc);
        gbc.gridy++;
        projectDataPanel.add(status, gbc);
        gbc.gridy++;
        projectDataPanel.add(dev1, gbc);
        gbc.gridy++;
        projectDataPanel.add(dev2, gbc);
        gbc.gridy++;
        projectDataPanel.add(commentList, gbc);
        gbc.gridy++;
        projectDataPanel.add(currentComment, gbc);
        gbc.gridy++;
        projectDataPanel.add(commentTime, gbc);
        gbc.gridy++;
        projectDataPanel.add(save, gbc);

        statusG.add(startB);
        statusG.add(stopB);

        pdpStatus.add(startB);
        pdpStatus.add(stopB);
        pdpStatus.add(new JLabel(""));
        pdpStatus.add(new JLabel(""));
        pdpStatus.add(new JLabel(""));
        pdpStatus.add(new JLabel(""));
        pdpStatus.add(new JLabel(""));

        projectPanel.add(projectDataPanel, BorderLayout.CENTER);
        projectPanel.add(pdpStatus, BorderLayout.NORTH);

    }

    /** Sets project data elements with correct data */
    void setProjectData() {

        //Clearing previous data and listeners
        for (ActionListener l : save.getActionListeners()) save.removeActionListener(l);
        for (ActionListener l : addComment.getActionListeners()) addComment.removeActionListener(l);
        for (ActionListener l : commentList.getActionListeners()) commentList.removeActionListener(l);
        for (KeyListener l : currentComment.getKeyListeners()) currentComment.removeKeyListener(l);
        statusG.clearSelection();
        status.removeAllItems();
        dev1.removeAllItems();
        dev2.removeAllItems();
        currentComment.setText("");
        commentTime.setText("");
        commentList.removeAllItems();
        commentList.setSelectedItem(null);
        name.setEditable(false);

        //Setting data and listeners
        if (selectedProj.getComments().size() > 0 && selectedProj.searchUserComment(User.users.get(activeUser.getLogin())) != null) {
            commentSB = new StringBuilder(selectedProj.searchUserComment(User.users.get(activeUser.getLogin())).getText());
        } else if (selectedProj.getComments().size() > 0) {
            commentSB = new StringBuilder("");
        }

        DataController.loadUsers();
        ArrayList<User> usrs = User.getDevelopers();
        ArrayList<User> aUsrs = new ArrayList<>();
        aUsrs.addAll(selectedProj.getWorkers());

        name.setText(selectedProj.getName());

        status.setModel(new DefaultComboBoxModel<>(Project.status.values()));
        status.setSelectedItem(selectedProj.getProjectStatus());

        if (activeUser.getUserType().equals(User.type.DEVELOPER)) {
            dev1.setEnabled(false);
            dev2.setEnabled(false);
            name.setEnabled(false);
        }

        dev1.addItem("");
        dev2.addItem("");
        for (User s : usrs) {
            dev1.addItem(s.getLogin());
            dev2.addItem(s.getLogin());
        }
        dev1.setSelectedItem(aUsrs.get(0).getLogin());
        if (aUsrs.size() == 1) dev2.setSelectedItem(null);
        else dev2.setSelectedItem(aUsrs.get(1).getLogin());

        if (selectedProj.getComments() != null) {
            for (Map.Entry<User, Comment> e : selectedProj.getComments().entrySet()) {
                commentList.addItem(e.getKey().getLogin());
            }
            commentList.setSelectedItem(activeUser.getLogin());
            if (commentList.getSelectedItem() != null) {
                if (commentList.getSelectedItem() == activeUser.getLogin()) {
                    currentComment.setText(commentSB.toString());
                } else
                    currentComment.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getText());
                commentTime.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getTsp().toString());
            }

        }

        currentComment.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                timer = System.currentTimeMillis();
                if (commentList.getSelectedItem().equals(activeUser.getLogin())) {
                    commentSB.setLength(0);

                    commentSB.append(currentComment.getText());
                    /*System.out.println(currentComment.);*/
                    System.out.println(commentSB.length());
                    System.out.println(commentSB.toString());
                    System.out.println(currentComment.getText());
                    /*if (e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                        System.out.println(commentSB.length());
                        System.out.println(commentSB.toString());
                        commentSB.setLength(commentSB.length() - 1);
                    } else {
                        System.out.println(commentSB.length());
                        System.out.println(commentSB.toString());
                        commentSB.append(e.getKeyChar());
                    }*/
                }
            }
        });

        commentList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commentList.getSelectedItem() != null) {
                    timer = System.currentTimeMillis();
                    SwingUtilities.invokeLater(
                            () -> {
                                if (commentList.getSelectedItem() == activeUser.getLogin()) {
                                    if (!(commentSB == null)) currentComment.setText(commentSB.toString());
                                    else {
                                        commentSB = new StringBuilder("");
                                        currentComment.setText(commentSB.toString());
                                    }
                                } else if (selectedProj.getComments().size() > 0 && selectedProj.searchUserComment(User.users.get(activeUser.getLogin())) != null) {
                                    currentComment.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getText());
                                    commentTime.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getTsp().toString());
                                }
                                if (commentList.getSelectedItem() != activeUser.getLogin()) {
                                    currentComment.setEditable(false);
                                } else {
                                    currentComment.setEditable(true);
                                }
                            }
                    );

                }
            }
        });

        addComment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer = System.currentTimeMillis();
                if (selectedProj.searchUserComment(activeUser) == null) {
                    selectedProj.addComment(new Comment("", activeUser), activeUser);
                    PopUpController.pop("Dodano komentarz");
                    DataController.sendLog(activeUser.getLogin() + " dodał komentarz do projektu : " + selectedProj.getName());
                    commentList.removeAllItems();
                    commentList.setSelectedItem(null);
                    if (selectedProj.getComments() != null) {
                        for (Map.Entry<User, Comment> ee : selectedProj.getComments().entrySet()) {
                            commentList.addItem(ee.getKey().getLogin());

                        }
                        commentList.setSelectedItem(activeUser.getLogin());
                        if (commentList.getSelectedItem() != null) {
                            currentComment.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getText());
                            commentTime.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getTsp().toString());
                        }

                    }
                } else {
                    PopUpController.pop("Twoj komentarz znajduję się w projekcie, możesz go edytować");
                }
            }
        });

        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer = System.currentTimeMillis();
                if (selectedProj == null)
                    PopUpController.pop("Nie wybrano projektu!");
                else {
                    if (dev1.getSelectedItem() == dev2.getSelectedItem())
                        PopUpController.pop("Wybrano 2 identycznych developerów!");
                    else {
                        StringBuilder sb = new StringBuilder("Zapisano ");
                        if (status.getSelectedItem() != selectedProj.getProjectStatus()) {
                            sb.append("status, ");
                            DataController.sendLog(activeUser.getLogin() + " zmienił status projektu " +
                                    selectedProj.getName() + " z " + selectedProj.getProjectStatus().toString() + " na " + status.getSelectedItem().toString());
                            selectedProj.setProjectStatus(Project.status.valueOf(status.getSelectedItem().toString()));
                        }
                        commentList.setSelectedItem(activeUser.getLogin());
                        if (commentList.getSelectedItem() != null) {
                            if (commentList.getSelectedItem() == activeUser.getLogin()) {
                                currentComment.setText(commentSB.toString());
                            } else
                                currentComment.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getText());
                            commentTime.setText(selectedProj.searchUserComment(User.users.get(commentList.getSelectedItem().toString())).getTsp().toString());
                            if (commentList.getSelectedItem() != activeUser.getLogin()) {
                                currentComment.setEditable(false);
                            } else {
                                currentComment.setEditable(true);
                            }
                        }
                        if (selectedProj.searchUserComment(User.users.get((activeUser.getLogin()))) != null) {
                            if (!currentComment.getText().equals(selectedProj.searchUserComment(User.users.get(activeUser.getLogin())).getText())) {
                                sb.append("komentarz, ");
                                DataController.sendLog(activeUser.getLogin() + " zmienił swoj komentarz w projekcie " + selectedProj.getName().toString());
                                selectedProj.changeComment(selectedProj.searchUserComment(User.users.get(activeUser.getLogin())), currentComment.getText());
                            }
                        }

                        if (!selectedProj.checkEquality(User.users.get(dev1.getSelectedItem())) || !selectedProj.checkEquality(User.users.get(dev2.getSelectedItem()))) {

                            HashSet<User> usr = new HashSet<>();
                            usr.add(User.users.get(dev1.getSelectedItem()));
                            usr.add(User.users.get(dev2.getSelectedItem()));
                            sb.append("pracownikow,");
                            DataController.sendLog(activeUser.getLogin() + " zmienił pracownikow projektu " + selectedProj.getName());
                            selectedProj.changeWorkers(usr);
                            DataController.loadPojects();
                        }

                        if (!sb.toString().equals("Zapisano ")) {
                            PopUpController.pop(sb.toString());
                        }
                    }
                }
            }
        });
    }

    /** Thread method to inactivity log out */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            //System.out.println(System.currentTimeMillis() - timer);
            if (System.currentTimeMillis() - timer >= 60000) {
                System.out.println(activeUser.getLogin() + " wylogowano");
                DataController.sendLog(activeUser.getLogin() + " wylogowano");
                PopUpController.pop("Wylogowano");
                dispose();
                SwingUtilities.invokeLater(
                        () -> new LoginWindow()
                );
                break;
            }
        }
    }
}
