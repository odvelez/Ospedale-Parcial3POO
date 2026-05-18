package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.Hospitalization;
import java.util.ArrayList;

public class StorageHospitalizationRepository implements HospitalizationRepository {

    private final Storage storage;

    public StorageHospitalizationRepository() {
        this.storage = Storage.getInstance();
    }

    @Override
    public ArrayList<Hospitalization> getAll() {
        return storage.getHospitalizations();
    }

    @Override
    public Hospitalization findById(String hospitalizationId) {
        return storage.findHospitalizationById(hospitalizationId);
    }

    @Override
    public boolean add(Hospitalization hospitalization) {
        return storage.addHospitalization(hospitalization);
    }
}
