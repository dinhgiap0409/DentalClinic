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
public class ScheduleExceptionsDto {

    private Integer DoctorId;
    private Date exceptionDate;
    private Boolean isWorkingDay;
    private Time startTime;
    private Time endTime;
    private Integer maxAppointment;
    private boolean sortMode;
    private boolean paginationMode;
    private int page = 1;
    private int size = 10;

    public ScheduleExceptionsDto() {
    }

    public ScheduleExceptionsDto(Integer DoctorId, Date exceptionDate, Boolean isWorkingDay, Time startTime, Time endTime, Integer maxAppointment, boolean sortMode, boolean paginationMode) {
        this.DoctorId = DoctorId;
        this.exceptionDate = exceptionDate;
        this.isWorkingDay = isWorkingDay;
        this.startTime = startTime;
        this.endTime = endTime;
        this.maxAppointment = maxAppointment;
        this.sortMode = sortMode;
        this.paginationMode = paginationMode;
    }

    public Integer getDoctorId() {
        return DoctorId;
    }

    public void setDoctorId(Integer DoctorId) {
        this.DoctorId = DoctorId;
    }

    public Date getExceptionDate() {
        return exceptionDate;
    }

    public void setExceptionDate(Date exceptionDate) {
        this.exceptionDate = exceptionDate;
    }

    public Boolean getIsWorkingDay() {
        return isWorkingDay;
    }

    public void setIsWorkingDay(Boolean isWorkingDay) {
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

    public Integer getMaxAppointment() {
        return maxAppointment;
    }

    public void setMaxAppointment(Integer maxAppointment) {
        this.maxAppointment = maxAppointment;
    }

    public boolean isSortMode() {
        return sortMode;
    }

    public void setSortMode(boolean sortMode) {
        this.sortMode = sortMode;
    }

    public boolean isPaginationMode() {
        return paginationMode;
    }

    public void setPaginationMode(boolean paginationMode) {
        this.paginationMode = paginationMode;
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
