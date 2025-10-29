/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.math.BigDecimal;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class OrderDetails {

    private int orderDetailId;
    private Orders orderId;         // OrderID (FK -> Orders)
    private Medications medicationId;    // MedicationID (FK -> Medications)
    private int quantity;
    private BigDecimal price;

    public OrderDetails() {
    }

    public OrderDetails(int orderDetailId, Orders orderId, Medications medicationId, int quantity, BigDecimal price) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.medicationId = medicationId;
        this.quantity = quantity;
        this.price = price;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public Orders getOrderId() {
        return orderId;
    }

    public void setOrderId(Orders orderId) {
        this.orderId = orderId;
    }

    public Medications getMedicationId() {
        return medicationId;
    }

    public void setMedicationId(Medications medicationId) {
        this.medicationId = medicationId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "OrderDetails{" + "orderDetailId=" + orderDetailId + ", orderId=" + orderId + ", medicationId=" + medicationId + ", quantity=" + quantity + ", price=" + price + '}';
    }

}
