/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.AppointmentServices;

/**
 *
 * @author TNO
 */
public class AppointmentServicesDao {

    public int insertAppointmentService(AppointmentServices appService) {
        String sql = "INSERT INTO AppointmentServices "
                + "(AppointmentID, ServiceID, Notes) "
                + "VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // set param
            ps.setInt(1, appService.getAppointmentId().getAppointmentId()); // lấy id từ object Appointments
            ps.setInt(2, appService.getServiceId().getServiceId());         // lấy id từ object Service
            ps.setString(3, appService.getNotes());

            int row = ps.executeUpdate();
            if (row > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // trả về AppointmentServiceID mới
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // nếu insert lỗi
    }
}
