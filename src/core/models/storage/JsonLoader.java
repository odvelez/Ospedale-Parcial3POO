package core.models.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import org.json.JSONArray;
import org.json.JSONObject;

import core.models.Administrator;
import packagee.Doctor;
import packagee.Patient;
import packagee.Specialty;
import packagee.User;

/**
 * Loads users from json/users.json into Storage using org.json.
 */
public class JsonLoader {

    public static void loadUsers() throws IOException {
        Path path = resolveUsersJsonPath();
        String content = Files.readString(path);
        JSONObject root = new JSONObject(content);
        JSONArray usersArray = root.getJSONArray("users");
        Storage storage = Storage.getInstance();

        for (int i = 0; i < usersArray.length(); i++) {
            JSONObject userJson = usersArray.getJSONObject(i);
            User user = parseUser(userJson);
            if (!storage.addUser(user)) {
                System.err.println("Skipped duplicate user: " + user.getUsername());
            }
        }
    }

    private static User parseUser(JSONObject userJson) {
        String type = userJson.getString("type");
        long id = userJson.getLong("id");
        String username = userJson.getString("username");
        String firstname = userJson.getString("firstname");
        String lastname = userJson.getString("lastname");
        String password = userJson.getString("password");

        if ("admin".equals(type)) {
            return new Administrator(id, username, firstname, lastname, password);
        }
        if ("patient".equals(type)) {
            return parsePatient(userJson, id, username, firstname, lastname, password);
        }
        if ("doctor".equals(type)) {
            return parseDoctor(userJson, id, username, firstname, lastname, password);
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

    private static Patient parsePatient(JSONObject userJson, long id, String username,
            String firstname, String lastname, String password) {
        String email = userJson.getString("email");
        LocalDate birthdate = LocalDate.parse(userJson.getString("birthdate"));
        boolean gender = userJson.getBoolean("gender");
        long phone = userJson.getLong("phone");
        String address = userJson.getString("address");
        return new Patient(id, username, firstname, lastname, password, email, birthdate, gender, phone, address);
    }

    private static Doctor parseDoctor(JSONObject userJson, long id, String username,
            String firstname, String lastname, String password) {
        Specialty specialty = parseSpecialty(userJson.getString("specialty"));
        String licenceNumber = userJson.getString("licenceNumber");
        String assignedOffice = userJson.getString("assignedOffice");
        return new Doctor(id, username, firstname, lastname, password, specialty, licenceNumber, assignedOffice);
    }

    private static Specialty parseSpecialty(String jsonSpecialty) {
        if ("ORTHOPEDICS".equals(jsonSpecialty)) {
            return Specialty.TRAUMATOLOGY_ORTHOPEDICS;
        }
        if ("GYNECOLOGY".equals(jsonSpecialty)) {
            return Specialty.GYNECOLOGY_OBSTETRICS;
        }
        return Specialty.valueOf(jsonSpecialty);
    }

    private static Path resolveUsersJsonPath() throws IOException {
        Path workingDir = Paths.get("").toAbsolutePath().normalize();
        Path candidate = workingDir.resolve("json/users.json");
        if (Files.isRegularFile(candidate)) {
            return candidate;
        }

        candidate = workingDir.resolve("Ospedale-Parcial3POO/json/users.json");
        if (Files.isRegularFile(candidate)) {
            return candidate;
        }

        if (workingDir.getParent() != null) {
            candidate = workingDir.getParent().resolve("Ospedale-Parcial3POO/json/users.json");
            if (Files.isRegularFile(candidate)) {
                return candidate;
            }
        }

        throw new IOException(
                "users.json not found. Tried json/users.json under: " + workingDir);
    }
}
