/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import com.formdev.flatlaf.FlatDarkLaf;
import core.models.storage.JsonLoader;
import java.io.IOException;
import javax.swing.UIManager;
import core.views.NewJFrame;

/**
 *
 * @author oscar
 */
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
