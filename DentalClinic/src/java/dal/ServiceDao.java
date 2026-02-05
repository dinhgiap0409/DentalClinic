/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.ServiceDto;
import dto.ServiceDto;
import java.math.BigDecimal;
import java.sql.*;
import model.Doctor;
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
        String sql = "SELECT s.ServiceID, s.ServiceName, s.Description, s.Price, "
                + "s.Duration, s.IsActive, s.CreatedBy, s.CreatedDate, "
                + "u.FullName "
                + "FROM dbo.Services s "
                + "LEFT JOIN dbo.Users u ON s.CreatedBy = u.UserID "
                + "ORDER BY s.ServiceID";
        List<Service> services = new ArrayList<>();

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
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

    public int countServices() {
        String sql = "SELECT COUNT(*) FROM dbo.Services";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Service> getServicesPage(int page, int size) {
        int offset = (page - 1) * size;

        String sql = """
        SELECT s.ServiceID, s.ServiceName, s.Description, s.Price,
               s.Duration, s.IsActive, s.CreatedBy, s.CreatedDate,
               u.FullName
        FROM dbo.Services s
        LEFT JOIN dbo.Users u ON s.CreatedBy = u.UserID
        ORDER BY s.ServiceID
        OFFSET ? ROWS FETCH NEXT ? ROWS ONLY
    """;

        List<Service> services = new ArrayList<>();
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, offset);
            ps.setInt(2, size);

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

    public Service findServiceByID(int id) {
        return getServiceById(id);
    }

    public List<ServiceDto> filterService(ServiceDto filter) {
        List<ServiceDto> list = new ArrayList<>();
        //cau lenh sql truy van join 3 bang user, doctor, service
        //lay thong tin ve cac dich vu, nguoi tao dich vu va thong tin doctor lien quanl
        StringBuilder sql = new StringBuilder("""
        SELECT s.ServiceID, s.ServiceName, s.[Description], s.Price, s.Duration,
               s.IsActive, s.CreatedBy, s.CreatedDate,
               u.UserID AS U_UserID, u.FullName AS U_FullName,
               d.DoctorID AS D_DoctorID, d.Specialization
        FROM dbo.Services s
        LEFT JOIN dbo.Users u ON s.CreatedBy = u.UserID
        LEFT JOIN dbo.Doctors d ON d.UserID = u.UserID
        WHERE 1=1
    """);
        //neu nguoi dung nhap theo ten thi loc theo ten
        if(filter.getServiceName() != null && !filter.getServiceName().isBlank()){
             sql.append(" AND s.ServiceName LIKE ?\n");
        }
        //nguoi dung muon gia thap nhat
        if(filter.getPriceFrom()!=null){
            sql.append(" AND s.Price >= ?\n");
        }
        //nguoi dung muon gia cao nhat
        if(filter.getPriceTo()!= null){
            sql.append(" AND s.Price <= ?\n");
        }
        //chon trang thai hoat dong online hoac off
        if(filter.getIsActive()!=null){
            sql.append(" AND s.IsActive = ?\n");
        }
        //sortMode nay tra ve true thi asc va false thi decs
        if(filter.isSortMode()){
            sql.append(" ORDER BY s.ServiceID ASC");
        }
        //dieu kien de phan trang
        if(filter.isPaginationMode()){
            if(!filter.isSortMode()){
                sql.append(" ORDER BY s.ServiceID ASC");
            }
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }
        //mo ket noi
        try (Connection connect = new DBContext().connection;
                PreparedStatement ps = connect.prepareStatement(sql.toString())){
            //giup nhay sang dau ? hop le
            int index = 1;
            if(filter.getServiceName() != null && !filter.getServiceName().isBlank()){
                ps.setString(index++,"%"+filter.getServiceName()+"%");
            }
            if(filter.getPriceFrom() != null){
                ps.setBigDecimal(index++, java.math.BigDecimal.valueOf(filter.getPriceFrom()));
            }
            if(filter.getPriceTo() != null){
                ps.setBigDecimal(index++, java.math.BigDecimal.valueOf(filter.getPriceTo()));
            }
            if(filter.getIsActive() != null){
                ps.setBoolean(index++, filter.getIsActive());
            }
            if(filter.isPaginationMode()){
                int offset = (filter.getPage() - 1) * filter.getSize();
                ps.setInt(index++, offset);
                ps.setInt(index++, filter.getSize());
            }
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ServiceDto dto = new ServiceDto();
                    
                    // Set Service information
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceID"));
                    service.setServiceName(rs.getString("ServiceName"));
                    service.setDescription(rs.getString("Description"));
                    service.setPrice(rs.getBigDecimal("Price"));
                    service.setDuration(rs.getInt("Duration"));
                    service.setIsActive(rs.getBoolean("IsActive"));
                    service.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    
                    // Set CreatedBy thong tin cua users
                    int createdById = rs.getInt("CreatedBy");
                    if (!rs.wasNull()) {
                        Users user = new Users();
                        user.setUserId(rs.getInt("U_UserID"));
                        user.setFullName(rs.getString("U_FullName"));
                        service.setCreatedBy(user);
                    }
                    
                    dto.setService(service);
                    dto.setCreator(service.getCreatedBy());
                    
                    // Set thong tin cua doctor
                    int doctorId = rs.getInt("D_DoctorID");
                    if (!rs.wasNull()) {
                        Doctor doctor = new Doctor();
                        doctor.setDoctorID(doctorId);
                        doctor.setSpecialization(rs.getString("Specialization"));
                        doctor.setUserId(service.getCreatedBy());
                        dto.setDoctror(doctor);
                    }
                    
                    list.add(dto);
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
