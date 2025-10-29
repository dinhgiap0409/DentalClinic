/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Timestamp;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class Notifications {

    private int notificationId;
    private Users userId;            // UserID (FK -> Users)
    private String title;
    private String message;
    private String type;
    private boolean isRead;
    private Timestamp createdDate;
    private Timestamp scheduledDate;

    public Notifications() {
    }

    public Notifications(int notificationId, Users userId, String title, String message, String type, boolean isRead, Timestamp createdDate, Timestamp scheduledDate) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.title = title;
        this.message = message;
        this.type = type;
        this.isRead = isRead;
        this.createdDate = createdDate;
        this.scheduledDate = scheduledDate;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Timestamp scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    @Override
    public String toString() {
        return "Notifications{" + "notificationId=" + notificationId + ", userId=" + userId + ", title=" + title + ", message=" + message + ", type=" + type + ", isRead=" + isRead + ", createdDate=" + createdDate + ", scheduledDate=" + scheduledDate + '}';
    }

}
