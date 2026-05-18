package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.Appointment;
import java.util.ArrayList;

public class StorageAppointmentRepository implements AppointmentRepository {

    private final Storage storage;

    public StorageAppointmentRepository() {
        this.storage = Storage.getInstance();
    }

    @Override
    public ArrayList<Appointment> getAll() {
        return storage.getAppointments();
    }

    @Override
    public Appointment findById(String appointmentId) {
        return storage.findAppointmentById(appointmentId);
    }

    @Override
    public boolean add(Appointment appointment) {
        return storage.addAppointment(appointment);
    }
}
