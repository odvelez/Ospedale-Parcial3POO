package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.storage.Storage;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import packagee.Administrator;
import packagee.Doctor;
import packagee.Patient;
import packagee.Specialty;
import packagee.User;

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

            Response idError = validateTwelveDigitId(id);
            if (idError != null) {
                return idError;
            }
            long patientId = Long.parseLong(id.trim());

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

    public static Response registerDoctor(String id, String firstname, String lastname,
            String username, String password, String passwordConfirmation, String specialtyDisplay,
            String licenceNumber, String assignedOffice) {
        try {
            Storage storage = Storage.getInstance();
            User currentUser = storage.getCurrentUser();
            if (!(currentUser instanceof Administrator)) {
                return new Response("Only administrators can register doctors", Status.BAD_REQUEST);
            }

            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (username == null || username.trim().isEmpty()) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }

            Response idError = validateTwelveDigitId(id);
            if (idError != null) {
                return idError;
            }
            long doctorId = Long.parseLong(id.trim());

            if (password == null || password.isEmpty()) {
                return new Response("Password is required", Status.BAD_REQUEST);
            }
            if (passwordConfirmation == null || !password.equals(passwordConfirmation)) {
                return new Response("Password and confirmation must match", Status.BAD_REQUEST);
            }

            Specialty specialty = parseSpecialtyFromDisplay(specialtyDisplay);
            if (specialty == null) {
                return new Response("Specialty must be selected", Status.BAD_REQUEST);
            }

            if (!isValidLicenceNumber(licenceNumber)) {
                return new Response("Licence number must follow the format L-XXXXXXXXXX MTL", Status.BAD_REQUEST);
            }

            if (!isValidAssignedOffice(assignedOffice)) {
                return new Response("Assigned office must follow the format O-XXX", Status.BAD_REQUEST);
            }

            if (storage.findUserById(doctorId) != null) {
                return new Response("A user with that id already exists", Status.BAD_REQUEST);
            }
            if (storage.findUserByUsername(username.trim()) != null) {
                return new Response("Username already exists", Status.BAD_REQUEST);
            }

            Doctor doctor = new Doctor(doctorId, username.trim(), firstname.trim(), lastname.trim(),
                    password, specialty, licenceNumber.trim(), assignedOffice.trim());

            if (!storage.addUser(doctor)) {
                return new Response("Could not register doctor", Status.BAD_REQUEST);
            }

            return new Response("Doctor registered successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getPatientProfile(long patientId) {
        try {
            Patient patient = findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            return new Response("Patient profile loaded", Status.OK, serializePatient(patient));
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getDoctorProfile(long doctorId) {
        try {
            Doctor doctor = findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            return new Response("Doctor profile loaded", Status.OK, serializeDoctor(doctor));
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updatePatient(long patientId, String firstname, String lastname,
            String username, String password, String passwordConfirmation, String email,
            String birthdate, String phone, String address, String gender) {
        try {
            Patient patient = findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }

            Response validationError = validatePatientFields(firstname, lastname, username, password,
                    passwordConfirmation, email, birthdate, phone, address, gender, patientId);
            if (validationError != null) {
                return validationError;
            }

            Boolean genderValue = parseGender(gender);
            patient.setFirstname(firstname.trim());
            patient.setLastname(lastname.trim());
            patient.setUsername(username.trim());
            patient.setPassword(password);
            patient.setEmail(email.trim());
            patient.setBirthdate(LocalDate.parse(birthdate.trim()));
            patient.setGender(genderValue);
            patient.setPhone(Long.parseLong(phone.trim()));
            patient.setAddress(address.trim());

            return new Response("Patient information updated successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updateDoctor(long doctorId, String firstname, String lastname,
            String username, String password, String passwordConfirmation, String specialtyDisplay,
            String licenceNumber, String assignedOffice) {
        try {
            Doctor doctor = findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }

            if (firstname == null || firstname.trim().isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.trim().isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (username == null || username.trim().isEmpty()) {
                return new Response("Username must not be empty", Status.BAD_REQUEST);
            }
            if (password == null || password.isEmpty()) {
                return new Response("Password is required", Status.BAD_REQUEST);
            }
            if (passwordConfirmation == null || !password.equals(passwordConfirmation)) {
                return new Response("Password and confirmation must match", Status.BAD_REQUEST);
            }

            Specialty specialty = parseSpecialtyFromDisplay(specialtyDisplay);
            if (specialty == null) {
                return new Response("Specialty must be selected", Status.BAD_REQUEST);
            }
            if (!isValidLicenceNumber(licenceNumber)) {
                return new Response("Licence number must follow the format L-XXXXXXXXXX MTL", Status.BAD_REQUEST);
            }
            if (!isValidAssignedOffice(assignedOffice)) {
                return new Response("Assigned office must follow the format O-XXX", Status.BAD_REQUEST);
            }
            if (isUsernameTakenByOther(username.trim(), doctorId)) {
                return new Response("Username already exists", Status.BAD_REQUEST);
            }

            doctor.setFirstname(firstname.trim());
            doctor.setLastname(lastname.trim());
            doctor.setUsername(username.trim());
            doctor.setPassword(password);
            doctor.setSpecialty(specialty);
            doctor.setLicenceNumber(licenceNumber.trim());
            doctor.setAssignedOffice(assignedOffice.trim());

            return new Response("Doctor information updated successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static Response validatePatientFields(String firstname, String lastname, String username,
            String password, String passwordConfirmation, String email, String birthdate,
            String phone, String address, String gender, long patientId) {
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
        try {
            LocalDate.parse(birthdate.trim());
        } catch (DateTimeParseException ex) {
            return new Response("Birthdate must be valid and follow the format AAAA-MM-DD", Status.BAD_REQUEST);
        }
        if (parseGender(gender) == null) {
            return new Response("Gender must be selected", Status.BAD_REQUEST);
        }
        if (isUsernameTakenByOther(username.trim(), patientId)) {
            return new Response("Username already exists", Status.BAD_REQUEST);
        }
        return null;
    }

    private static Patient findPatientById(long patientId) {
        Storage storage = Storage.getInstance();
        User user = storage.findUserById(patientId);
        if (user instanceof Patient) {
            return (Patient) user;
        }
        return null;
    }

    private static Doctor findDoctorById(long doctorId) {
        Storage storage = Storage.getInstance();
        User user = storage.findUserById(doctorId);
        if (user instanceof Doctor) {
            return (Doctor) user;
        }
        return null;
    }

    private static boolean isUsernameTakenByOther(String username, long excludeId) {
        Storage storage = Storage.getInstance();
        User existing = storage.findUserByUsername(username);
        if (existing == null) {
            return false;
        }
        return existing.getId() != excludeId;
    }

    private static HashMap<String, Object> serializePatient(Patient patient) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", patient.getId());
        data.put("firstname", patient.getFirstname());
        data.put("lastname", patient.getLastname());
        data.put("username", patient.getUsername());
        data.put("email", patient.getEmail());
        data.put("birthdate", patient.getBirthdate().toString());
        data.put("phone", String.valueOf(patient.getPhone()));
        data.put("address", patient.getAddress());
        if (patient.isGender()) {
            data.put("gender", "Male");
        } else {
            data.put("gender", "Female");
        }
        return data;
    }

    private static HashMap<String, Object> serializeDoctor(Doctor doctor) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id", doctor.getId());
        data.put("firstname", doctor.getFirstname());
        data.put("lastname", doctor.getLastname());
        data.put("username", doctor.getUsername());
        data.put("licenceNumber", doctor.getLicenceNumber());
        data.put("assignedOffice", doctor.getAssignedOffice());
        data.put("specialty", specialtyToDisplay(doctor.getSpecialty()));
        return data;
    }

    private static String specialtyToDisplay(Specialty specialty) {
        if (specialty == Specialty.GENERAL_MEDICINE) {
            return "General Medicine";
        }
        if (specialty == Specialty.CARDIOLOGY) {
            return "Cardiology";
        }
        if (specialty == Specialty.PEDIATRICS) {
            return "Pediatrics";
        }
        if (specialty == Specialty.NEUROLOGY) {
            return "Neurology";
        }
        if (specialty == Specialty.TRAUMATOLOGY_ORTHOPEDICS) {
            return "Traumatology & Orthopedics";
        }
        if (specialty == Specialty.GYNECOLOGY_OBSTETRICS) {
            return "Gynecology & Obstetrics";
        }
        if (specialty == Specialty.DERMATOLOGY) {
            return "Dermatology";
        }
        if (specialty == Specialty.PSYCHIATRY) {
            return "Psychiatry";
        }
        if (specialty == Specialty.ONCOLOGY) {
            return "Oncology";
        }
        if (specialty == Specialty.OPHTHALMOLOGY) {
            return "Ophthalmology";
        }
        if (specialty == Specialty.INTERNAL_MEDICINE) {
            return "Internal Medicine";
        }
        return "Select one";
    }

    private static Response validateTwelveDigitId(String id) {
        try {
            long userId = Long.parseLong(id.trim());
            if (userId <= 0) {
                return new Response("Id must be greater than 0", Status.BAD_REQUEST);
            }
            if (String.valueOf(userId).length() != 12) {
                return new Response("Id must have exactly 12 digits", Status.BAD_REQUEST);
            }
            return null;
        } catch (NumberFormatException ex) {
            return new Response("Id must be numeric", Status.BAD_REQUEST);
        }
    }

    private static Specialty parseSpecialtyFromDisplay(String specialtyDisplay) {
        if (specialtyDisplay == null || specialtyDisplay.trim().isEmpty()) {
            return null;
        }
        if ("Select one".equals(specialtyDisplay)) {
            return null;
        }
        if ("General Medicine".equals(specialtyDisplay)) {
            return Specialty.GENERAL_MEDICINE;
        }
        if ("Cardiology".equals(specialtyDisplay)) {
            return Specialty.CARDIOLOGY;
        }
        if ("Pediatrics".equals(specialtyDisplay)) {
            return Specialty.PEDIATRICS;
        }
        if ("Neurology".equals(specialtyDisplay)) {
            return Specialty.NEUROLOGY;
        }
        if ("Traumatology & Orthopedics".equals(specialtyDisplay)) {
            return Specialty.TRAUMATOLOGY_ORTHOPEDICS;
        }
        if ("Gynecology & Obstetrics".equals(specialtyDisplay)) {
            return Specialty.GYNECOLOGY_OBSTETRICS;
        }
        if ("Dermatology".equals(specialtyDisplay)) {
            return Specialty.DERMATOLOGY;
        }
        if ("Psychiatry".equals(specialtyDisplay)) {
            return Specialty.PSYCHIATRY;
        }
        if ("Oncology".equals(specialtyDisplay)) {
            return Specialty.ONCOLOGY;
        }
        if ("Ophthalmology".equals(specialtyDisplay)) {
            return Specialty.OPHTHALMOLOGY;
        }
        if ("Internal Medicine".equals(specialtyDisplay)) {
            return Specialty.INTERNAL_MEDICINE;
        }
        return null;
    }

    private static boolean isValidLicenceNumber(String licenceNumber) {
        if (licenceNumber == null) {
            return false;
        }
        String trimmed = licenceNumber.trim();
        if (!trimmed.startsWith("L-")) {
            return false;
        }
        if (!trimmed.endsWith(" MTL")) {
            return false;
        }
        String digitsPart = trimmed.substring(2, trimmed.length() - 4);
        if (digitsPart.length() != 10) {
            return false;
        }
        for (int i = 0; i < digitsPart.length(); i++) {
            if (!Character.isDigit(digitsPart.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidAssignedOffice(String assignedOffice) {
        if (assignedOffice == null) {
            return false;
        }
        String trimmed = assignedOffice.trim();
        if (!trimmed.startsWith("O-")) {
            return false;
        }
        if (trimmed.length() != 5) {
            return false;
        }
        String digitsPart = trimmed.substring(2);
        for (int i = 0; i < digitsPart.length(); i++) {
            if (!Character.isDigit(digitsPart.charAt(i))) {
                return false;
            }
        }
        return true;
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
