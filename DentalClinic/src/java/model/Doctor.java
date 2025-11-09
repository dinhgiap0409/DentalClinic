/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Doctor {

    private int doctorID;
    private Users userId;
    private String specialization;
    private String licenseNumber;
    private int yearsOfExperience;
    private String education;
    private String biography;
    private BigDecimal consultationFee;

    public Doctor() {
    }

    public Doctor(int doctorID) {
        this.doctorID = doctorID;
    }

    public Doctor(int doctorID, Users userId, String specialization, String licenseNumber, int yearsOfExperience, String education, String biography, BigDecimal consultationFee) {
        this.doctorID = doctorID;
        this.userId = userId;
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
        this.yearsOfExperience = yearsOfExperience;
        this.education = education;
        this.biography = biography;
        this.consultationFee = consultationFee;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    @Override
    public String toString() {
        return "Doctor{" + "doctorID=" + doctorID + ", userId=" + userId + ", specialization=" + specialization + ", licenseNumber=" + licenseNumber + ", yearsOfExperience=" + yearsOfExperience + ", education=" + education + ", biography=" + biography + ", consultationFee=" + consultationFee + '}';
    }

}
