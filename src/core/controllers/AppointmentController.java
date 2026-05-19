package core.controllers;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.controllers.support.ComboOptionParser;
import core.controllers.support.ControllerRepositories;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.utils.AvailabilityService;
import core.models.utils.IdGenerator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.Prescription;
import core.models.entities.User;
import core.models.enums.AppointmentStatus;
import core.models.enums.Specialty;
import core.models.storage.ModelChangeNotifier;
import core.models.storage.ModelChangeType;

public class AppointmentController {

    public static Response getSpecialtyComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            options.add("General Medicine");
            options.add("Cardiology");
            options.add("Pediatrics");
            options.add("Neurology");
            options.add("Traumatology & Orthopedics");
            options.add("Gynecology & Obstetrics");
            options.add("Dermatology");
            options.add("Psychiatry");
            options.add("Oncology");
            options.add("Ophthalmology");
            options.add("Internal Medicine");
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Specialty options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getDoctorComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            for (User user : ControllerRepositories.USERS.getUsers()) {
                if (user instanceof Doctor) {
                    Doctor doctor = (Doctor) user;
                    options.add(doctor.getId() + " - " + doctor.getFirstname() + " " + doctor.getLastname());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Doctor options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getDoctorNameComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            for (User user : ControllerRepositories.USERS.getUsers()) {
                if (user instanceof Doctor) {
                    Doctor doctor = (Doctor) user;
                    options.add(doctor.getFirstname() + " " + doctor.getLastname());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Doctor options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listPatientNameComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            for (User user : ControllerRepositories.USERS.getUsers()) {
                if (user instanceof Patient) {
                    Patient patient = (Patient) user;
                    options.add(patient.getFirstname() + " " + patient.getLastname());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Patient options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response requestAppointment(long patientId, boolean byDoctor, String comboSelection,
            String date, String time, String reason, String appointmentTypeDisplay) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            if (reason == null || reason.trim().isEmpty()) {
                return new Response("Appointment reason must not be empty", Status.BAD_REQUEST);
            }

            LocalDate appointmentDate = parseDate(date);
            if (appointmentDate == null) {
                return new Response("Appointment date must be valid and follow the format AAAA-MM-DD", Status.BAD_REQUEST);
            }
            LocalTime appointmentTime = parseTime(time);
            if (appointmentTime == null) {
                return new Response("Appointment time must be valid, follow the format hh:mm and use quarter hours", Status.BAD_REQUEST);
            }
            LocalDateTime datetime = LocalDateTime.of(appointmentDate, appointmentTime);

            Boolean inPerson = parseAppointmentType(appointmentTypeDisplay);
            if (inPerson == null) {
                return new Response("Appointment type must be selected", Status.BAD_REQUEST);
            }

            Doctor doctor = null;
            Specialty specialty = null;

            if (byDoctor) {
                Long doctorId = ComboOptionParser.parseDoctorIdFromCombo(comboSelection);
                if (doctorId == null) {
                    return new Response("Doctor must be selected", Status.BAD_REQUEST);
                }
                doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
                if (doctor == null) {
                    return new Response("Doctor is not valid", Status.BAD_REQUEST);
                }
                specialty = doctor.getSpecialty();
            } else {
                specialty = parseSpecialtyFromDisplay(comboSelection);
                if (specialty == null) {
                    return new Response("Specialty must be selected", Status.BAD_REQUEST);
                }
                doctor = findAvailableDoctorForSpecialty(specialty, datetime);
                if (doctor == null) {
                    return new Response("No doctor with that specialty is available at the requested time", Status.BAD_REQUEST);
                }
            }

            if (!AvailabilityService.isDoctorAvailable(doctor, datetime, null)) {
                return new Response("The doctor is not available at the requested time", Status.BAD_REQUEST);
            }

            String appointmentId = IdGenerator.nextAppointmentId(patientId);
            Appointment appointment = new Appointment(appointmentId, patient, doctor, specialty, datetime,
                    reason.trim(), inPerson);

            ControllerRepositories.APPOINTMENTS.add(appointment);
            patient.addAppointment(appointment);
            doctor.getAppointments().add(appointment);

            return new Response("Appointment requested successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response cancelAppointment(long patientId, String appointmentId, String observations) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            if (ComboOptionParser.isMissingSelection(appointmentId)) {
                return new Response("Appointment must be selected", Status.BAD_REQUEST);
            }

            Appointment appointment = ControllerRepositories.APPOINTMENTS.findById(appointmentId.trim());
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getPatient() == null || appointment.getPatient().getId() != patientId) {
                return new Response("Appointment does not belong to this patient", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                return new Response("Completed appointments cannot be canceled", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() == AppointmentStatus.CANCELED) {
                return new Response("Appointment is already canceled", Status.BAD_REQUEST);
            }

            appointment.setStatus(AppointmentStatus.CANCELED);
            if (observations != null && !observations.trim().isEmpty()) {
                appointment.setObservations(observations.trim());
            }

            notifyAppointmentUpdated();
            return new Response("Appointment canceled successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response acceptAppointment(long doctorId, String appointmentId) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Appointment appointment = findValidAppointmentForDoctor(doctorId, appointmentId);
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() != AppointmentStatus.REQUESTED) {
                return new Response("Only requested appointments can be accepted", Status.BAD_REQUEST);
            }
            appointment.setStatus(AppointmentStatus.PENDING);
            notifyAppointmentUpdated();
            return new Response("Appointment accepted successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response completeAppointment(long doctorId, String appointmentId, String diagnosis,
            String observations, String recommendedTreatment, String followUp) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Appointment appointment = findValidAppointmentForDoctor(doctorId, appointmentId);
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() != AppointmentStatus.PENDING) {
                return new Response("Only pending appointments can be completed", Status.BAD_REQUEST);
            }
            if (diagnosis != null) {
                appointment.setDiagnosis(diagnosis.trim());
            }
            if (observations != null) {
                appointment.setObservations(observations.trim());
            }
            if (recommendedTreatment != null) {
                appointment.setRecommendedTreatment(recommendedTreatment.trim());
            }
            if (followUp != null) {
                appointment.setFollowUp(followUp.trim());
            }
            appointment.setStatus(AppointmentStatus.COMPLETED);
            notifyAppointmentUpdated();
            return new Response("Appointment completed successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response rescheduleAppointment(long doctorId, String appointmentId, String newTime,
            String rescheduleReason) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Appointment appointment = findValidAppointmentForDoctor(doctorId, appointmentId);
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() == AppointmentStatus.COMPLETED
                    || appointment.getStatus() == AppointmentStatus.CANCELED) {
                return new Response("This appointment cannot be rescheduled", Status.BAD_REQUEST);
            }

            LocalTime parsedTime = parseTime(newTime);
            if (parsedTime == null) {
                return new Response("New time must be valid, follow the format hh:mm and use quarter hours", Status.BAD_REQUEST);
            }

            LocalDateTime currentDatetime = appointment.getDatetime();
            LocalDateTime newDatetime = LocalDateTime.of(currentDatetime.toLocalDate(), parsedTime);

            if (!AvailabilityService.isDoctorAvailable(doctor, newDatetime, appointment.getId())) {
                return new Response("The doctor is not available at the new time", Status.BAD_REQUEST);
            }

            appointment.setDatetime(newDatetime);

            if (rescheduleReason != null && !rescheduleReason.trim().isEmpty()) {
                String originalReason = appointment.getReason();
                if (originalReason == null) {
                    originalReason = "";
                }
                String combined = originalReason + " | Reschedule: " + rescheduleReason.trim();
                appointment.setReason(combined);
            }

            notifyAppointmentUpdated();
            return new Response("Appointment rescheduled successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response prescribeMedication(long doctorId, String appointmentId, String medicationName,
            double dose, String administrationRoute, int treatmentDuration, String additionalInstructions,
            int frequency) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Appointment appointment = findValidAppointmentForDoctor(doctorId, appointmentId);
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() != AppointmentStatus.PENDING) {
                return new Response("Medications can only be prescribed for pending appointments", Status.BAD_REQUEST);
            }
            if (medicationName == null || medicationName.trim().isEmpty()) {
                return new Response("Medication name must not be empty", Status.BAD_REQUEST);
            }
            if (administrationRoute == null || administrationRoute.trim().isEmpty()) {
                return new Response("Administration route must not be empty", Status.BAD_REQUEST);
            }
            if (treatmentDuration <= 0) {
                return new Response("Treatment duration must be greater than 0", Status.BAD_REQUEST);
            }
            if (frequency <= 0) {
                return new Response("Frequency must be greater than 0", Status.BAD_REQUEST);
            }

            new Prescription(appointment, medicationName.trim(), dose, administrationRoute.trim(),
                    treatmentDuration, additionalInstructions, frequency);

            notifyAppointmentUpdated();
            return new Response("Medication prescribed successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listPatientAppointments(long patientId) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            ArrayList<Appointment> patientAppointments = collectAppointmentsForPatient(patientId);
            sortAppointmentsDescending(patientAppointments);
            ArrayList<HashMap<String, Object>> rows = serializeAppointmentRows(patientAppointments);
            HashMap<String, Object> data = new HashMap<>();
            data.put("rows", rows);
            return new Response("Appointments loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listDoctorAppointments(long doctorId, boolean pendingOnly) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            ArrayList<Appointment> doctorAppointments = collectAppointmentsForDoctor(doctorId);
            if (pendingOnly) {
                ArrayList<Appointment> filtered = new ArrayList<>();
                for (Appointment appointment : doctorAppointments) {
                    if (appointment.getStatus() == AppointmentStatus.PENDING) {
                        filtered.add(appointment);
                    }
                }
                doctorAppointments = filtered;
            }
            sortAppointmentsDescending(doctorAppointments);
            ArrayList<HashMap<String, Object>> rows = serializeDoctorAppointmentRows(doctorAppointments);
            HashMap<String, Object> data = new HashMap<>();
            data.put("rows", rows);
            return new Response("Appointments loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listPatientComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            for (User user : ControllerRepositories.USERS.getUsers()) {
                if (user instanceof Patient) {
                    Patient patient = (Patient) user;
                    options.add(patient.getId() + " - " + patient.getFirstname() + " " + patient.getLastname());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Patient options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listDoctorAppointmentIds(long doctorId, String statusFilter) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            ArrayList<Appointment> doctorAppointments = collectAppointmentsForDoctor(doctorId);
            sortAppointmentsDescending(doctorAppointments);
            for (Appointment appointment : doctorAppointments) {
                if (matchesDoctorAppointmentFilter(appointment, statusFilter)) {
                    options.add(appointment.getId());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Appointment options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listCancellableAppointmentIds(long patientId) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            ArrayList<Appointment> patientAppointments = collectAppointmentsForPatient(patientId);
            sortAppointmentsDescending(patientAppointments);
            for (Appointment appointment : patientAppointments) {
                if (appointment.getStatus() != AppointmentStatus.COMPLETED
                        && appointment.getStatus() != AppointmentStatus.CANCELED) {
                    options.add(appointment.getId());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Cancellable appointments loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static ArrayList<Appointment> collectAppointmentsForPatient(long patientId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : ControllerRepositories.APPOINTMENTS.getAll()) {
            if (appointment.getPatient() != null && appointment.getPatient().getId() == patientId) {
                result.add(appointment);
            }
        }
        return result;
    }

    private static ArrayList<Appointment> collectAppointmentsForDoctor(long doctorId) {
        ArrayList<Appointment> result = new ArrayList<>();
        for (Appointment appointment : ControllerRepositories.APPOINTMENTS.getAll()) {
            if (appointment.getDoctor() != null && appointment.getDoctor().getId() == doctorId) {
                result.add(appointment);
            }
        }
        return result;
    }

    private static void sortAppointmentsDescending(ArrayList<Appointment> appointments) {
        Collections.sort(appointments, new Comparator<Appointment>() {
            @Override
            public int compare(Appointment first, Appointment second) {
                return second.getDatetime().compareTo(first.getDatetime());
            }
        });
    }

    private static boolean matchesDoctorAppointmentFilter(Appointment appointment, String statusFilter) {
        if (statusFilter == null) {
            return false;
        }
        if ("REQUESTED".equals(statusFilter)) {
            return appointment.getStatus() == AppointmentStatus.REQUESTED;
        }
        if ("PENDING".equals(statusFilter)) {
            return appointment.getStatus() == AppointmentStatus.PENDING;
        }
        if ("RESCHEDULABLE".equals(statusFilter)) {
            if (appointment.getStatus() == AppointmentStatus.COMPLETED) {
                return false;
            }
            if (appointment.getStatus() == AppointmentStatus.CANCELED) {
                return false;
            }
            return true;
        }
        return false;
    }

    private static ArrayList<HashMap<String, Object>> serializeAppointmentRows(ArrayList<Appointment> appointments) {
        ArrayList<HashMap<String, Object>> rows = new ArrayList<>();
        for (Appointment appointment : appointments) {
            rows.add(buildAppointmentRow(appointment));
        }
        return rows;
    }

    private static ArrayList<HashMap<String, Object>> serializeDoctorAppointmentRows(ArrayList<Appointment> appointments) {
        ArrayList<HashMap<String, Object>> rows = new ArrayList<>();
        for (Appointment appointment : appointments) {
            HashMap<String, Object> row = buildAppointmentRow(appointment);
            if (appointment.getPatient() != null) {
                row.put("patient", appointment.getPatient().getFirstname() + " "
                        + appointment.getPatient().getLastname());
            } else {
                row.put("patient", "");
            }
            rows.add(row);
        }
        return rows;
    }

    private static HashMap<String, Object> buildAppointmentRow(Appointment appointment) {
        HashMap<String, Object> row = new HashMap<>();
        row.put("id", appointment.getId());
        row.put("date", appointment.getDatetime().toString());
        if (appointment.getDoctor() != null) {
            row.put("doctor", appointment.getDoctor().getFirstname() + " "
                    + appointment.getDoctor().getLastname());
        } else {
            row.put("doctor", "");
        }
        row.put("specialty", specialtyToDisplay(appointment.getSpecialty()));
        row.put("type", appointmentTypeLabel(appointment.isType()));
        row.put("status", appointment.getStatus().name());
        return row;
    }

    private static String appointmentTypeLabel(boolean inPerson) {
        if (inPerson) {
            return "In-person";
        }
        return "Remote";
    }

    private static Doctor findAvailableDoctorForSpecialty(Specialty specialty, LocalDateTime datetime) {
        for (User user : ControllerRepositories.USERS.getUsers()) {
            if (user instanceof Doctor) {
                Doctor doctor = (Doctor) user;
                if (doctor.getSpecialty() == specialty
                        && AvailabilityService.isDoctorAvailable(doctor, datetime, null)) {
                    return doctor;
                }
            }
        }
        return null;
    }

    private static Appointment findValidAppointmentForDoctor(long doctorId, String appointmentId) {
        if (appointmentId == null || appointmentId.trim().isEmpty()) {
            return null;
        }
        Appointment appointment = ControllerRepositories.APPOINTMENTS.findById(appointmentId.trim());
        if (appointment == null) {
            return null;
        }
        if (appointment.getDoctor() == null || appointment.getDoctor().getId() != doctorId) {
            return null;
        }
        return appointment;
    }

    private static LocalDate parseDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim());
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static LocalTime parseTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            return null;
        }
        try {
            LocalTime parsed = LocalTime.parse(time.trim());
            int minute = parsed.getMinute();
            if (minute != 0 && minute != 15 && minute != 30 && minute != 45) {
                return null;
            }
            return parsed;
        } catch (DateTimeParseException ex) {
            return null;
        }
    }

    private static Boolean parseAppointmentType(String appointmentTypeDisplay) {
        if (appointmentTypeDisplay == null || appointmentTypeDisplay.trim().isEmpty()) {
            return null;
        }
        if (ComboOptionParser.isMissingSelection(appointmentTypeDisplay)) {
            return null;
        }
        if ("In-person".equals(appointmentTypeDisplay)) {
            return true;
        }
        if ("Remote".equals(appointmentTypeDisplay)) {
            return false;
        }
        return null;
    }

    private static Specialty parseSpecialtyFromDisplay(String specialtyDisplay) {
        if (ComboOptionParser.isMissingSelection(specialtyDisplay)) {
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
        return "";
    }

    private static void notifyAppointmentUpdated() {
        ModelChangeNotifier.getInstance().notifyChange(ModelChangeType.APPOINTMENT_UPDATED);
    }
}
