/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

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
 * @author Nguyen Dinh Giap
 */
public class AppointmentsDao extends DBContext {

    public boolean updateAppointment(Appointments a) {
        if (a == null || a.getAppointmentId() <= 0) {
            return false;
        }

        String sql = """
        UPDATE dbo.Appointments
        SET 
            PatientID = ?, 
            DoctorID = ?, 
            ServiceID = ?, 
            AppointmentDate = ?, 
            StartTime = ?, 
            EndTime = ?, 
            Status = ?, 
            Notes = ?, 
            UpdatedDate = GETDATE()
        WHERE AppointmentID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            int i = 1;

            // Gán các giá trị cho cột
            if (a.getPatientId() != null) {
                ps.setInt(i++, a.getPatientId().getPatientID());
            } else {
                ps.setNull(i++, Types.INTEGER);
            }

            if (a.getDoctorId() != null) {
                ps.setInt(i++, a.getDoctorId().getDoctorID());
            } else {
                ps.setNull(i++, Types.INTEGER);
            }

            if (a.getServiceId() != null) {
                ps.setInt(i++, a.getServiceId().getServiceId());
            } else {
                ps.setNull(i++, Types.INTEGER);
            }

            if (a.getAppointmentDate() != null) {
                ps.setDate(i++, a.getAppointmentDate());
            } else {
                ps.setNull(i++, Types.DATE);
            }

            if (a.getStartTime() != null) {
                ps.setTime(i++, a.getStartTime());
            } else {
                ps.setNull(i++, Types.TIME);
            }

            if (a.getEndTime() != null) {
                ps.setTime(i++, a.getEndTime());
            } else {
                ps.setNull(i++, Types.TIME);
            }

            if (a.getStatus() != null && !a.getStatus().isBlank()) {
                ps.setString(i++, a.getStatus().trim());
            } else {
                ps.setNull(i++, Types.NVARCHAR);
            }

            if (a.getNotes() != null && !a.getNotes().isBlank()) {
                ps.setString(i++, a.getNotes());
            } else {
                ps.setNull(i++, Types.NVARCHAR);
            }

            ps.setInt(i++, a.getAppointmentId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //lay lich hen qua id
    public Appointments getAppointmentsById(int appointmentId) {
        String sql = """
        SELECT 
            ap.AppointmentID,
            ap.PatientID,
            ap.DoctorID,
            ap.ServiceID,
            ap.AppointmentDate,
            ap.StartTime,
            ap.EndTime,
            ap.Status,
            ap.Notes,
            ap.CreatedDate,
            ap.UpdatedDate,
            u_patient.FullName AS PatientName,
            u_patient.PhoneNumber AS PatientPhone,
            u_patient.DateOfBirth AS PatientDOB,
            u_patient.Gender AS PatientGender,
            u_patient.Address AS PatientAddress,
            u_doctor.FullName AS DoctorName,
            d.Specialization AS DoctorSpecialization,
            d.ConsultationFee AS DoctorConsultationFee,
            s.ServiceName
        FROM dbo.Appointments ap
        JOIN dbo.Patients p ON ap.PatientID = p.PatientID
        JOIN dbo.Users u_patient ON p.UserID = u_patient.UserID
        JOIN dbo.Doctors d ON ap.DoctorID = d.DoctorID
        JOIN dbo.Users u_doctor ON d.UserID = u_doctor.UserID
        JOIN dbo.Services s ON ap.ServiceID = s.ServiceID
        WHERE ap.AppointmentID = ?
    """;

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, appointmentId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowWithJoin(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Lọc danh sách lịch hẹn
    public List<Appointments> filterAppointment(AppointmentDto a) {
        List<Appointments> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            ap.AppointmentID,
            ap.PatientID,
            ap.DoctorID,
            ap.ServiceID,
            ap.AppointmentDate,
            ap.StartTime,
            ap.EndTime,
            ap.Status,
            ap.Notes,
            ap.CreatedDate,
            ap.UpdatedDate,
            u_patient.FullName AS PatientName,
            u_patient.PhoneNumber AS PatientPhone,
            u_patient.DateOfBirth AS PatientDOB,
            u_patient.Gender AS PatientGender,
            u_patient.Address AS PatientAddress,
            u_doctor.FullName AS DoctorName,
            d.Specialization AS DoctorSpecialization,
            d.ConsultationFee AS DoctorConsultationFee,
            s.ServiceName
        FROM dbo.Appointments ap
        JOIN dbo.Patients p      ON ap.PatientID = p.PatientID
        JOIN dbo.Users u_patient ON p.UserID = u_patient.UserID
        JOIN dbo.Doctors d       ON ap.DoctorID  = d.DoctorID
        JOIN dbo.Users u_doctor  ON d.UserID     = u_doctor.UserID
        JOIN dbo.Services s      ON ap.ServiceID = s.ServiceID
        WHERE 1=1
    """);

