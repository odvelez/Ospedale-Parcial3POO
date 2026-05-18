package core.models.storage;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

import core.models.entities.Hospitalization;
import java.util.ArrayList;

public interface HospitalizationRepository {

    ArrayList<Hospitalization> getAll();

    Hospitalization findById(String hospitalizationId);

    boolean add(Hospitalization hospitalization);
}
