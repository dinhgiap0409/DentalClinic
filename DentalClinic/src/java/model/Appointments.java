/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
/**
 *
 * @author Nguyen Dinh Giap
 */
public class Appointments {

    private int appointmentId;       
    private Patients patientId;          
    private Doctor doctorId;            
    private Service serviceId;           
    private Date appointmentDate;   
    private Time startTime;         
    private Time endTime;            
    private String status;          
    private String notes;            
    private Timestamp createdDate;   
    private Timestamp updatedDate;   

    public Appointments() {
    }

    public Appointments(int appointmentId, Patients patientId, Doctor doctorId, Service serviceId, Date appointmentDate, Time startTime, Time endTime, String status, String notes, Timestamp createdDate, Timestamp updatedDate) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.notes = notes;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Patients getPatientId() {
        return patientId;
    }

    public void setPatientId(Patients patientId) {
        this.patientId = patientId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "Appointments{" + "appointmentId=" + appointmentId + ", patientId=" + patientId + ", doctorId=" + doctorId + ", serviceId=" + serviceId + ", appointmentDate=" + appointmentDate + ", startTime=" + startTime + ", endTime=" + endTime + ", status=" + status + ", notes=" + notes + ", createdDate=" + createdDate + ", updatedDate=" + updatedDate + '}';
    }
}
