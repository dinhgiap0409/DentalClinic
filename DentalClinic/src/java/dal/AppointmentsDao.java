/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import dto.*;
import dto.AppointmentDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Appointments;
import model.Patients;
import model.Doctor;
import model.Service;
import model.Users;


/**
 *
 * @author Nguyen Dang Khang
 */
public class AppointmentsDao extends DBContext {

    //lay lich hen qua id
    public Appointments getAppointmentsById(int appointmentId) {
        String sql = """
            SELECT AppointmentID, PatientID, DoctorID, ServiceID,
                   AppointmentDate, StartTime, EndTime, Status, Notes, CreatedDate, UpdatedDate
            FROM dbo.Appointments
            WHERE AppointmentID = ?
        """;
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Appointments> filterAppointment(AppointmentDto a) {
        List<Appointments> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT AppointmentID, PatientID, DoctorID, ServiceID,
            AppointmentDate, StartTime, EndTime, Status, Notes, CreatedDate, UpdatedDate
            FROM dbo.Appointments WHERE 1 = 1""");
        if (a.getPatientId() != null) {
            sql.append(" AND PatientID = ?\n");
        }
        if (a.getDoctorId() != null) {
            sql.append(" AND DoctorID = ?\n");
        }
        if (a.getServiceId() != null) {
            sql.append(" AND ServiceID = ?\n");
        }
        if (a.getAppointmentDate() != null) {
            sql.append(" AND AppointmentDate = ?\n");
        }
        if (a.getStartTime() != null) {
            sql.append(" AND StartTime >= ?\n");
        }
        if (a.getEndTime() != null) {
            sql.append(" AND EndTime <= ?\n");
        }
        if (a.getStatus() != null && !a.getStatus().isBlank()) {
            sql.append(" AND Status = ?\n");
        }
        if (a.getCreatedDate() != null) {
            sql.append(" AND CreatedDate >= ?\n");
        }
        if (a.isSortMode()) {
            sql.append(" ORDER BY AppointmentID ASC\n");
        }
        if (a.isPaginationMode()) {
            if (!a.isSortMode()) {
                sql.append(" ORDER BY AppointmentID ASC\n");
            }
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }
        //PatientID, DoctorID, ServiceID,
        //AppointmentDate, StartTime, EndTime, Status, CreatedDate
        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql.toString())) {
            int i = 1;
            if (a.getPatientId() != null) {
                ps.setInt(i++, a.getPatientId());
            }
            if (a.getDoctorId() != null) {
                ps.setInt(i++, a.getDoctorId());
            }
            if (a.getServiceId() != null) {
                ps.setInt(i++, a.getServiceId());
            }
            if (a.getAppointmentDate() != null) {
                ps.setDate(i++, a.getAppointmentDate());
            }
            if (a.getStartTime() != null) {
                ps.setTime(i++, a.getStartTime());
            }
            if (a.getEndTime() != null) {
                ps.setTime(i++, a.getEndTime());
            }
            if (a.getStatus() != null && !a.getStatus().isBlank()) {
                ps.setString(i++, a.getStatus().trim());
            }
            if (a.getCreatedDate() != null) {
                ps.setDate(i++, a.getCreatedDate());
            }

            if (a.isPaginationMode()) {
                int page = Math.max(1, a.getPage());
                int size = Math.max(1, a.getSize());
                ps.setInt(i++, (page - 1) * size);
                ps.setInt(i++, size);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private Appointments mapRow(ResultSet rs) throws SQLException {
        Appointments appointment = new Appointments();
        appointment.setAppointmentId(rs.getInt("AppointmentID"));
        Object patientIdObj = rs.getObject("PatientID");
        if (patientIdObj != null) {
            Patients patient = new Patients();
            patient.setPatientID((Integer) patientIdObj);
            appointment.setPatientId(patient);
        }
        Object doctorIdObj = rs.getObject("DoctorID");
        if (doctorIdObj != null) {
            Doctor doctor = new Doctor();
            doctor.setDoctorID((Integer) doctorIdObj);
            appointment.setDoctorId(doctor);
        }
        Object serviceIdObj = rs.getObject("ServiceID");
        if (serviceIdObj != null) {
            Service service = new Service();
            service.setServiceId((Integer) serviceIdObj);
            appointment.setServiceId(service);
        }
        appointment.setAppointmentDate(rs.getDate("AppointmentDate"));
        appointment.setStartTime(rs.getTime("StartTime"));
        appointment.setEndTime(rs.getTime("EndTime"));
        appointment.setStatus(rs.getString("Status"));
        appointment.setNotes(rs.getString("Notes"));
        appointment.setCreatedDate(rs.getTimestamp("CreatedDate"));
        appointment.setUpdatedDate(rs.getTimestamp("UpdatedDate"));

        return appointment;
    }

    public Integer insertAppointment(Appointments a) {
        //check null cho tung doi tuong phai co doi tuong moi dat duoc lich
        if (a == null || a.getPatientId() == null || a.getDoctorId() == null || a.getServiceId() == null) {
            return null;
        }
        //check ngay thang nam dat lich
        if (a.getAppointmentDate() == null || a.getStartTime() == null || a.getEndTime() == null) {
            return null;
        }
        //start phai dung trc end
        if (!a.getEndTime().after(a.getStartTime())) {
            return null;
        }
        String sql = """
        INSERT INTO dbo.Appointments
            (PatientID, DoctorID, ServiceID,
             AppointmentDate, StartTime, EndTime,
             Status, Notes)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
    """;
        try (Connection cn = new DBContext().connection; PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, a.getPatientId().getPatientID());
            ps.setInt(2, a.getDoctorId().getDoctorID());
            ps.setInt(3, a.getServiceId().getServiceId());
            ps.setDate(4, a.getAppointmentDate());
            ps.setTime(5, a.getStartTime());
            ps.setTime(6, a.getEndTime());
            String status = (a.getStatus() == null || a.getStatus().isBlank())
                    ? "Scheduled" : a.getStatus().trim();
            ps.setString(7, status);
            if (a.getNotes() == null || a.getNotes().isBlank()) {
                ps.setNull(8, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(8, a.getNotes());
            }
            int affected = ps.executeUpdate();
            if (affected == 0) {
                return null;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return null;

        } catch (java.sql.SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //ham check trung lap khoang thoi gian
    public boolean existsDoctorTimeConflict(int doctorId, Date appointmentDate,
            Time newStartTime, Time newEndTime) {
        String sql = """
        SELECT 1
        FROM dbo.Appointments
        WHERE DoctorID = ? AND AppointmentDate = ? 
        AND Status != 'Cancelled'
        AND StartTime < ? AND ? < EndTime
        AND StartTime < ? AND ? < EndTime -- StartTime < newEndTime AND newStartTime < EndTime
    """;
        try (Connection cn = new DBContext().connection; PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, appointmentDate);
            ps.setTime(3, newEndTime);
            ps.setTime(4, newStartTime);
            ps.setTime(5, newEndTime);
            ps.setTime(6, newStartTime);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }
     /**
     * Lấy danh sách lịch sử các cuộc hẹn đã hoàn thành của một bệnh nhân.
     * Hàm này truy vấn kèm tên bác sĩ và tên dịch vụ để hiển thị.
     * @param patientId ID của bệnh nhân.
     * @return Một List chứa thông tin cơ bản của các lần khám.
     */
    public List<Appointments> getAppointmentHistoryByPatientId(int patientId) {
        List<Appointments> history = new ArrayList<>();
        // Câu lệnh SQL JOIN các bảng để lấy thông tin cần thiết trong một lần truy vấn
        String sql = """
            SELECT 
                a.AppointmentID, a.AppointmentDate, a.StartTime, a.Status, a.Notes,
                s.ServiceName,
                u.FullName AS DoctorName,
                d.DoctorID,
                s.ServiceID
            FROM dbo.Appointments a
            JOIN dbo.Services s ON a.ServiceID = s.ServiceID
            JOIN dbo.Doctors d ON a.DoctorID = d.DoctorID
            JOIN dbo.Users u ON d.UserID = u.UserID
            WHERE a.PatientID = ? AND a.Status = 'Completed'
            ORDER BY a.AppointmentDate DESC, a.StartTime DESC
        """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Appointments appointment = new Appointments();
                    appointment.setAppointmentId(rs.getInt("AppointmentID"));
                    appointment.setAppointmentDate(rs.getDate("AppointmentDate"));
                    appointment.setStartTime(rs.getTime("StartTime"));
                    appointment.setStatus(rs.getString("Status"));
                    appointment.setNotes(rs.getString("Notes"));

                    // Set thông tin Service (Dịch vụ)
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceID"));
                    service.setServiceName(rs.getString("ServiceName"));
                    appointment.setServiceId(service);

                    // Set thông tin Doctor (Bác sĩ)
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));
                    Users doctorUser = new Users();
                    doctorUser.setFullName(rs.getString("DoctorName"));
                    doctor.setUserId(doctorUser);
                    appointment.setDoctorId(doctor);
                    
                    history.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return history;
    }
    public List<Appointments> getAppointmentsByDoctorAndDate(int doctorId, java.sql.Date date) {
    List<Appointments> list = new ArrayList<>();

    String sql = """
        SELECT 
            a.AppointmentID, a.AppointmentDate, a.StartTime, a.EndTime, a.Status,
            p.PatientID,
            u.UserID, u.FullName,
            s.ServiceID, s.ServiceName
        FROM dbo.Appointments a
        JOIN dbo.Patients p ON a.PatientID = p.PatientID
        JOIN dbo.Users u ON p.UserID = u.UserID
        JOIN dbo.Services s ON a.ServiceID = s.ServiceID
        WHERE a.DoctorID = ? AND a.AppointmentDate = ?
        ORDER BY a.StartTime ASC
    """;

    try (Connection conn = new DBContext().connection;
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setInt(1, doctorId);
        ps.setDate(2, date);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Appointments a = new Appointments();
                a.setAppointmentId(rs.getInt("AppointmentID"));
                a.setAppointmentDate(rs.getDate("AppointmentDate"));
                a.setStartTime(rs.getTime("StartTime"));
                a.setEndTime(rs.getTime("EndTime"));
                a.setStatus(rs.getString("Status"));

                // 🔹 Gắn thông tin bệnh nhân và user (FullName)
                Users user = new Users();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));

                Patients patient = new Patients();
                patient.setPatientID(rs.getInt("PatientID"));
                patient.setUserID(  user); // gán user vào bệnh nhân

                a.setPatientId(patient);

                // 🔹 Gắn thông tin dịch vụ
                Service s = new Service();
                s.setServiceId(rs.getInt("ServiceID"));
                s.setServiceName(rs.getString("ServiceName"));
                a.setServiceId(s);

                list.add(a);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return list;
}
}
