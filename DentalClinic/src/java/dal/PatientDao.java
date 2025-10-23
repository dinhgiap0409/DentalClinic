/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.Patients;
import model.Users;

/**
 *
 * @author Nguyen Dang Khang
 */
public class PatientDao extends DBContext {

    /**
     * Lấy thông tin bệnh nhân dựa trên UserID. Hàm này JOIN với bảng Users để
     * lấy thông tin đầy đủ.
     *
     * @param userId ID của người dùng (lấy từ session).
     * @return Một đối tượng Patients nếu tìm thấy, ngược lại trả về null.
     */
    public Patients getPatientByUserId(int userId) {
        String sql = """
            SELECT 
                p.PatientID, p.UserID, p.BloodType, p.Allergies, p.MedicalHistory, 
                p.InsuranceInfo, p.EmergencyContactName, p.EmergencyContactPhone,
                u.Username, u.Email, u.FullName, u.PhoneNumber, u.DateOfBirth, 
                u.Gender, u.Address, u.Role, u.IsActive, u.CreatedDate
            FROM dbo.Patients p
            JOIN dbo.Users u ON p.UserID = u.UserID
            WHERE p.UserID = ?
        """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //Tao doi tuong user tu ket qua truy van
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserID"));
                    user.setUserName(rs.getString("Username"));
                    user.setEmail(rs.getString("Email"));
                    user.setFullName(rs.getString("FullName"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    user.setDateOfBirth(rs.getDate("DateOfBirth"));
                    user.setGender(rs.getString("Gender"));
                    user.setAddress(rs.getString("Address"));
                    user.setRole(rs.getString("Role"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    Timestamp ts = rs.getTimestamp("CreatedDate");
                    user.setCreateDate(ts == null ? null : new java.sql.Date(ts.getTime()));
                    //Tao doi tuong patients va gan doi tuong user vao
                    Patients patient = new Patients();
                    patient.setPatientID(rs.getInt("PatientID"));
                    patient.setBloodType(rs.getString("BloodType"));
                    patient.setAllergies(rs.getString("Allergies"));
                    patient.setMedicalHistory(rs.getString("MedicalHistory"));
                    patient.setInsuranceInfo(rs.getString("InsuranceInfo"));
                    patient.setEmergencyContactName(rs.getString("EmergencyContactName"));
                    patient.setEmergencyContactPhone(rs.getString("EmergencyContactPhone"));
                    patient.setUserID(user);

                    return patient;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Integer getUserIdByPatientId(int patientId) {
        String sql = "SELECT UserID FROM Patients WHERE PatientID=?";
        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Thêm một bệnh nhân mới vào cơ sở dữ liệu, chỉ dựa trên thông tin Users.
     * Các trường thông tin y tế khác sẽ để trống (NULL).
     *
     * @param patient Đối tượng Patients chỉ cần chứa thông tin UserID.
     * @return true nếu thêm thành công, false nếu thất bại.
     */
    public boolean insertPatient(Patients patient) {
        // Đảm bảo có thông tin user để liên kết
        if (patient == null || patient.getUserID() == null) {
            return false;
        }
        String sql = "INSERT INTO dbo.Patients (UserID) VALUES (?)";
        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, patient.getUserID().getUserId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // In lỗi ra để debug
            return false;
        }
    }

    /**
     * Cập nhật thông tin hồ sơ bệnh nhân.
     *
     * @param patient Đối tượng Patients chứa thông tin cần cập nhật.
     * @return true nếu cập nhật thành công, false nếu thất bại.
     */
    public boolean updatePatientProfile(Patients patient) {
        // SQL theo đúng yêu cầu của bạn
        String sql = "UPDATE dbo.Patients SET BloodType=?, Allergies=?, MedicalHistory=?, "
                + "InsuranceInfo=?, EmergencyContactName=?, EmergencyContactPhone=? WHERE PatientID=?";

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patient.getBloodType());
            ps.setString(2, patient.getAllergies());
            ps.setString(3, patient.getMedicalHistory());
            ps.setString(4, patient.getInsuranceInfo());
            ps.setString(5, patient.getEmergencyContactName());
            ps.setString(6, patient.getEmergencyContactPhone());
            ps.setInt(7, patient.getPatientID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
