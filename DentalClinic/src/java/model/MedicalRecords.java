/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class MedicalRecords {

    private int recordId;
    private Appointments appointmentId;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private Date followUpDate;
    private Timestamp createdDate;

    public MedicalRecords() {
    }

    public MedicalRecords(int recordId, Appointments appointmentId, String diagnosis, String symptoms, String treatmentPlan, Date followUpDate, Timestamp createdDate) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.treatmentPlan = treatmentPlan;
        this.followUpDate = followUpDate;
        this.createdDate = createdDate;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public Appointments getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointments appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    public Date getFollowUpDate() {
        return followUpDate;
    }

    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "MedicalRecords{" + "recordId=" + recordId + ", appointmentId=" + appointmentId + ", diagnosis=" + diagnosis + ", symptoms=" + symptoms + ", treatmentPlan=" + treatmentPlan + ", followUpDate=" + followUpDate + ", createdDate=" + createdDate + '}';
    }

}
