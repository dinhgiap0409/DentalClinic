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
    private String serviceName;
    private Double priceFrom;
    private Double priceTo;
    private Boolean isActive;
    private boolean sortMode;
    private boolean paginationMode;
    private int page = 1;
    private int size = 10;
     private Service service;
    private Users creator;
    private Doctor doctror;

    public ServiceDto() {
    }

    public ServiceDto(String serviceName, Double priceFrom, Double priceTo, Boolean isActive, boolean sortMode, boolean paginationMode) {
        this.serviceName = serviceName;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.isActive = isActive;
        this.sortMode = sortMode;
        this.paginationMode = paginationMode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
