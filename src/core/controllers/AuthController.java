package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.entities.Administrator;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.User;
import core.models.storage.Storage;
import java.util.HashMap;

public class AuthController {

    public static Response login(String username, String password) {
        try {
            if (username == null || username.trim().isEmpty()) {
                return new Response("Username is required", Status.BAD_REQUEST);
            }
            if (password == null || password.isEmpty()) {
                return new Response("Password is required", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();
            User found = storage.findUserByUsername(username.trim());

            if (found == null) {
                return new Response("User not found", Status.NOT_FOUND);
            }
            if (!found.getPassword().equals(password)) {
                return new Response("Invalid password", Status.BAD_REQUEST);
            }

            storage.setCurrentUser(found);

            HashMap<String, Object> data = new HashMap<>();
            data.put("id", found.getId());
            data.put("username", found.getUsername());
            data.put("firstname", found.getFirstname());
            data.put("lastname", found.getLastname());

            if (found instanceof Administrator) {
                data.put("role", "admin");
            } else if (found instanceof Doctor) {
                data.put("role", "doctor");
            } else if (found instanceof Patient) {
                data.put("role", "patient");
            }

            return new Response("Login successful", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response logout() {
        try {
            Storage.getInstance().setCurrentUser(null);
            return new Response("Logged out successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
