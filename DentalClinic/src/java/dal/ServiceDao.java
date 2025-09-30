/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.Service;
import model.Users;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ServiceDao extends DBContext {

    public ServiceDao() {
    }

    private void setCreatedByParam(PreparedStatement ps, int index, Users createdBy) throws SQLException {
        if (createdBy != null && createdBy.getUserId() > 0) {
            ps.setInt(index, createdBy.getUserId());
        } else {
            ps.setNull(index, Types.INTEGER);
        }
    }

    public int insertService(Service service) {
        String sql = "INSERT INTO [dbo].[Services]\n"
                + "           ([ServiceName]\n"
                + "           ,[Description]\n"
                + "           ,[Price]\n"
                + "           ,[Duration]\n"
                + "           ,[IsActive]\n"
                + "           ,[CreatedBy])\n"
                + "     VALUES\n"
                + "           (?\n"
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
            setCreatedByParam(ps, 6, service.getCreatedBy());
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
                + "       [IsActive] = ?\n"
                + " WHERE ServiceID = ?";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, service.getServiceName());
            ps.setString(2, service.getDescription());
            ps.setBigDecimal(3, service.getPrice());
            ps.setInt(4, service.getDuration());
            ps.setBoolean(5, service.isIsActive());
            ps.setInt(6, service.getServiceId());
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
                    int createdById = rs.getInt("CreatedBy");
                    if (!rs.wasNull()) {
                        Users u = new Users();
                        u.setUserId(createdById);
                        service.setCreatedBy(u);
                    } else {
                        service.setCreatedBy(null);
                    }
                    service.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    return service;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Service> getAllServices() {
        String sql = "SELECT s.ServiceID, s.ServiceName, s.Description, s.Price, " +
                     "s.Duration, s.IsActive, s.CreatedBy, s.CreatedDate, " +
                     "u.FullName " +
                     "FROM dbo.Services s " +
                     "LEFT JOIN dbo.Users u ON s.CreatedBy = u.UserID " +
                     "ORDER BY s.ServiceID";
        List<Service> services = new ArrayList<>();
        
        try (Connection connect = new DBContext().connection; 
             PreparedStatement ps = connect.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceID"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setDescription(rs.getString("Description"));
                    service.setPrice(rs.getBigDecimal("Price"));
                    service.setDuration(rs.getInt("Duration"));
                    service.setIsActive(rs.getBoolean("IsActive"));
                    service.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    
                    int createdById = rs.getInt("CreatedBy");
                    if (!rs.wasNull()) {
                        Users user = new Users();
                        user.setUserId(createdById);
                        user.setFullName(rs.getString("FullName"));
                        service.setCreatedBy(user);
                    }
                    
                    services.add(service);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return services;
    }

    public boolean deleteService(int serviceId) {
        String sql = "DELETE FROM dbo.Services WHERE ServiceID = ?";
        try (Connection connect = new DBContext().connection; 
             PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
