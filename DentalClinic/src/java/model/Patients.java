/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Patients {

    private int patientID;
    private Users userID;
    private String bloodType;
    private String allergies;
    private String medicalHistory;
    private String insuranceInfo;
    private String emergencyContactName;
    private String emergencyContactPhone;

    public Patients() {
    }
    
    public Patients(int patientID) {
        this.patientID = patientID;    
    }

    public Patients(int patientID, Users userID, String bloodType, String allergies, String medicalHistory, String insuranceInfo, String emergencyContactName, String emergencyContactPhone) {
        this.patientID = patientID;
        this.userID = userID;
        this.bloodType = bloodType;
        this.allergies = allergies;
        this.medicalHistory = medicalHistory;
        this.insuranceInfo = insuranceInfo;
        this.emergencyContactName = emergencyContactName;
        this.emergencyContactPhone = emergencyContactPhone;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        this.patientID = patientID;
    }

    public Users getUserID() {
        return userID;
    }

    public void setUserID(Users userID) {
        this.userID = userID;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getInsuranceInfo() {
        return insuranceInfo;
    }

    public void setInsuranceInfo(String insuranceInfo) {
        this.insuranceInfo = insuranceInfo;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public String getEmergencyContactPhone() {
        return emergencyContactPhone;
    }

    public void setEmergencyContactPhone(String emergencyContactPhone) {
        this.emergencyContactPhone = emergencyContactPhone;
    }

    @Override
    public String toString() {
        return "Patients{" + "patientID=" + patientID + ", userID=" + userID + ", bloodType=" + bloodType + ", allergies=" + allergies + ", medicalHistory=" + medicalHistory + ", insuranceInfo=" + insuranceInfo + ", emergencyContactName=" + emergencyContactName + ", emergencyContactPhone=" + emergencyContactPhone + '}';
    }

}
