package P2;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        DataController.loadUsers();
        DataController.loadPojects();

        SwingUtilities.invokeLater(
                () -> new LoginWindow()
        );

    }
}
