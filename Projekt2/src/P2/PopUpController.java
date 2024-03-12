package P2;

import javax.swing.*;

public abstract class PopUpController extends JFrame {

    /** Showing pop-up with given string */
    public static void pop(String text) {
        JOptionPane.showMessageDialog(null, text);
    }

}
