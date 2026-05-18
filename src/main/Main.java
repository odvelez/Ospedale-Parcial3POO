package main;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import com.formdev.flatlaf.FlatDarkLaf;
import core.models.storage.JsonLoader;
import java.io.IOException;
import javax.swing.UIManager;
import core.views.NewJFrame;

public class Main {

    public static void main(String[] args) {
        System.setProperty("flatlaf.useNativeLibrary", "false");
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        try {
            JsonLoader.loadUsers();
        } catch (IOException ex) {
            System.err.println("Failed to load users.json: " + ex.getMessage());
            ex.printStackTrace();
        }
        java.awt.EventQueue.invokeLater(() -> new NewJFrame().setVisible(true));
    }

}
