package core.controllers.support;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.storage.AppointmentRepository;
import core.models.storage.HospitalizationRepository;
import core.models.storage.StorageAppointmentRepository;
import core.models.storage.StorageHospitalizationRepository;
import core.models.storage.StorageUserRepository;
import core.models.storage.UserRepository;

public final class ControllerRepositories {

    public static final UserRepository USERS = new StorageUserRepository();
    public static final AppointmentRepository APPOINTMENTS = new StorageAppointmentRepository();
    public static final HospitalizationRepository HOSPITALIZATIONS = new StorageHospitalizationRepository();
    public static final UserLookup USER_LOOKUP = new UserLookup(USERS);

    private ControllerRepositories() {
    }
}
