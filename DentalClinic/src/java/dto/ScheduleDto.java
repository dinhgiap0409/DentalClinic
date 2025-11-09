/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Time;
import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ScheduleDto {

    // ===== Thông tin lọc chính =====
    private Integer doctorId;         // Lọc theo ID bác sĩ
    private String doctorName;        // Lọc theo tên bác sĩ (UserName hoặc FullName)
    private Integer dayOfWeek;        // Lọc theo ngày trong tuần (1-7)
    private Time startTime;           // Lọc theo giờ bắt đầu >= startTime
    private Time endTime;             // Lọc theo giờ kết thúc <= endTime
    private Boolean isAvailable;      // Lọc theo trạng thái có sẵn
    private Boolean requiresApproval; // Lọc theo yêu cầu phê duyệt
    private Boolean isApproved;       // Lọc theo trạng thái phê duyệt

    // ===== Thông tin khác (tùy chọn) =====
    private Integer maxAppointment;
    private Date validFrom;
    private Date validTo;
    private Integer approvedBy;
    private Timestamp approvedDate;
    private Timestamp createdDate;

    // ===== Phân trang & sắp xếp =====
    private boolean sortMode = true;       // true = ASC, false = DESC
    private boolean paginationMode = false; // true = bật phân trang
    private int page = 1;                   // số trang hiện tại
    private int size = 10;                  // số bản ghi trên 1 trang

    public ScheduleDto() {
    }

    public ScheduleDto(Integer doctorId, String doctorName, Integer dayOfWeek, Time startTime, Time endTime, Boolean isAvailable, Boolean requiresApproval, Boolean isApproved, Integer maxAppointment, Date validFrom, Date validTo, Integer approvedBy, Timestamp approvedDate, Timestamp createdDate) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isAvailable = isAvailable;
        this.requiresApproval = requiresApproval;
        this.isApproved = isApproved;
        this.maxAppointment = maxAppointment;
        this.validFrom = validFrom;
        this.validTo = validTo;
        this.approvedBy = approvedBy;
        this.approvedDate = approvedDate;
        this.createdDate = createdDate;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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
