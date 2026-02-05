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
public class ScheduleDto {
    //su dung wrapper mac dinh = null 
    //khi nguoi dung ko truyen dieu kien thi tra ve null
    private Integer doctorId; 
    private Integer dayOfWeek;
    private Time startTime;
    private Time endTime;
    private Boolean isAvailable;
    private Integer maxAppointment;
    private Date validFrom;
    private Date validTo;
    private Boolean requiresApproval; 
    private Boolean isApproved;
    private Integer approvedBy;
    private Timestamp approvedDate;
    private Timestamp createdDate;

    //phan trang
    private boolean sortMode;
    private boolean paginationMode;
    private int page = 1;
    private int size = 10;

    public ScheduleDto() {
    }

    public ScheduleDto(Integer doctorId, Integer dayOfWeek, Time startTime, Time endTime, Boolean isAvailable, Integer maxAppointment, Date validFrom, Date validTo, Boolean requiresApproval, Boolean isApproved, Integer approvedBy, Timestamp approvedDate, Timestamp createdDate, boolean sortMode, boolean paginationMode) {
        this.doctorId = doctorId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.maxAppointment = maxAppointment;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.requiresApproval = requiresApproval;
        this.isApproved = isApproved;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
        this.createdDate = createdDate;
        this.sortMode = sortMode;
        this.paginationMode = paginationMode;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
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

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Integer getMaxAppointment() {
        return maxAppointment;
    }

    public void setMaxAppointment(Integer maxAppointment) {
        this.maxAppointment = maxAppointment;
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

    public Boolean getRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Boolean getIsApproved() {
        return isApproved;
    }

    public void setIsApproved(Boolean isApproved) {
        this.isApproved = isApproved;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
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
