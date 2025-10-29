/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.sql.Time;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class TimeSlotDto {
    private Time startTime;
    private Time endTime;
    private boolean available; // Always true for available slots in this DTO

    public TimeSlotDto(Time startTime, Time endTime, boolean available) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    public Time getStartTime() { return startTime; }
    public void setStartTime(Time startTime) { this.startTime = startTime; }

    public Time getEndTime() { return endTime; }
    public void setEndTime(Time endTime) { this.endTime = endTime; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "TimeSlotDto{" + "startTime=" + startTime + ", endTime=" + endTime + ", available=" + available + '}';
    }
}