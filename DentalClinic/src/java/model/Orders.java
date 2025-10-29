/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 *
 * @author Nguyen Dinh Giap
 */
public class Orders {
      private int orderId;            
    private int patientId;           // PatientID (FK -> Patients)
    private Integer prescriptionId;  
    private Timestamp orderDate;     
    private BigDecimal totalAmount;  
    private String status;           
    private String paymentMethod;    
    private String paymentStatus;  

    public Orders() {
    }

    public Orders(int orderId, int patientId, Integer prescriptionId, Timestamp orderDate, BigDecimal totalAmount, String status, String paymentMethod, String paymentStatus) {
        this.orderId = orderId;
        this.patientId = patientId;
        this.prescriptionId = prescriptionId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Integer getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(Integer prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Orders{" + "orderId=" + orderId + ", patientId=" + patientId + ", prescriptionId=" + prescriptionId + ", orderDate=" + orderDate + ", totalAmount=" + totalAmount + ", status=" + status + ", paymentMethod=" + paymentMethod + ", paymentStatus=" + paymentStatus + '}';
    }
    
}
