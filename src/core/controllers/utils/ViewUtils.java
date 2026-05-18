package core.controllers.utils;

import core.controllers.AuthController;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import packagee.NewJFrame;

public class ViewUtils {

    public static void showResponseMessage(Response response) {
        if (response.getStatus() >= 500) {
            JOptionPane.showMessageDialog(null, response.getMessage(),
                    "Error " + response.getStatus(), JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() >= 400) {
            JOptionPane.showMessageDialog(null, response.getMessage(),
                    "Error " + response.getStatus(), JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, response.getMessage(),
                    "Response Message", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void performLogout(JFrame currentView) {
        Response response = AuthController.logout();
        showResponseMessage(response);
        if (response.getStatus() == Status.OK) {
            NewJFrame login = new NewJFrame();
            login.setVisible(true);
            currentView.setVisible(false);
        }
    }
}
