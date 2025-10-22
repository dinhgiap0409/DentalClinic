/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.*;
/**
 *
 * @author Nguyen Dinh Giap
 */
public class NotificationsDao extends DBContext {

    public boolean insert(int userId, String title, String message, String type) {
        String sql = """
            INSERT INTO Notifications(UserID, Title, Message, Type, IsRead, CreatedDate)
            VALUES (?, ?, ?, ?, 0, GETDATE())
        """;
        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setString(4, type); // 'Appointment'
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
