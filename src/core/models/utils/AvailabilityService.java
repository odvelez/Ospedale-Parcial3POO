package core.models.utils;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.storage.Storage;
import java.time.LocalDateTime;
import core.models.entities.Appointment;
import core.models.entities.Doctor;
import core.models.enums.AppointmentStatus;

public final class AvailabilityService {

    public static final int APPOINTMENT_DURATION_MINUTES = 15;

    private AvailabilityService() {
    }

    public static boolean isDoctorAvailable(Doctor doctor, LocalDateTime start, String excludeAppointmentId) {
        if (doctor == null || start == null) {
            return false;
        }
        LocalDateTime end = start.plusMinutes(APPOINTMENT_DURATION_MINUTES);
        Storage storage = Storage.getInstance();
        for (Appointment appointment : storage.getAppointments()) {
            if (appointment.getDoctor() != null && appointment.getDoctor().getId() == doctor.getId()) {
                if (appointment.getStatus() != AppointmentStatus.CANCELED) {
                    boolean isExcluded = false;
                    if (excludeAppointmentId != null && excludeAppointmentId.equals(appointment.getId())) {
                        isExcluded = true;
                    }
                    if (!isExcluded) {
                        LocalDateTime existingStart = appointment.getDatetime();
                        if (existingStart != null) {
                            LocalDateTime existingEnd = existingStart.plusMinutes(APPOINTMENT_DURATION_MINUTES);
                            if (start.isBefore(existingEnd) && end.isAfter(existingStart)) {
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
