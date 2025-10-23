/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.Appointments;
import model.Doctor;
import model.MedicalRecords;
import model.Service;
import model.Users;

/**
 *
 * @author Nguyen Dang Khang
 */
public class MedicalRecordsDao extends DBContext {

    /**
     * Lấy thông tin chi tiết của một lần khám (bệnh án) dựa vào ID của cuộc
     * hẹn. param appointmentId ID của cuộc hẹn (từ cuộc hẹn mà người dùng
     * chọn). return Một đối tượng MedicalRecords chứa thông tin chi tiết, hoặc
     * null nếu không tìm thấy.
     */
    public MedicalRecords getMedicalRecordByAppointmentId(int appointmentId) {
        String sql = """
            SELECT RecordID, AppointmentID, Diagnosis, Symptoms, TreatmentPlan, FollowUpDate, CreatedDate
            FROM dbo.MedicalRecords
            WHERE AppointmentID = ?
        """;
        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, appointmentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MedicalRecords record = new MedicalRecords();
                    record.setRecordId(rs.getInt("RecordID"));

                    Appointments appt = new Appointments();
                    appt.setAppointmentId(rs.getInt("AppointmentID"));
                    record.setAppointmentId(appt);

                    record.setDiagnosis(rs.getString("Diagnosis"));
                    record.setSymptoms(rs.getString("Symptoms"));
                    record.setTreatmentPlan(rs.getString("TreatmentPlan"));
                    record.setFollowUpDate(rs.getDate("FollowUpDate"));
                    record.setCreatedDate(rs.getTimestamp("CreatedDate"));

                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public MedicalRecords getMedicalRecordByPatientId(int patientId) {

        String sql = """
        SELECT TOP 1
            mr.RecordID,
            mr.Diagnosis,
            mr.Symptoms,
            mr.TreatmentPlan,
            mr.FollowUpDate,
            mr.CreatedDate,
            a.AppointmentID,
            a.AppointmentDate,
            a.StartTime,
            a.EndTime,
            a.Status,
            s.ServiceID,
            s.ServiceName,
            d.DoctorID,
            u.FullName AS DoctorName
        FROM dbo.MedicalRecords mr
        JOIN dbo.Appointments a ON mr.AppointmentID = a.AppointmentID
        JOIN dbo.Services s ON a.ServiceID = s.ServiceID
        JOIN dbo.Doctors d ON a.DoctorID = d.DoctorID
        JOIN dbo.Users u ON d.UserID = u.UserID
        WHERE a.PatientID = ?
        ORDER BY mr.CreatedDate DESC
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    MedicalRecords record = new MedicalRecords();

                    record.setRecordId(rs.getInt("RecordID"));
                    record.setDiagnosis(rs.getString("Diagnosis"));
                    record.setSymptoms(rs.getString("Symptoms"));
                    record.setTreatmentPlan(rs.getString("TreatmentPlan"));
                    record.setFollowUpDate(rs.getDate("FollowUpDate"));
                    record.setCreatedDate(rs.getTimestamp("CreatedDate"));

                    // 🔹 Map thông tin Appointments
                    Appointments ap = new Appointments();
                    ap.setAppointmentId(rs.getInt("AppointmentID"));
                    ap.setAppointmentDate(rs.getDate("AppointmentDate"));
                    ap.setStartTime(rs.getTime("StartTime"));
                    ap.setEndTime(rs.getTime("EndTime"));
                    ap.setStatus(rs.getString("Status"));

                    // 🔹 Dịch vụ
                    Service service = new Service();
                    service.setServiceId(rs.getInt("ServiceID"));
                    service.setServiceName(rs.getString("ServiceName"));
                    ap.setServiceId(service);

                    // 🔹 Bác sĩ
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));
                    Users doctorUser = new Users();
                    doctorUser.setFullName(rs.getString("DoctorName"));
                    doctor.setUserId(doctorUser);
                    ap.setDoctorId(doctor);

                    // Gán Appointment vào MedicalRecord
                    record.setAppointmentId(ap);

                    return record;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
