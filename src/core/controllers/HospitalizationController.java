package core.controllers;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.controllers.support.ComboOptionParser;
import core.controllers.support.ControllerRepositories;
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.utils.IdGenerator;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.entities.Hospitalization;
import core.models.entities.Patient;
import core.models.enums.AppointmentStatus;
import core.models.enums.HospitalizationStatus;
import core.models.enums.RoomType;
import core.models.storage.ModelChangeNotifier;
import core.models.storage.ModelChangeType;

public class HospitalizationController {

    public static Response getRoomTypeComboOptions() {
        try {
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            options.add("STANDARD");
            options.add("ICU");
            options.add("NICU");
            options.add("IMC");
            options.add("ISOLATION");
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Room type options loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response requestHospitalization(long patientId, String doctorSelection, String admissionDate,
            String reason, String roomTypeDisplay, String observations) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            if (reason == null || reason.trim().isEmpty()) {
                return new Response("Hospitalization reason must not be empty", Status.BAD_REQUEST);
            }

            Long doctorId = ComboOptionParser.parseDoctorIdFromCombo(doctorSelection);
            if (doctorId == null) {
                return new Response("Attending doctor must be selected", Status.BAD_REQUEST);
            }
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor is not valid", Status.BAD_REQUEST);
            }

            LocalDate parsedDate = parseDate(admissionDate);
            if (parsedDate == null) {
                return new Response("Admission date must be valid and follow the format AAAA-MM-DD", Status.BAD_REQUEST);
            }

            RoomType roomType = parseRoomType(roomTypeDisplay);
            if (roomType == null) {
                return new Response("Room type must be selected", Status.BAD_REQUEST);
            }

            if (hasActiveHospitalization(patientId)) {
                return new Response("Patient already has an active hospitalization request", Status.BAD_REQUEST);
            }

            String hospitalizationId = IdGenerator.nextHospitalizationId(patientId);
            String observationsValue = "";
            if (observations != null) {
                observationsValue = observations.trim();
            }

            Hospitalization hospitalization = new Hospitalization(hospitalizationId, patient, doctor, parsedDate,
                    reason.trim(), roomType, observationsValue);

            ControllerRepositories.HOSPITALIZATIONS.add(hospitalization);

            return new Response("Hospitalization requested successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response approveHospitalization(long doctorId, String hospitalizationId) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Hospitalization hospitalization = findValidHospitalizationForDoctor(doctorId, hospitalizationId);
            if (hospitalization == null) {
                return new Response("Hospitalization is not valid", Status.BAD_REQUEST);
            }
            if (hospitalization.getStatus() != HospitalizationStatus.REQUESTED) {
                return new Response("Only requested hospitalizations can be approved", Status.BAD_REQUEST);
            }
            hospitalization.setStatus(HospitalizationStatus.ONGOING);
            notifyHospitalizationUpdated();
            return new Response("Hospitalization approved successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response denyHospitalization(long doctorId, String hospitalizationId) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Hospitalization hospitalization = findValidHospitalizationForDoctor(doctorId, hospitalizationId);
            if (hospitalization == null) {
                return new Response("Hospitalization is not valid", Status.BAD_REQUEST);
            }
            if (hospitalization.getStatus() == HospitalizationStatus.CANCELED) {
                return new Response("Hospitalization is already canceled", Status.BAD_REQUEST);
            }
            if (hospitalization.getStatus() == HospitalizationStatus.ONGOING) {
                return new Response("Ongoing hospitalizations cannot be denied", Status.BAD_REQUEST);
            }
            hospitalization.setStatus(HospitalizationStatus.CANCELED);
            notifyHospitalizationUpdated();
            return new Response("Hospitalization denied successfully", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response createFromAppointment(long doctorId, String appointmentId, String admissionDate,
            String reason, String roomTypeDisplay, String observations) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            Appointment appointment = ControllerRepositories.APPOINTMENTS.findById(appointmentId);
            if (appointment == null) {
                return new Response("Appointment is not valid", Status.BAD_REQUEST);
            }
            if (appointment.getDoctor() == null || appointment.getDoctor().getId() != doctorId) {
                return new Response("Appointment does not belong to this doctor", Status.BAD_REQUEST);
            }
            if (appointment.getStatus() != AppointmentStatus.PENDING) {
                return new Response("Hospitalization can only be created from a pending appointment", Status.BAD_REQUEST);
            }
            if (appointment.getPatient() == null) {
                return new Response("Appointment patient is not valid", Status.BAD_REQUEST);
            }

            Patient patient = appointment.getPatient();
            if (hasActiveHospitalization(patient.getId())) {
                return new Response("Patient already has an active hospitalization", Status.BAD_REQUEST);
            }

            if (reason == null || reason.trim().isEmpty()) {
                return new Response("Hospitalization reason must not be empty", Status.BAD_REQUEST);
            }

            LocalDate parsedDate = parseDate(admissionDate);
            if (parsedDate == null) {
                return new Response("Admission date must be valid and follow the format AAAA-MM-DD", Status.BAD_REQUEST);
            }

            RoomType roomType = parseRoomType(roomTypeDisplay);
            if (roomType == null) {
                return new Response("Room type must be selected", Status.BAD_REQUEST);
            }

            appointment.setStatus(AppointmentStatus.COMPLETED);
            ModelChangeNotifier.getInstance().notifyChange(ModelChangeType.APPOINTMENT_UPDATED);

            String hospitalizationId = IdGenerator.nextHospitalizationId(patient.getId());
            String observationsValue = "";
            if (observations != null) {
                observationsValue = observations.trim();
            }

            Hospitalization hospitalization = new Hospitalization(hospitalizationId, patient, doctor, parsedDate,
                    reason.trim(), roomType, observationsValue, HospitalizationStatus.ONGOING);

            ControllerRepositories.HOSPITALIZATIONS.add(hospitalization);

            return new Response("Hospitalization created from appointment successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listPatientHospitalizations(long patientId) {
        try {
            Patient patient = ControllerRepositories.USER_LOOKUP.findPatientById(patientId);
            if (patient == null) {
                return new Response("Patient not found", Status.NOT_FOUND);
            }
            ArrayList<Hospitalization> list = collectHospitalizationsForPatient(patientId);
            sortHospitalizationsDescending(list);
            HashMap<String, Object> data = new HashMap<>();
            data.put("rows", serializeHospitalizationRows(list));
            return new Response("Hospitalizations loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listDoctorHospitalizations(long doctorId) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            ArrayList<Hospitalization> list = collectHospitalizationsForDoctor(doctorId);
            sortHospitalizationsDescending(list);
            HashMap<String, Object> data = new HashMap<>();
            data.put("rows", serializeHospitalizationRows(list));
            return new Response("Hospitalizations loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listPendingHospitalizationIds(long doctorId) {
        try {
            Doctor doctor = ControllerRepositories.USER_LOOKUP.findDoctorById(doctorId);
            if (doctor == null) {
                return new Response("Doctor not found", Status.NOT_FOUND);
            }
            ArrayList<String> options = new ArrayList<>();
            options.add("Select one");
            ArrayList<Hospitalization> list = collectHospitalizationsForDoctor(doctorId);
            for (Hospitalization hospitalization : list) {
                if (hospitalization.getStatus() == HospitalizationStatus.REQUESTED) {
                    options.add(hospitalization.getId());
                }
            }
            HashMap<String, Object> data = new HashMap<>();
            data.put("options", options);
            return new Response("Pending hospitalizations loaded", Status.OK, data);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    private static boolean hasActiveHospitalization(long patientId) {
        for (Hospitalization hospitalization : ControllerRepositories.HOSPITALIZATIONS.getAll()) {
            if (hospitalization.getPatient() != null && hospitalization.getPatient().getId() == patientId) {
                if (hospitalization.getStatus() != HospitalizationStatus.CANCELED) {
                    return true;
                }
            }
        }
        return false;
    }

    private static ArrayList<Hospitalization> collectHospitalizationsForPatient(long patientId) {
        ArrayList<Hospitalization> result = new ArrayList<>();
        for (Hospitalization hospitalization : ControllerRepositories.HOSPITALIZATIONS.getAll()) {
            if (hospitalization.getPatient() != null && hospitalization.getPatient().getId() == patientId) {
                result.add(hospitalization);
            }
        }
        return result;
    }

    private static ArrayList<Hospitalization> collectHospitalizationsForDoctor(long doctorId) {
        ArrayList<Hospitalization> result = new ArrayList<>();
        for (Hospitalization hospitalization : ControllerRepositories.HOSPITALIZATIONS.getAll()) {
            if (hospitalization.getDoctor() != null && hospitalization.getDoctor().getId() == doctorId) {
                result.add(hospitalization);
            }
        }
        return result;
    }

    private static void sortHospitalizationsDescending(ArrayList<Hospitalization> hospitalizations) {
        Collections.sort(hospitalizations, new Comparator<Hospitalization>() {
            @Override
            public int compare(Hospitalization first, Hospitalization second) {
                return second.getDate().compareTo(first.getDate());
            }
        });
    }

    private static ArrayList<HashMap<String, Object>> serializeHospitalizationRows(
            ArrayList<Hospitalization> hospitalizations) {
        ArrayList<HashMap<String, Object>> rows = new ArrayList<>();
        for (Hospitalization hospitalization : hospitalizations) {
            HashMap<String, Object> row = new HashMap<>();
            row.put("id", hospitalization.getId());
            row.put("date", hospitalization.getDate().toString());
            if (hospitalization.getDoctor() != null) {
                row.put("doctor", hospitalization.getDoctor().getFirstname() + " "
                        + hospitalization.getDoctor().getLastname());
            } else {
                row.put("doctor", "");
            }
            if (hospitalization.getPatient() != null) {
                row.put("patient", hospitalization.getPatient().getFirstname() + " "
                        + hospitalization.getPatient().getLastname());
            } else {
                row.put("patient", "");
            }
            row.put("roomType", hospitalization.getRoomType().name());
            row.put("status", hospitalization.getStatus().name());
            row.put("reason", hospitalization.getReason());
            rows.add(row);
        }
        return rows;
    }

    private static Hospitalization findValidHospitalizationForDoctor(long doctorId, String hospitalizationId) {
        if (hospitalizationId == null || hospitalizationId.trim().isEmpty()) {
            return null;
        }
        Hospitalization hospitalization = ControllerRepositories.HOSPITALIZATIONS.findById(hospitalizationId.trim());
        if (hospitalization == null) {
            return null;
        }
        if (hospitalization.getDoctor() == null || hospitalization.getDoctor().getId() != doctorId) {
            return null;
        }
        return hospitalization;
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

    private static RoomType parseRoomType(String roomTypeDisplay) {
        if (roomTypeDisplay == null || roomTypeDisplay.trim().isEmpty()) {
            return null;
        }
        if (ComboOptionParser.isMissingSelection(roomTypeDisplay)) {
            return null;
        }
        try {
            return RoomType.valueOf(roomTypeDisplay.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }

    private static void notifyHospitalizationUpdated() {
        ModelChangeNotifier.getInstance().notifyChange(ModelChangeType.HOSPITALIZATION_UPDATED);
    }

}
