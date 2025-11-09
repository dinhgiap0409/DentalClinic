/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.*;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class AppointmentDto {

    private Integer patientId;
    private Integer doctorId;
    private Integer serviceId;
    private Date appointmentDate;
    private Date appointmentDateFrom;
    private Date appointmentDateTo;
    private Time startTime;
    private Time endTime;
    private String status;
    private Date createdDate;

    private String patientName;
    private String phoneNumber;
    private String doctorName;
    private String serviceName;

    private boolean paginationMode = true;
    private boolean sortMode = false;
    private int page = 1;
    private int size = 10;

    public AppointmentDto() {
    }

    public AppointmentDto(Integer patientId, Integer doctorId, Integer serviceId, Date appointmentDate, Date appointmentDateFrom, Date appointmentDateTo, Time startTime, Time endTime, String status, Date createdDate, String patientName, String phoneNumber, String doctorName, String serviceName) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.serviceId = serviceId;
        this.appointmentDate = appointmentDate;
        this.appointmentDateFrom = appointmentDateFrom;
        this.appointmentDateTo = appointmentDateTo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdDate = createdDate;
        this.patientName = patientName;
        this.phoneNumber = phoneNumber;
        this.doctorName = doctorName;
        this.serviceName = serviceName;
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

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Date getAppointmentDateFrom() {
        return appointmentDateFrom;
    }

    public void setAppointmentDateFrom(Date appointmentDateFrom) {
        this.appointmentDateFrom = appointmentDateFrom;
    }

    public Date getAppointmentDateTo() {
        return appointmentDateTo;
    }

    public void setAppointmentDateTo(Date appointmentDateTo) {
        this.appointmentDateTo = appointmentDateTo;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
