/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class SchedulePermissions {

    private int permissionId;
    private Doctor doctorId;              // DoctorID (FK -> Doctors)
    private boolean canEditOwnSchedule;
    private boolean canEditOtherSchedule;
    private boolean requiresApproval;
    private Integer updatedBy;             // UpdatedBy (nullable, FK -> Users)
    private Timestamp updatedDate;

    public SchedulePermissions() {
    }

    public SchedulePermissions(int permissionId, Doctor doctorId, boolean canEditOwnSchedule, boolean canEditOtherSchedule, boolean requiresApproval, Integer updatedBy, Timestamp updatedDate) {
        this.permissionId = permissionId;
        this.doctorId = doctorId;
        this.canEditOwnSchedule = canEditOwnSchedule;
        this.canEditOtherSchedule = canEditOtherSchedule;
        this.requiresApproval = requiresApproval;
        this.updatedBy = updatedBy;
        this.updatedDate = updatedDate;
    }

    public int getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(int permissionId) {
        this.permissionId = permissionId;
    }

    public Doctor getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Doctor doctorId) {
        this.doctorId = doctorId;
    }

    public boolean isCanEditOwnSchedule() {
        return canEditOwnSchedule;
    }

    public void setCanEditOwnSchedule(boolean canEditOwnSchedule) {
        this.canEditOwnSchedule = canEditOwnSchedule;
    }

    public boolean isCanEditOtherSchedule() {
        return canEditOtherSchedule;
    }

    public void setCanEditOtherSchedule(boolean canEditOtherSchedule) {
        this.canEditOtherSchedule = canEditOtherSchedule;
    }

    public boolean isRequiresApproval() {
        return requiresApproval;
    }

    public void setRequiresApproval(boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public String toString() {
        return "SchedulePermissions{" + "permissionId=" + permissionId + ", doctorId=" + doctorId + ", canEditOwnSchedule=" + canEditOwnSchedule + ", canEditOtherSchedule=" + canEditOtherSchedule + ", requiresApproval=" + requiresApproval + ", updatedBy=" + updatedBy + ", updatedDate=" + updatedDate + '}';
    }

}
