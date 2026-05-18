package core.models.entities;

// @author odvelez
// @author lvillarreale
// @author joeltrespalaciosp

public class Prescription {
    private Appointment appointment;
    private String medicationName;
    private double dose;
    private String administrationRoute;
    private int treatmentDuration;
    private String additionalInstructions;
    private int frecuency;

    public Prescription(Appointment appointment, String medicationName, double dose, String administrationRoute, int treatmentDuration, String additionalInstructions, int frecuency) {
        this.appointment = appointment;
        appointment.addPrescription(this);
        this.medicationName = medicationName;
        this.dose = dose;
        this.administrationRoute = administrationRoute;
        this.treatmentDuration = treatmentDuration;
        this.additionalInstructions = additionalInstructions;
        this.frecuency = frecuency;
    }
    
    
    
}
