/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ScheduleExceptions {

    private int exceptionId;
    private Doctor doctorId;
    private Date exceptionDate;
    private String reason;
    private boolean isWorkingDay;
    private Time startTime;
    private Time endTime;
    private Integer maxAppointments;

    public ScheduleExceptions() {
    }

    public ScheduleExceptions(int exceptionId, Doctor doctorId, Date exceptionDate, String reason, boolean isWorkingDay, Time startTime, Time endTime, Integer maxAppointments) {
        this.exceptionId = exceptionId;
        this.doctorId = doctorId;
        this.exceptionDate = exceptionDate;
        this.reason = reason;
        this.isWorkingDay = isWorkingDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxAppointments = maxAppointments;
    }

    public int getExceptionId() {
        return exceptionId;
    }

    public void setExceptionId(int exceptionId) {
        this.exceptionId = exceptionId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isIsWorkingDay() {
        return isWorkingDay;
    }

    public void setIsWorkingDay(boolean isWorkingDay) {
        this.isWorkingDay = isWorkingDay;
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

    public Integer getMaxAppointments() {
        return maxAppointments;
    }

    public void setMaxAppointments(Integer maxAppointments) {
        this.maxAppointments = maxAppointments;
    }

    @Override
    public String toString() {
        return "ScheduleExceptions{" + "exceptionId=" + exceptionId + ", doctorId=" + doctorId + ", exceptionDate=" + exceptionDate + ", reason=" + reason + ", isWorkingDay=" + isWorkingDay + ", startTime=" + startTime + ", endTime=" + endTime + ", maxAppointments=" + maxAppointments + '}';
    }

}
