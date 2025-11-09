/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.*;

public class PrescriptionDto {

    private Integer recordId;
    private Integer doctorId;
    private Timestamp issueDateFrom;
    private Timestamp issueDateTo;
    private String instructions;
    private Integer patientId;
    private Integer appointmentId;

    private boolean paginationMode = true;
    private boolean sortMode = false;
    private int page = 1;
    private int size = 10;

    public PrescriptionDto() {
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Timestamp getIssueDateFrom() {
        return issueDateFrom;
    }

    public void setIssueDateFrom(Timestamp issueDateFrom) {
        this.issueDateFrom = issueDateFrom;
    }

    public Timestamp getIssueDateTo() {
        return issueDateTo;
    }

    public void setIssueDateTo(Timestamp issueDateTo) {
        this.issueDateTo = issueDateTo;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
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
