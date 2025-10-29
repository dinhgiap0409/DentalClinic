/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;
import java.sql.Date;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Medications {

    private int medicationId;
    private String medicationName;
    private String description;
    private BigDecimal price;
    private int stockQuantity;
    private Date expiryDate;
    private String manufacturer;
    private boolean isActive;

    public Medications() {
    }

    public Medications(int medicationId, String medicationName, String description, BigDecimal price, int stockQuantity, Date expiryDate, String manufacturer, boolean isActive) {
        this.medicationId = medicationId;
        this.medicationName = medicationName;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.expiryDate = expiryDate;
        this.manufacturer = manufacturer;
        this.isActive = isActive;
    }

    public int getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(int medicationId) {
        this.medicationId = medicationId;
    }

    public String getMedicationName() {
        return medicationName;
    }

    public void setMedicationName(String medicationName) {
        this.medicationName = medicationName;
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

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "Medications{" + "medicationId=" + medicationId + ", medicationName=" + medicationName + ", description=" + description + ", price=" + price + ", stockQuantity=" + stockQuantity + ", expiryDate=" + expiryDate + ", manufacturer=" + manufacturer + ", isActive=" + isActive + '}';
    }
}
