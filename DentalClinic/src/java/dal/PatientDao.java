/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Patients;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class PatientDao extends DBContext {

    public Integer insertPatient(Patients patient) {
        String sql = """
        INSERT INTO [dbo].[Patients]
        ([UserID], [BloodType], [Allergies], [MedicalHistory],
         [InsuranceInfo], [EmergencyContactName], [EmergencyContactPhone])
        VALUES (?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // --- Gán giá trị ---
            if (patient.getUserID() != null) {
                ps.setInt(1, patient.getUserID().getUserId());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setString(2, patient.getBloodType());
            ps.setString(3, patient.getAllergies());
            ps.setString(4, patient.getMedicalHistory());
            ps.setString(5, patient.getInsuranceInfo());
            ps.setString(6, patient.getEmergencyContactName());
            ps.setString(7, patient.getEmergencyContactPhone());

            // --- Thực thi và lấy ID vừa insert ---
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Trả về PatientID vừa thêm
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Thất bại
    }

    public List<Patients> getAllPatients() {
        List<Patients> patients = new ArrayList<>();
        String sql = """
        SELECT 
            p.PatientID, p.UserID, p.BloodType, p.Allergies, p.MedicalHistory,
            p.InsuranceInfo, p.EmergencyContactName, p.EmergencyContactPhone,
            u.FullName, u.Email, u.PhoneNumber
        FROM dbo.Patients p
        JOIN dbo.Users u ON p.UserID = u.UserID
        ORDER BY p.PatientID
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Users user = new Users();
                user.setUserId(rs.getInt("UserID"));
                user.setFullName(rs.getString("FullName"));
                user.setEmail(rs.getString("Email"));
                user.setPhoneNumber(rs.getString("PhoneNumber"));

                Patients patient = new Patients();
                patient.setPatientID(rs.getInt("PatientID"));
                patient.setBloodType(rs.getString("BloodType"));
                patient.setAllergies(rs.getString("Allergies"));
                patient.setMedicalHistory(rs.getString("MedicalHistory"));
                patient.setInsuranceInfo(rs.getString("InsuranceInfo"));
                patient.setEmergencyContactName(rs.getString("EmergencyContactName"));
                patient.setEmergencyContactPhone(rs.getString("EmergencyContactPhone"));
                patient.setUserID(user);

                patients.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

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
                    user.setCreateDate(rs.getDate("CreatedDate"));
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

    public boolean updatePatient(Patients patient) {
        String sql = """
            UPDATE [dbo].[Patients]
            SET [BloodType] = ?,
                [Allergies] = ?,
                [MedicalHistory] = ?,
                [InsuranceInfo] = ?,
                [EmergencyContactName] = ?,
                [EmergencyContactPhone] = ?
            WHERE [PatientID] = ?
        """;

        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {

            // ===== Gán giá trị =====
            if (patient.getBloodType() != null && !patient.getBloodType().isBlank()) {
                ps.setString(1, patient.getBloodType());
            } else {
                ps.setNull(1, Types.NVARCHAR);
            }

            if (patient.getAllergies() != null && !patient.getAllergies().isBlank()) {
                ps.setString(2, patient.getAllergies());
            } else {
                ps.setNull(2, Types.NVARCHAR);
            }

            if (patient.getMedicalHistory() != null && !patient.getMedicalHistory().isBlank()) {
                ps.setString(3, patient.getMedicalHistory());
            } else {
                ps.setNull(3, Types.NVARCHAR);
            }

            if (patient.getInsuranceInfo() != null && !patient.getInsuranceInfo().isBlank()) {
                ps.setString(4, patient.getInsuranceInfo());
            } else {
                ps.setNull(4, Types.NVARCHAR);
            }

            if (patient.getEmergencyContactName() != null && !patient.getEmergencyContactName().isBlank()) {
                ps.setString(5, patient.getEmergencyContactName());
            } else {
                ps.setNull(5, Types.NVARCHAR);
            }

            if (patient.getEmergencyContactPhone() != null && !patient.getEmergencyContactPhone().isBlank()) {
                ps.setString(6, patient.getEmergencyContactPhone());
            } else {
                ps.setNull(6, Types.NVARCHAR);
            }

            ps.setInt(7, patient.getPatientID());

            int row = ps.executeUpdate();
            return row > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Patients getPatientById(int patientId) {
        String sql = """
        SELECT 
            p.PatientID, p.UserID, p.BloodType, p.Allergies, p.MedicalHistory, 
            p.InsuranceInfo, p.EmergencyContactName, p.EmergencyContactPhone,
            u.Username, u.Email, u.FullName, u.PhoneNumber, u.DateOfBirth, 
            u.Gender, u.Address, u.Role, u.IsActive, u.CreatedDate
        FROM dbo.Patients p
        JOIN dbo.Users u ON p.UserID = u.UserID
        WHERE p.PatientID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Tạo đối tượng Users
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
                    user.setCreateDate(rs.getDate("CreatedDate"));

                    // Tạo đối tượng Patients
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

}
