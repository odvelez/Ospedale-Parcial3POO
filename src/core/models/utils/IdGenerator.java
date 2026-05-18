package core.models.utils;

import core.models.storage.Storage;
import core.models.entities.Appointment;
import core.models.entities.Hospitalization;
public final class IdGenerator {

    private static final String APPOINTMENT_PREFIX = "A-";
    private static final String HOSPITALIZATION_PREFIX = "H-";

    private IdGenerator() {
    }

    public static String nextAppointmentId(long patientId) {
        int count = 0;
        Storage storage = Storage.getInstance();
        String prefix = APPOINTMENT_PREFIX + patientId + "-";
        for (Appointment appointment : storage.getAppointments()) {
            if (appointment.getPatient() != null
                    && appointment.getPatient().getId() == patientId
                    && appointment.getId() != null
                    && appointment.getId().startsWith(prefix)) {
                count++;
            }
        }
        return prefix + formatSequence(count);
    }

    public static String nextHospitalizationId(long patientId) {
        int count = 0;
        Storage storage = Storage.getInstance();
        String prefix = HOSPITALIZATION_PREFIX + patientId + "-";
        for (Hospitalization hospitalization : storage.getHospitalizations()) {
            if (hospitalization.getId() != null && hospitalization.getId().startsWith(prefix)) {
                count++;
            }
        }
        return prefix + formatSequence(count);
    }

    private static String formatSequence(int sequence) {
        String digits = String.valueOf(sequence);
        while (digits.length() < 4) {
            digits = "0" + digits;
        }
        return digits;
    }
}