        // ===== Lọc theo ID =====
        if (a.getPatientId() != null) {
            sql.append(" AND ap.PatientID = ? ");
        }
        if (a.getDoctorId() != null) {
            sql.append(" AND ap.DoctorID = ? ");
        }
        if (a.getServiceId() != null) {
            sql.append(" AND ap.ServiceID = ? ");
        }

        // ===== Lọc mềm (LIKE) =====
        if (a.getPatientName() != null && !a.getPatientName().isBlank()) {
            sql.append(" AND u_patient.FullName LIKE ? ");
        }
        if (a.getPhoneNumber() != null && !a.getPhoneNumber().isBlank()) {
            sql.append(" AND u_patient.PhoneNumber LIKE ? ");
        }
        if (a.getDoctorName() != null && !a.getDoctorName().isBlank()) {
            sql.append(" AND u_doctor.FullName LIKE ? ");
        }
        if (a.getServiceName() != null && !a.getServiceName().isBlank()) {
            sql.append(" AND s.ServiceName LIKE ? ");
        }

        // ===== Lọc theo ngày =====
        if (a.getAppointmentDate() != null) {
            sql.append(" AND ap.AppointmentDate = ? ");
        } else {
            if (a.getAppointmentDateFrom() != null) {
                sql.append(" AND ap.AppointmentDate >= ? ");
            }
            if (a.getAppointmentDateTo() != null) {
                sql.append(" AND ap.AppointmentDate <= ? ");
            }
        }

        // ===== Trạng thái =====
        if (a.getStatus() != null && !a.getStatus().isBlank()) {
            sql.append(" AND ap.Status = ? ");
        }

        // ===== Sắp xếp =====
        if (a.isSortMode()) {
            sql.append(" ORDER BY ap.AppointmentDate DESC, ap.StartTime ASC ");
        } else {
            sql.append(" ORDER BY ap.AppointmentDate DESC ");
        }

        // ===== Phân trang =====
        if (a.isPaginationMode()) {
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        }

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

            if (a.getPatientName() != null && !a.getPatientName().isBlank()) {
                ps.setString(i++, "%" + a.getPatientName().trim() + "%");
            }
            if (a.getPhoneNumber() != null && !a.getPhoneNumber().isBlank()) {
                ps.setString(i++, "%" + a.getPhoneNumber().trim() + "%");
            }
            if (a.getDoctorName() != null && !a.getDoctorName().isBlank()) {
                ps.setString(i++, "%" + a.getDoctorName().trim() + "%");
            }
            if (a.getServiceName() != null && !a.getServiceName().isBlank()) {
                ps.setString(i++, "%" + a.getServiceName().trim() + "%");
            }

            if (a.getAppointmentDate() != null) {
                ps.setDate(i++, a.getAppointmentDate());
            } else {
                if (a.getAppointmentDateFrom() != null) {
                    ps.setDate(i++, a.getAppointmentDateFrom());
                }
                if (a.getAppointmentDateTo() != null) {
                    ps.setDate(i++, a.getAppointmentDateTo());
                }
            }

            if (a.getStatus() != null && !a.getStatus().isBlank()) {
                ps.setString(i++, a.getStatus().trim());
            }

