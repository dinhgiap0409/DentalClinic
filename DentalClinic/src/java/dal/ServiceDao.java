/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.Service;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ServiceDao extends DBContext {

    public ServiceDao() {
    }

    private int insertService(Service service) {
        String sql = "INSERT INTO [dbo].[Services]\n"
                + "           ([ServiceName]\n"
                + "           ,[Description]\n"
                + "           ,[Price]\n"
                + "           ,[Duration]\n"
                + "           ,[IsActive]\n"
                + "           ,[CreatedBy]\n"
                + "           ,[CreatedDate])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?)";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());
            ps.setInt(4, service.getDuration());
            ps.setBoolean(5, service.isIsActive());
            ps.setInt(6, service.getCreatedBy());
            ps.setTimestamp(7, service.getCreatedDate());
            int row = ps.executeUpdate();
            if (row > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean updateService(Service service) {
        String sql = "UPDATE [dbo].[Services]\n"
                + "   SET [ServiceName] = ?,\n"
                + "       [Description] = ?,\n"
                + "       [Price] = ?,\n"
                + "       [Duration] = ?,\n"
                + "       [IsActive] = ?,\n"
                + "       [CreatedBy] = ?,\n"
                + "       [CreatedDate] = ?\n"
                + " WHERE ServiceID = ?";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());
            ps.setInt(4, service.getDuration());
            ps.setBoolean(5, service.isIsActive());
            ps.setInt(6, service.getCreatedBy());
            ps.setTimestamp(7, service.getCreatedDate());
            ps.setInt(8, service.getServiceId());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Service getServiceById(int serviceId) {
        String sql = "SELECT ServiceID, ServiceName, Description, Price, Duration, "
                + "IsActive, CreatedBy, CreatedDate "
                + "FROM dbo.Services WHERE ServiceID = ?";

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceID"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setDescription(rs.getString("Description"));
                    service.setPrice(rs.getBigDecimal("Price"));
                    service.setDuration(rs.getInt("Duration"));
                    service.setIsActive(rs.getBoolean("IsActive"));
                    service.setCreatedBy(rs.getInt("CreatedBy"));
                    service.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    return service;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
