package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storage.Storage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import packagee.Patient;

public class UserController {

    public static Response registerPatient(String id, String firstname, String lastname,
            String username, String password, String passwordConfirmation, String email,
            String birthdate, String phone, String address, String gender) {
        try {
            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (username == null || username.trim().isEmpty()) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }
            if (address == null || address.trim().isEmpty()) {
                return new Response("Address must not be empty", Status.BAD_REQUEST);
            }

            long patientId;
            try {
                patientId = Long.parseLong(id.trim());
            } catch (NumberFormatException ex) {
                return new Response("Id must be numeric", Status.BAD_REQUEST);
            }
            if (patientId <= 0) {
                return new Response("Id must be greater than 0", Status.BAD_REQUEST);
            }
            if (String.valueOf(patientId).length() != 12) {
                return new Response("Id must have exactly 12 digits", Status.BAD_REQUEST);
            }

            if (password == null || password.isEmpty()) {
                return new Response("Password is required", Status.BAD_REQUEST);
            }
            if (passwordConfirmation == null || !password.equals(passwordConfirmation)) {
                return new Response("Password and confirmation must match", Status.BAD_REQUEST);
            }

            if (!isValidEmail(email)) {
                return new Response("Email must follow the format XXXXX@XXXXX.com", Status.BAD_REQUEST);
            }

            if (!isValidPhone(phone)) {
                return new Response("Phone must have exactly 10 digits", Status.BAD_REQUEST);
            }

            LocalDate birthdateParsed;
            try {
                birthdateParsed = LocalDate.parse(birthdate.trim());
            } catch (DateTimeParseException ex) {
                return new Response("Birthdate must be valid and follow the format AAAA-MM-DD", Status.BAD_REQUEST);
            }

            Boolean genderValue = parseGender(gender);
            if (genderValue == null) {
                return new Response("Gender must be selected", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();
            if (storage.findUserById(patientId) != null) {
                return new Response("A user with that id already exists", Status.BAD_REQUEST);
            }
            if (storage.findUserByUsername(username.trim()) != null) {
                return new Response("Username already exists", Status.BAD_REQUEST);
            }

            Patient patient = new Patient(patientId, username.trim(), firstname.trim(), lastname.trim(),
                    password, email.trim(), birthdateParsed, genderValue, Long.parseLong(phone.trim()), address.trim());

            if (!storage.addUser(patient)) {
                return new Response("Could not register patient", Status.BAD_REQUEST);
            }

            return new Response("Patient registered successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String trimmed = email.trim();
        int atIndex = trimmed.indexOf('@');
        if (atIndex <= 0) {
            return false;
        }
        String local = trimmed.substring(0, atIndex);
        String domain = trimmed.substring(atIndex + 1);
        if (local.isEmpty() || domain.isEmpty()) {
            return false;
        }
        if (!domain.endsWith(".com")) {
            return false;
        }
        String domainName = domain.substring(0, domain.length() - 4);
        return !domainName.isEmpty();
    }

    private static boolean isValidPhone(String phone) {
        if (phone == null) {
            return false;
        }
        String trimmed = phone.trim();
        if (trimmed.length() != 10) {
            return false;
        }
        for (int i = 0; i < trimmed.length(); i++) {
            if (!Character.isDigit(trimmed.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static Boolean parseGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            return null;
        }
        if ("Female".equals(gender)) {
            return false;
        }
        if ("Male".equals(gender)) {
            return true;
        }
        return null;
    }
}
