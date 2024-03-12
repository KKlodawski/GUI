package P2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserDataWindow extends JFrame {
    //Vars
    private User activeUser;
    private JPanel
            mainPanel = new JPanel(new BorderLayout()),
            buttonPanel = new JPanel(new FlowLayout()),
            contentPanel = new JPanel(new GridBagLayout());
    private JLabel
            passwdL = new JLabel("Haslo: "),
            nameL = new JLabel("Imie: "),
            surNameL = new JLabel("Nazwisko: ");
    private JTextField
            passwdF = new JTextField(),
            nameF = new JTextField(),
            surNameF = new JTextField();
    private JButton
            acceptB = new JButton("Zatwierdz"),
            cancelB = new JButton("Wroc bez zmian");

    //Constructors
    UserDataWindow(User user) {
        this.activeUser = user;
        System.out.println(activeUser);
        System.out.println(user);

        setContentPane(mainPanel);

        addListeners();
        setItems();
        fillData();

        setSize(1080, 720);

        setResizable(false);

        setVisible(true);
    }

    //Methods

    /** Adding elements to window */
    private void setItems() {
        buttonPanel.add(cancelB);
        buttonPanel.add(acceptB);

        nameF.setPreferredSize(new Dimension(300, 40));
        surNameF.setPreferredSize(new Dimension(300, 40));
        passwdF.setPreferredSize(new Dimension(300, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;

        contentPanel.add(nameL, gbc);
        gbc.gridy++;
        contentPanel.add(surNameL, gbc);
        gbc.gridy++;
        contentPanel.add(passwdL, gbc);
        gbc.gridy = 0;
        gbc.gridx++;
        contentPanel.add(nameF, gbc);
        gbc.gridy++;
        contentPanel.add(surNameF, gbc);
        gbc.gridy++;
        contentPanel.add(passwdF, gbc);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

    }

    /** Filling elements with user's data */
    private void fillData() {
        nameF.setText(activeUser.getName());
        surNameF.setText(activeUser.getSurname());
        passwdF.setText(activeUser.getPassword());
    }

    /** Adding listeners to items */
    private void addListeners() {

        //Cancel button
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

        //Save data butoon
        acceptB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!passwdF.getText().toString().equals(activeUser.getPassword()))
                    activeUser.silentSetPassword(passwdF.getText().toString());
                if (!nameF.getText().toString().equals(activeUser.getName()))
                    activeUser.silentSetName(nameF.getText().toString());
                if (!surNameF.getText().toString().equals(activeUser.getName()))
                    activeUser.silentSetSurname(surNameF.getText().toString());
                DataController.modifyUser(activeUser);
                PopUpController.pop("Zapisano zmiany!");
                DataController.sendLog(activeUser.getLogin() + " zmienił swoje dane");

                SwingUtilities.invokeLater(
                        () -> {
                            new FunctionalWindow(activeUser);
                        }
                );
                dispose();
            }
        });

        //Window listener
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                DataController.sendLog(activeUser.getLogin() + " wylogowano");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
            }
        });
    }


}
