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
    private Medications medications;          // MedicationID (FK -> Medications)
    private String dosage;
    private int quantity;
    private String duration;

    public PrescriptionDetails() {
    }

    public PrescriptionDetails(int prescriptionDetailId, Prescriptions prescriptionId, Medications medications, String dosage, int quantity, String duration) {
        this.prescriptionDetailId = prescriptionDetailId;
        this.prescriptionId = prescriptionId;
        this.medications = medications;
        this.dosage = dosage;
        this.quantity = quantity;
        this.duration = duration;
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

    public Medications getMedications() {
        return medications;
    }

    public void setMedications(Medications medications) {
        this.medications = medications;
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
    
    

    
}
