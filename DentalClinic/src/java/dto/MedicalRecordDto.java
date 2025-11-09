/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;
import java.sql.Date;

public class MedicalRecordDto {

    private Integer recordId;
    private Integer appointmentId;
    private String diagnosis;
    private String symptoms;
    private String treatmentPlan;
    private Date followUpDate;
    private Date createdDateFrom;
    private Date createdDateTo;
    
    private Integer patientId;   // lọc theo bệnh nhân
    private Integer doctorId;
    
    private boolean paginationMode = true;
    private boolean sortMode = false;
    private int page = 1;
    private int size = 10;

    public MedicalRecordDto() {
    }

    public MedicalRecordDto(Integer recordId, Integer appointmentId, String diagnosis, String symptoms, String treatmentPlan, Date followUpDate, Date createdDateFrom, Date createdDateTo, Integer patientId, Integer doctorId) {
        this.recordId = recordId;
        this.appointmentId = appointmentId;
        this.diagnosis = diagnosis;
        this.symptoms = symptoms;
        this.treatmentPlan = treatmentPlan;
        this.followUpDate = followUpDate;
        this.createdDateFrom = createdDateFrom;
        this.createdDateTo = createdDateTo;
        this.patientId = patientId;
        this.doctorId = doctorId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
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

    public Date getCreatedDateFrom() {
        return createdDateFrom;
    }

    public void setCreatedDateFrom(Date createdDateFrom) {
        this.createdDateFrom = createdDateFrom;
    }

    public Date getCreatedDateTo() {
        return createdDateTo;
    }

    public void setCreatedDateTo(Date createdDateTo) {
        this.createdDateTo = createdDateTo;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isPaginationMode() {
        return paginationMode;
    }

    public void setPaginationMode(boolean paginationMode) {
        this.paginationMode = paginationMode;
    }

    public boolean isSortMode() {
        return sortMode;
    }

    public void setSortMode(boolean sortMode) {
        this.sortMode = sortMode;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    

    
    
    
    

}