            if (a.isPaginationMode()) {
                int page = Math.max(a.getPage(), 1);
                int size = Math.max(a.getSize(), 1);
                ps.setInt(i++, (page - 1) * size);
                ps.setInt(i++, size);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRowWithJoin(rs));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countAppointments(AppointmentDto a) {
        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(1)
        FROM dbo.Appointments ap
        JOIN dbo.Patients p      ON ap.PatientID = p.PatientID
        JOIN dbo.Users u_patient ON p.UserID     = u_patient.UserID
        JOIN dbo.Doctors d       ON ap.DoctorID  = d.DoctorID
        JOIN dbo.Users u_doctor  ON d.UserID     = u_doctor.UserID
        JOIN dbo.Services s      ON ap.ServiceID = s.ServiceID
        WHERE 1=1
    """);

        if (a.getPatientId() != null) {
            sql.append(" AND ap.PatientID = ? ");
        }
        if (a.getDoctorId() != null) {
            sql.append(" AND ap.DoctorID = ? ");
        }
        if (a.getServiceId() != null) {
            sql.append(" AND ap.ServiceID = ? ");
        }
        if (a.getPatientName() != null && !a.getPatientName().isBlank()) {
            sql.append(" AND u_patient.FullName LIKE ? ");
        }
        if (a.getPhoneNumber() != null && !a.getPhoneNumber().isBlank()) {
            sql.append(" AND u_patient.PhoneNumber LIKE ? ");
        }
        if (a.getDoctorName() != null && !a.getDoctorName().isBlank()) {
            sql.append(" AND u_doctor.FullName LIKE ? ");
        }
        if (a.getServiceName() != null && !a.getServiceName().isBlank()) {
            sql.append(" AND s.ServiceName LIKE ? ");
        }

        if (a.getAppointmentDate() != null) {
            sql.append(" AND ap.AppointmentDate = ? ");
        } else {
            if (a.getAppointmentDateFrom() != null) {
                sql.append(" AND ap.AppointmentDate >= ? ");
            }
            if (a.getAppointmentDateTo() != null) {
                sql.append(" AND ap.AppointmentDate <= ? ");
            }
        }

        if (a.getStatus() != null && !a.getStatus().isBlank()) {
            sql.append(" AND ap.Status = ? ");
        }

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

            if (a.getPatientName() != null && !a.getPatientName().isBlank()) {
                ps.setString(i++, "%" + a.getPatientName().trim() + "%");
            }
            if (a.getPhoneNumber() != null && !a.getPhoneNumber().isBlank()) {
                ps.setString(i++, "%" + a.getPhoneNumber().trim() + "%");
            }
            if (a.getDoctorName() != null && !a.getDoctorName().isBlank()) {
                ps.setString(i++, "%" + a.getDoctorName().trim() + "%");
            }
            if (a.getServiceName() != null && !a.getServiceName().isBlank()) {
                ps.setString(i++, "%" + a.getServiceName().trim() + "%");
            }

            if (a.getAppointmentDate() != null) {
                ps.setDate(i++, a.getAppointmentDate());
            } else {
                if (a.getAppointmentDateFrom() != null) {
                    ps.setDate(i++, a.getAppointmentDateFrom());
                }
                if (a.getAppointmentDateTo() != null) {
                    ps.setDate(i++, a.getAppointmentDateTo());
                }
            }

            if (a.getStatus() != null && !a.getStatus().isBlank()) {
                ps.setString(i++, a.getStatus().trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private Appointments mapRowWithJoin(ResultSet rs) throws SQLException {
        Appointments appointment = new Appointments();

        appointment.setAppointmentId(rs.getInt("AppointmentID"));
        appointment.setAppointmentDate(rs.getDate("AppointmentDate"));
        appointment.setStartTime(rs.getTime("StartTime"));
        appointment.setEndTime(rs.getTime("EndTime"));
        appointment.setStatus(rs.getString("Status"));
        appointment.setNotes(rs.getString("Notes"));
        appointment.setCreatedDate(rs.getTimestamp("CreatedDate"));
        appointment.setUpdatedDate(rs.getTimestamp("UpdatedDate"));

        // Patient
        Patients patient = new Patients();
        patient.setPatientID(rs.getInt("PatientID"));
        Users uPatient = new Users();
        uPatient.setFullName(rs.getString("PatientName"));
        uPatient.setPhoneNumber(rs.getString("PatientPhone"));
        uPatient.setDateOfBirth(rs.getDate("PatientDOB"));
        uPatient.setGender(rs.getString("PatientGender"));
        uPatient.setAddress(rs.getString("PatientAddress"));
        patient.setUserID(uPatient);
        appointment.setPatientId(patient);

        // Doctor
        Doctor doctor = new Doctor();
        doctor.setDoctorID(rs.getInt("DoctorID"));
        Users uDoctor = new Users();
        uDoctor.setFullName(rs.getString("DoctorName"));
        doctor.setUserId(uDoctor);
        doctor.setSpecialization(rs.getString("DoctorSpecialization"));
        doctor.setConsultationFee(rs.getBigDecimal("DoctorConsultationFee"));
        appointment.setDoctorId(doctor);

        // Service
        Service service = new Service();
        service.setServiceId(rs.getInt("ServiceID"));
        service.setServiceName(rs.getString("ServiceName"));
        appointment.setServiceId(service);

        return appointment;
    }

    public Integer insertAppointment(Appointments a) {
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
        //check trung lap trung dau, trung giua va trung cuoi
        String sql = """
        SELECT 1
        FROM dbo.Appointments
        WHERE DoctorID = ? AND AppointmentDate = ? 
        AND Status != 'Cancelled'
        AND (
            (StartTime < ? AND EndTime > ?) OR  
            (StartTime < ? AND EndTime > ?) OR   
            (StartTime >= ? AND EndTime <= ?)  
        )
    """;
        try (Connection cn = new DBContext().connection; PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            ps.setDate(2, appointmentDate);
            ps.setTime(3, newEndTime);
            ps.setTime(4, newStartTime);
            ps.setTime(5, newEndTime);
            ps.setTime(6, newStartTime);
            ps.setTime(7, newStartTime);
            ps.setTime(8, newEndTime);

            try (ResultSet rs = ps.executeQuery()) {
                //co trung lap
                return rs.next();
            }
        } catch (SQLException e) {
            return true;
        }
    }

    /**
     * Lấy danh sách lịch sử các cuộc hẹn đã hoàn thành của một bệnh nhân. Hàm
     * này truy vấn kèm tên bác sĩ và tên dịch vụ để hiển thị.
     *
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

}
