/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author TNO
 */
public class AppointmentServices {
    private int appointmentServiceID;
    private Appointments appointmentId; 
    private Service serviceId;        
    private String notes;

    public AppointmentServices() {
    }

    public AppointmentServices(int appointmentServiceID, Appointments appointmentId, Service serviceId, String notes) {
        this.appointmentServiceID = appointmentServiceID;
        this.appointmentId = appointmentId;
        this.serviceId = serviceId;
        this.notes = notes;
    }

    public int getAppointmentServiceID() {
        return appointmentServiceID;
    }

    public void setAppointmentServiceID(int appointmentServiceID) {
        this.appointmentServiceID = appointmentServiceID;
    }

    public Appointments getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Appointments appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
    
}
