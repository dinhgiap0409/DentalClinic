/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;
import java.math.BigDecimal;
import java.sql.*;

public class OrderDto {
    // Thuộc tính Orders
    private Integer orderId;
    private Integer prescriptionId;
    private Date orderDateFrom;
    private Date orderDateTo;
    private BigDecimal totalAmountFrom;
    private BigDecimal totalAmountTo;
    private String status;
    private String paymentStatus;

    private Integer patientId;
    private String patientName;
    
    private boolean paginationMode = true;
    private boolean sortMode = false; 
    private int page = 1;
    private int size = 10;

    public OrderDto() {
    }

    public OrderDto(Integer orderId, Integer prescriptionId, Date orderDateFrom, Date orderDateTo, BigDecimal totalAmountFrom, BigDecimal totalAmountTo, String status, String paymentStatus, Integer patientId, String patientName) {
        this.orderId = orderId;
        this.prescriptionId = prescriptionId;
        this.orderDateFrom = orderDateFrom;
        this.orderDateTo = orderDateTo;
        this.totalAmountFrom = totalAmountFrom;
        this.totalAmountTo = totalAmountTo;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.patientId = patientId;
        this.patientName = patientName;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Date getOrderDateFrom() {
        return orderDateFrom;
    }

    public void setOrderDateFrom(Date orderDateFrom) {
        this.orderDateFrom = orderDateFrom;
    }

    public Date getOrderDateTo() {
        return orderDateTo;
    }

    public void setOrderDateTo(Date orderDateTo) {
        this.orderDateTo = orderDateTo;
    }

    public BigDecimal getTotalAmountFrom() {
        return totalAmountFrom;
    }

    public void setTotalAmountFrom(BigDecimal totalAmountFrom) {
        this.totalAmountFrom = totalAmountFrom;
    }

    public BigDecimal getTotalAmountTo() {
        return totalAmountTo;
    }

    public void setTotalAmountTo(BigDecimal totalAmountTo) {
        this.totalAmountTo = totalAmountTo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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
