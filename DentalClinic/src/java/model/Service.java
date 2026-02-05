/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.*;
import java.math.BigDecimal;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Service {

    private int serviceId;
    private String serviceName;
    private String description;
    private BigDecimal price;
    private int duration;
    private boolean isActive;
    private Users createdBy;
    private Timestamp createdDate;

    public Service() {
    }

    public Service(int serviceId) {
        this.serviceId = serviceId;
    }

    public Service(int serviceId, String serviceName, String description, BigDecimal price, int duration, boolean isActive, Users createdBy, Timestamp createdDate) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.isActive = isActive;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Users getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Users createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Service{" + "serviceId=" + serviceId + ", serviceName=" + serviceName + ", description=" + description + ", price=" + price + ", duration=" + duration + ", isActive=" + isActive + ", createdBy=" + createdBy + ", createdDate=" + createdDate + '}';
    }

}
