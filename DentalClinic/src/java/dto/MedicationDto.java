/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.math.BigDecimal;
import java.sql.*;


public class MedicationDto {
    private Integer medicationId;     // tuỳ chọn: lọc theo ID cụ thể
    private String name;              // MedicationName (LIKE)
    private BigDecimal priceFrom;     // >=
    private BigDecimal priceTo;       // <=
    private Date expiryDateFrom;      // >=
    private Date expiryDateTo;        // <=
    private String manufacturer;      // Manufacturer (LIKE)
    private Boolean isActive;         // true/false

    // Phân trang & sắp xếp
    private boolean paginationMode = true;
    private boolean sortMode = true;   // nếu true: ORDER BY ExpiryDate ASC, Name ASC; else: ORDER BY MedicationID DESC
    private int page = 1;
    private int size = 10;

    public MedicationDto() {
    }

    public MedicationDto(Integer medicationId, String name, BigDecimal priceFrom, BigDecimal priceTo, Date expiryDateFrom, Date expiryDateTo, String manufacturer, Boolean isActive) {
        this.medicationId = medicationId;
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.expiryDateFrom = expiryDateFrom;
        this.expiryDateTo = expiryDateTo;
        this.manufacturer = manufacturer;
        this.isActive = isActive;
    }

    public Integer getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Integer medicationId) {
        this.medicationId = medicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(BigDecimal priceFrom) {
        this.priceFrom = priceFrom;
    }

    public BigDecimal getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(BigDecimal priceTo) {
        this.priceTo = priceTo;
    }

    public Date getExpiryDateFrom() {
        return expiryDateFrom;
    }

    public void setExpiryDateFrom(Date expiryDateFrom) {
        this.expiryDateFrom = expiryDateFrom;
    }

    public Date getExpiryDateTo() {
        return expiryDateTo;
    }

    public void setExpiryDateTo(Date expiryDateTo) {
        this.expiryDateTo = expiryDateTo;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isPaginationMode() {
        return paginationMode;
    }

    public void setPaginationMode(boolean paginationMode) {
        this.paginationMode = paginationMode;
    }

    public boolean isSortMode() {
        return sortMode;
    }

    public void setSortMode(boolean sortMode) {
        this.sortMode = sortMode;
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
    
    
    
    
    
}
