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
public class Reviews {

    private int reviewId;
    private Patients patientId;        // PatientID (FK -> Patients)
    private Integer doctorId;     // DoctorID (nullable)
    private Integer appointmentId; // AppointmentID (nullable)
    private int rating;
    private String comment;
    private Timestamp reviewDate;
    private boolean isApproved;

    public Reviews() {
    }

    public Reviews(int reviewId, Patients patientId, Integer doctorId, Integer appointmentId, int rating, String comment, Timestamp reviewDate, boolean isApproved) {
        this.reviewId = reviewId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.isApproved = isApproved;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public Patients getPatientId() {
        return patientId;
    }

    public void setPatientId(Patients patientId) {
        this.patientId = patientId;
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Timestamp reviewDate) {
        this.reviewDate = reviewDate;
    }

    public boolean isIsApproved() {
        return isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    @Override
    public String toString() {
        return "Reviews{" + "reviewId=" + reviewId + ", patientId=" + patientId + ", doctorId=" + doctorId + ", appointmentId=" + appointmentId + ", rating=" + rating + ", comment=" + comment + ", reviewDate=" + reviewDate + ", isApproved=" + isApproved + '}';
    }

}
