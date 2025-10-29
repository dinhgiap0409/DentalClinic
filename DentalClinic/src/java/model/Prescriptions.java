/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Prescriptions {

    private int prescriptionId;
    private MedicalRecords recordId;           // RecordID (FK -> MedicalRecords)
    private Doctor doctorId;           // DoctorID (FK -> Doctors)
    private Timestamp issueDate;    // IssueDate (default GETDATE)
    private String instructions;

    public Prescriptions() {
    }

    public Prescriptions(int prescriptionId, MedicalRecords recordId, Doctor doctorId, Timestamp issueDate, String instructions) {
        this.prescriptionId = prescriptionId;
        this.recordId = recordId;
        this.doctorId = doctorId;
        this.issueDate = issueDate;
        this.instructions = instructions;
    }

    public int getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(int prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public MedicalRecords getRecordId() {
        return recordId;
    }

    public void setRecordId(MedicalRecords recordId) {
        this.recordId = recordId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public Timestamp getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Timestamp issueDate) {
        this.issueDate = issueDate;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Prescriptions{" + "prescriptionId=" + prescriptionId + ", recordId=" + recordId + ", doctorId=" + doctorId + ", issueDate=" + issueDate + ", instructions=" + instructions + '}';
    }

}
