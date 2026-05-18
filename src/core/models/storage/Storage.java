package core.models.storage;

import java.util.ArrayList;
import core.models.entities.Appointment;
import core.models.entities.Hospitalization;
import core.models.entities.User;

public class Storage {

    private static Storage instance;

    private final ArrayList<User> users;
    private final ArrayList<Appointment> appointments;
    private final ArrayList<Hospitalization> hospitalizations;
    private User currentUser;

    private Storage() {
        this.users = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.hospitalizations = new ArrayList<>();
        this.currentUser = null;
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public ArrayList<Hospitalization> getHospitalizations() {
        return hospitalizations;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public User findUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public User findUserById(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public boolean addUser(User user) {
        if (findUserById(user.getId()) != null) {
            return false;
        }
        if (findUserByUsername(user.getUsername()) != null) {
            return false;
        }
        users.add(user);
        return true;
    }

    public boolean addAppointment(Appointment appointment) {
        appointments.add(appointment);
        return true;
    }

    public boolean addHospitalization(Hospitalization hospitalization) {
        hospitalizations.add(hospitalization);
        return true;
    }

    public Appointment findAppointmentById(String appointmentId) {
        if (appointmentId == null) {
            return null;
        }
        for (Appointment appointment : appointments) {
            if (appointmentId.equals(appointment.getId())) {
                return appointment;
            }
        }
        return null;
    }

    public Hospitalization findHospitalizationById(String hospitalizationId) {
        if (hospitalizationId == null) {
            return null;
        }
        for (Hospitalization hospitalization : hospitalizations) {
            if (hospitalizationId.equals(hospitalization.getId())) {
                return hospitalization;
            }
        }
        return null;
    }
}
