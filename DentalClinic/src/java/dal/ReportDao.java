/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DailyRevenue;

/**
 *
 * @author TNO
 */
public class ReportDao extends DBContext {

    // Lấy doanh thu thuốc theo ngày
    public List<DailyRevenue> findAllMedicationRevenue() {
        List<DailyRevenue> list = new ArrayList<>();
        String sql = """
            SELECT 
                CAST(o.OrderDate AS DATE) AS OrderDate,
                SUM(od.Quantity * od.Price) AS DailyMedicationRevenue
            FROM Orders o
            JOIN OrderDetails od ON o.OrderID = od.OrderID
            WHERE o.PaymentStatus = 'Paid'
            GROUP BY CAST(o.OrderDate AS DATE)
            ORDER BY OrderDate;
        """;

        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DailyRevenue(
                        rs.getDate("OrderDate"),
                        rs.getBigDecimal("DailyMedicationRevenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Lấy doanh thu dịch vụ khám theo ngày
    public List<DailyRevenue> findAllServiceRevenue() {
        List<DailyRevenue> list = new ArrayList<>();
        String sql = """
            SELECT 
                a.AppointmentDate,
                SUM(s.Price) AS DailyServiceRevenue
            FROM Appointments a
            JOIN Services s ON a.ServiceID = s.ServiceID
            WHERE a.Status IN ('Completed', 'Confirmed')
            GROUP BY a.AppointmentDate
            ORDER BY a.AppointmentDate;
        """;

        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new DailyRevenue(
                        rs.getDate("AppointmentDate"),
                        rs.getBigDecimal("DailyServiceRevenue")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Tổng doanh thu toàn bộ
    public BigDecimal getTotalRevenue() {
        String sql = """
            SELECT      
                (SELECT SUM(s.Price) 
                 FROM Appointments a
                 JOIN Services s ON a.ServiceID = s.ServiceID
                 WHERE a.Status IN ('Completed', 'Confirmed')
                ) 
                +
                (SELECT SUM(od.Quantity * od.Price) 
                 FROM Orders o
                 JOIN OrderDetails od ON o.OrderID = od.OrderID
                 WHERE o.PaymentStatus = 'Paid'
                ) AS TotalRevenue;
        """;

        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("TotalRevenue");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }
}
