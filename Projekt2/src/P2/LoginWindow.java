package P2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class LoginWindow extends JFrame {

    //Vars
    private JPanel
            mainPanel = new JPanel(),
            loginPanel = new JPanel(),
            buttonPanel = new JPanel();
    private JLabel
            loginL = new JLabel("Login: "),
            passwdL = new JLabel("Haslo: ");
    private JTextField
            loginF = new JTextField("Login"),
            passwdF = new JTextField("Password");
    private JButton
            confirm = new JButton("Zaloguj!");


    //Constructors
    public LoginWindow() throws HeadlessException {
        setSize(480, 360);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        mainPanel.setLayout(new BorderLayout());
        loginPanel.setLayout(new GridLayout(3, 2));
        buttonPanel.setLayout(new FlowLayout());

        loginPanel.add(loginL);
        loginPanel.add(loginF);
        loginPanel.add(passwdL);
        loginPanel.add(passwdF);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginPerform();
            }
        });

        loginF.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) loginPerform();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        passwdF.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 10) loginPerform();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });

        buttonPanel.add(confirm);

        mainPanel.setBackground(Color.LIGHT_GRAY);
        loginPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setBackground(Color.LIGHT_GRAY);

        loginPanel.setBorder(BorderFactory.createEmptyBorder(100, 50, 0, 50));

        mainPanel.add(loginPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        setVisible(true);
    }

    //Methods
    /**
    Checking if user typed correct data, If correct log in, Else throw errors
     */
    private void loginPerform() {
        if (User.users.containsKey(loginF.getText())) {
            if (String.valueOf(User.users.get(loginF.getText()).getPassword()).equals(passwdF.getText())) {
                System.out.println(loginF.getText() + " zalogowano");
                DataController.sendLog(loginF.getText() + " zalogowano");
                SwingUtilities.invokeLater(
                        () -> new FunctionalWindow(User.users.get(loginF.getText()))
                );
                dispose();
            } else {
                System.out.println(loginF.getText() + " nieudana próba logowania");
                PopUpController.pop("Invalid password");
                DataController.sendLog(loginF.getText() + " nieudana próba logowania");
            }
        } else {
            System.out.println("Niedana próba logowania niezarejestrowanego użytkownika");
            PopUpController.pop("User does not exist!");
            DataController.sendLog("Niedana próba logowania niezarejestrowanego użytkownika");
        }
    }
}
