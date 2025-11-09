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
public class Schedules {

    private int scheduleId;
    private Doctor doctorId;
    private int dayOfWeek;
    private Time startTime;
    private Time endTime;
    private boolean isAvailable;
    private int maxAppointments;
    private Date validFrom;
    private Date validTo;
    private boolean requiresApproval;
    private boolean isApproved;
    private Users approvedBy;
    private Timestamp approvedDate;
    private Timestamp createdDate;

    public Schedules() {
    }

    public Schedules(int scheduleId, Doctor doctorId, int dayOfWeek, Time startTime, Time endTime, boolean isAvailable, int maxAppointments, Date validFrom, Date validTo, boolean requiresApproval, boolean isApproved, Users approvedBy, Timestamp approvedDate, Timestamp createdDate) {
        this.scheduleId = scheduleId;
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.maxAppointments = maxAppointments;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.requiresApproval = requiresApproval;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
        this.createdDate = createdDate;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public int getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(int maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Users getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Users approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Timestamp getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Timestamp approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Schedules{" + "scheduleId=" + scheduleId + ", doctorId=" + doctorId + ", dayOfWeek=" + dayOfWeek + ", startTime=" + startTime + ", endTime=" + endTime + ", isAvailable=" + isAvailable + ", maxAppointments=" + maxAppointments + ", validFrom=" + validFrom + ", validTo=" + validTo + ", requiresApproval=" + requiresApproval + ", isApproved=" + isApproved + ", approvedBy=" + approvedBy + ", approvedDate=" + approvedDate + ", createdDate=" + createdDate + '}';
    }

}
