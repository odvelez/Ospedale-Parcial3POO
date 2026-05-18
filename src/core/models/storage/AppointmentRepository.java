package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.Appointment;
import java.util.ArrayList;

public interface AppointmentRepository {

    ArrayList<Appointment> getAll();

    Appointment findById(String appointmentId);

    boolean add(Appointment appointment);
}
