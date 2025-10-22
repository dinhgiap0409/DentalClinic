/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class AppointmentDetailDto {
    private int appointmentId;
    private Date appointmentDate;
    private Time startTime;
    private String status;
    private String notes;
    private int patientId;
    private String patientName;
    private String patientPhone;
    private String doctorName;
    private String serviceName;

    public AppointmentDetailDto() {
    }

    public AppointmentDetailDto(int appointmentId, Date appointmentDate, Time startTime, String status, String notes,
            int patientId, String patientName, String patientPhone, String doctorName, String serviceName) {
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.startTime = startTime;
        this.status = status;
        this.notes = notes;
        this.patientId = patientId;
        this.patientName = patientName;
        this.patientPhone = patientPhone;
        this.doctorName = doctorName;
        this.serviceName = serviceName;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public void setPatientPhone(String patientPhone) {
        this.patientPhone = patientPhone;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    @Override
    public String toString() {
        return "AppointmentDetailDto{" + "appointmentId=" + appointmentId + ", appointmentDate=" + appointmentDate
                + ", startTime=" + startTime + ", status=" + status + ", notes=" + notes + ", patientId=" + patientId
                + ", patientName=" + patientName + ", patientPhone=" + patientPhone + ", doctorName=" + doctorName
                + ", serviceName=" + serviceName + '}';
    }
}