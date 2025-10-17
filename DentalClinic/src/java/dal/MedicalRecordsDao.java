/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.*;
import model.Appointments;
import model.MedicalRecords;
/**
 *
 * @author Nguyen Dinh Giap
 */
public class MedicalRecordsDao extends DBContext{
    /**
     * Lấy thông tin chi tiết của một lần khám (bệnh án) dựa vào ID của cuộc hẹn.
     * param appointmentId ID của cuộc hẹn (từ cuộc hẹn mà người dùng chọn).
     * return Một đối tượng MedicalRecords chứa thông tin chi tiết, hoặc null nếu không tìm thấy.
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
}
