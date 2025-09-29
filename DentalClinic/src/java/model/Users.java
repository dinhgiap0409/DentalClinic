/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author Nguyen Dinh Giap
 */
import java.sql.Date;

public class Users {

    private int userId;
    private String userName;
    private String passWord;
    private String email;
    private String fullName;
    private String phoneNumber;
    private Date dateOfBirth;
    private String gender;
    private String address;
    private String role;
    private boolean isActive;
    private Date createDate;

    public Users() {
    }

    public Users(int userId, String userName, String passWord, String email, String fullName, String phoneNumber, Date dateOfBirth, String gender, String address, String role, boolean isActive, Date createDate) {
        this.userId = userId;
        this.userName = userName;
        this.passWord = passWord;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
        this.role = role;
        this.isActive = isActive;
        this.createDate = createDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "Users{" + "userId=" + userId + ", userName=" + userName + ", passWord=" + passWord + ", email=" + email + ", fullName=" + fullName + ", phoneNumber=" + phoneNumber + ", dateOfBirth=" + dateOfBirth + ", gender=" + gender + ", address=" + address + ", role=" + role + ", isActive=" + isActive + ", createDate=" + createDate + '}';
    }

}
