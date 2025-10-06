/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class PrescriptionDetails {

    private int prescriptionDetailId;
    private Prescriptions prescriptionId;        // PrescriptionID (FK -> Prescriptions)
    private MedicalRecords medicationId;          // MedicationID (FK -> Medications)
    private String dosage;
    private int quantity;
    private String duration;

    public PrescriptionDetails(int prescriptionDetailId, Prescriptions prescriptionId, MedicalRecords medicationId, String dosage, int quantity, String duration) {
        this.prescriptionDetailId = prescriptionDetailId;
        this.prescriptionId = prescriptionId;
        this.medicationId = medicationId;
        this.dosage = dosage;
        this.quantity = quantity;
        this.duration = duration;
    }

    public PrescriptionDetails() {
    }

    public int getPrescriptionDetailId() {
        return prescriptionDetailId;
    }

    public void setPrescriptionDetailId(int prescriptionDetailId) {
        this.prescriptionDetailId = prescriptionDetailId;
    }

    public Prescriptions getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Prescriptions prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public MedicalRecords getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(MedicalRecords medicationId) {
        this.medicationId = medicationId;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "PrescriptionDetails{" + "prescriptionDetailId=" + prescriptionDetailId + ", prescriptionId=" + prescriptionId + ", medicationId=" + medicationId + ", dosage=" + dosage + ", quantity=" + quantity + ", duration=" + duration + '}';
    }

}
