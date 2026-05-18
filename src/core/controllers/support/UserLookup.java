package core.controllers.support;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.Doctor;
import core.models.entities.Patient;
import core.models.entities.User;
import core.models.storage.UserRepository;

public class UserLookup {

    private final UserRepository userRepository;

    public UserLookup(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Patient findPatientById(long patientId) {
        User user = userRepository.findById(patientId);
        if (user instanceof Patient) {
            return (Patient) user;
        }
        return null;
    }

    public Doctor findDoctorById(long doctorId) {
        User user = userRepository.findById(doctorId);
        if (user instanceof Doctor) {
            return (Doctor) user;
        }
        return null;
    }
}
