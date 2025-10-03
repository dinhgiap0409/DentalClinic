/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import model.Doctor;
import model.Service;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ServiceDto {

    private Service service;
    private Users creator;
    private Doctor doctror;

    public ServiceDto() {
    }

    public ServiceDto(Service service, Users creator, Doctor doctror) {
        this.service = service;
        this.creator = creator;
        this.doctror = doctror;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public Users getCreator() {
        return creator;
    }

    public void setCreator(Users creator) {
        this.creator = creator;
    }

    public Doctor getDoctror() {
        return doctror;
    }

    public void setDoctror(Doctor doctror) {
        this.doctror = doctror;
    }

}
