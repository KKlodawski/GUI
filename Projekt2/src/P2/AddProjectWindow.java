package P2;

import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;

public class AddProjectWindow extends JFrame {

    //Vars
    private User activeUser;
    private JPanel
            mainPanel = new JPanel(new BorderLayout()),
            buttonPanel = new JPanel(new FlowLayout()),
            contentPanel = new JPanel(new GridBagLayout());
    private JButton
            acceptB = new JButton("Create"),
            cancelB = new JButton("Cancel");
    private JTextField
            nameF = new JTextField("");
    private JComboBox
            statusF = new JComboBox(),
            firstDevF = new JComboBox(),
            secondDevF = new JComboBox();
    private JLabel
            nameL = new JLabel("Nazwa projektu: "),
            statusL = new JLabel(" Status projektu: "),
            firstDevL = new JLabel("Pierwszy developer: "),
            secondDevL = new JLabel("Drugi developer: ");

    //Constructors
    AddProjectWindow(User user) {
        activeUser = user;

        addListeners();

        setItems();
        setVisuals();
        setContentPanel();

        setContentPane(mainPanel);

        setSize(1080, 720);

        setResizable(false);

        setVisible(true);
    }

    //Methods

    private void setItems() {
        buttonPanel.add(cancelB);
        buttonPanel.add(acceptB);

        nameF.setPreferredSize(new Dimension(300, 40));
        statusF.setPreferredSize(new Dimension(300, 40));
        firstDevF.setPreferredSize(new Dimension(300, 40));
        secondDevF.setPreferredSize(new Dimension(300, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;

        contentPanel.add(nameL, gbc);
        gbc.gridy++;
        contentPanel.add(statusL, gbc);
        gbc.gridy++;
        contentPanel.add(firstDevL, gbc);
        gbc.gridy++;
        contentPanel.add(secondDevL, gbc);
        gbc.gridy = 0;
        gbc.gridx++;
        contentPanel.add(nameF, gbc);
        gbc.gridy++;
        contentPanel.add(statusF, gbc);
        gbc.gridy++;
        contentPanel.add(firstDevF, gbc);
        gbc.gridy++;
        contentPanel.add(secondDevF, gbc);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

    }

    private void setVisuals() {
        mainPanel.setBackground(Color.LIGHT_GRAY);
    }

    private void addListeners() {
        acceptB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (nameF.getText().equals("")) PopUpController.pop("Niepoprawna nazwa projektu!");
                else if (firstDevF.getSelectedItem() == secondDevF.getSelectedItem())
                    PopUpController.pop("Wybrano takich samych developerow");
                else if (Project.projects.containsKey(nameF.getText().toString()))
                    PopUpController.pop("Istnieje już projekt o podanej nazwie!");
                else {
                    Project p;
                    try {
                        if (firstDevF.getSelectedItem().equals("")) {
                            p = new Project(nameF.getText().toString(), User.users.get(secondDevF.getSelectedItem().toString()));
                        } else if (secondDevF.getSelectedItem().equals("")) {
                            p = new Project(nameF.getText().toString(), User.users.get(firstDevF.getSelectedItem().toString()));
                        } else {
                            p = new Project(nameF.getText().toString(), User.users.get(firstDevF.getSelectedItem().toString()), User.users.get(secondDevF.getSelectedItem().toString()));
                        }
                        p.silentSetProjectStatus(Project.status.valueOf(statusF.getSelectedItem().toString()));
                        PopUpController.pop("Utworzono nowy projekt o nazwie " + p.getName());
                        DataController.sendLog(activeUser.getLogin() + " utworzyl nowy projekt o nazwie " + p.getName());
                        DataController.sendProject(p);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    SwingUtilities.invokeLater(
                            () -> {
                                new FunctionalWindow(activeUser);
                            }
                    );
                    dispose();
                }

            }
        });

        cancelB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(
                        () -> {
                            new FunctionalWindow(activeUser);
                        }
                );
                dispose();
            }
        });

        //Window close
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataController.sendLog(activeUser.getLogin() + " wylogowano");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });

    }

    private void setContentPanel() {
        nameF.setText("");
        statusF.removeAllItems();
        firstDevF.removeAllItems();
        secondDevF.removeAllItems();

        ArrayList<User> usrs = User.getDevelopers();

        statusF.setModel(new DefaultComboBoxModel<>(Project.status.values()));
        firstDevF.addItem("");
        secondDevF.addItem("");
        for (User s : usrs) {
            firstDevF.addItem(s.getLogin());
            secondDevF.addItem(s.getLogin());
        }


    }


}
