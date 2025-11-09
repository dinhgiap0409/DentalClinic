/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Doctor;
import java.sql.*;
import model.Users;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class DoctorDao extends DBContext {

    public DoctorDao() {
    }

    public Doctor getDoctorByUserId(int userId) {
        String sql = """
            SELECT 
                d.[DoctorID],
                d.[UserID],
                d.[Specialization],
                d.[LicenseNumber],
                d.[YearsOfExperience],
                d.[Education],
                d.[Biography],
                d.[ConsultationFee],
                u.[Username],
                u.[PasswordHash],
                u.[Email],
                u.[FullName],
                u.[PhoneNumber],
                u.[DateOfBirth],
                u.[Gender],
                u.[Address],
                u.[Role],
                u.[IsActive],
                u.[CreatedDate],
                u.[Image]
            FROM [dbo].[Doctors] d
            JOIN [dbo].[Users] u ON d.[UserID] = u.[UserID]
            WHERE d.[UserID] = ?
        """;

        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));
                    doctor.setSpecialization(rs.getString("Specialization"));
                    doctor.setLicenseNumber(rs.getString("LicenseNumber"));
                    doctor.setYearsOfExperience(rs.getInt("YearsOfExperience"));
                    doctor.setEducation(rs.getString("Education"));
                    doctor.setBiography(rs.getString("Biography"));
                    doctor.setConsultationFee(rs.getBigDecimal("ConsultationFee"));

                    // Khởi tạo Users object
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserID"));
                    user.setUserName(rs.getString("Username"));
                    user.setPassWord(rs.getString("PasswordHash"));
                    user.setEmail(rs.getString("Email"));
                    user.setFullName(rs.getString("FullName"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    user.setDateOfBirth(rs.getDate("DateOfBirth"));
                    user.setGender(rs.getString("Gender"));
                    user.setAddress(rs.getString("Address"));
                    user.setRole(rs.getString("Role"));
                    user.setIsActive(rs.getBoolean("IsActive"));
                    user.setImage(rs.getString("Image"));

                    Timestamp ts = rs.getTimestamp("CreatedDate");
                    user.setCreateDate(ts == null ? null : new java.sql.Date(ts.getTime()));

                    // Gắn user vào doctor
                    doctor.setUserId(user);

                    return doctor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int insertDoctor(Doctor doctor) {
        String sql = "INSERT INTO [dbo].[Doctors]\n"
                + "           ([UserID]\n"
                + "           ,[Specialization]\n"
                + "           ,[LicenseNumber]\n"
                + "           ,[YearsOfExperience]\n"
                + "           ,[Education]\n"
                + "           ,[Biography]\n"
                + "           ,[ConsultationFee])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?)";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, doctor.getUserId().getUserId());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getLicenseNumber());
            ps.setInt(4, doctor.getYearsOfExperience());
            ps.setString(5, doctor.getEducation());
            ps.setString(6, doctor.getBiography());
            ps.setBigDecimal(7, doctor.getConsultationFee());
            int row = ps.executeUpdate();
            if (row == 0) {
                return -1;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.err.println("ERROR(FK/UNIQUE/CHECK): " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    public boolean updateDoctor(Doctor doctor) {
        String sql = "UPDATE [dbo].[Doctors]\n"
                + "   SET [UserID] = ?,\n"
                + "       [Specialization] = ?,\n"
                + "       [LicenseNumber] = ?,\n"
                + "       [YearsOfExperience] = ?,\n"
                + "       [Education] = ?,\n"
                + "       [Biography] = ?,\n"
                + "       [ConsultationFee] = ?\n"
                + " WHERE DoctorID = ?";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, doctor.getUserId().getUserId());
            ps.setString(2, doctor.getSpecialization());
            ps.setString(3, doctor.getLicenseNumber());
            ps.setInt(4, doctor.getYearsOfExperience());
            ps.setString(5, doctor.getEducation());
            ps.setString(6, doctor.getBiography());
            ps.setBigDecimal(7, doctor.getConsultationFee());
            ps.setInt(8, doctor.getDoctorID());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Doctor getDoctorByID(int doctorId) {
        final String sql = """
        SELECT 
            d.DoctorID,
            d.UserID,
            d.Specialization,
            d.LicenseNumber,
            d.YearsOfExperience,
            d.Education,
            d.Biography,
            d.ConsultationFee,

            u.UserID         AS u_UserID,
            u.Username       AS u_Username,
            u.PasswordHash   AS u_PasswordHash,
            u.Email          AS u_Email,
            u.FullName       AS u_FullName,
            u.PhoneNumber    AS u_PhoneNumber,
            u.DateOfBirth    AS u_DateOfBirth,
            u.Gender         AS u_Gender,
            u.Address        AS u_Address,
            u.Role           AS u_Role,
            u.IsActive       AS u_IsActive,
            u.CreatedDate    AS u_CreatedDate,
            u.image
                           
        FROM dbo.Doctors d
        JOIN dbo.Users   u ON u.UserID = d.UserID
        WHERE d.DoctorID = ?
        """;

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, doctorId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));

                    // Map Users
                    Users user = new Users();
                    user.setUserId(rs.getInt("u_UserID"));
                    user.setUserName(rs.getString("u_Username"));
                    user.setPassWord(rs.getString("u_PasswordHash")); // nếu field trong model là passWord
                    user.setEmail(rs.getString("u_Email"));
                    user.setFullName(rs.getString("u_FullName"));
                    user.setPhoneNumber(rs.getString("u_PhoneNumber"));
                    user.setDateOfBirth(rs.getDate("u_DateOfBirth")); // java.sql.Date
                    user.setGender(rs.getString("u_Gender"));
                    user.setAddress(rs.getString("u_Address"));
                    user.setRole(rs.getString("u_Role"));
                    // chú ý: getBoolean trả false khi NULL; nếu cần phân biệt null -> dùng getObject
                    user.setIsActive(rs.getObject("u_IsActive") == null ? null : rs.getBoolean("u_IsActive"));
                    user.setCreateDate(rs.getDate("u_CreatedDate"));
                    user.setImage(rs.getString("image"));

                    doctor.setUserId(user);

                    // Map Doctor
                    doctor.setSpecialization(rs.getString("Specialization"));
                    doctor.setLicenseNumber(rs.getString("LicenseNumber"));
                    doctor.setYearsOfExperience(rs.getInt("YearsOfExperience"));
                    doctor.setEducation(rs.getString("Education"));
                    doctor.setBiography(rs.getString("Biography"));
                    doctor.setConsultationFee(rs.getBigDecimal("ConsultationFee"));

                    return doctor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Doctor> getAllDoctors() {
        String sql = "SELECT d.DoctorID, d.UserID, d.Specialization, d.LicenseNumber, "
                + "d.YearsOfExperience, d.Education, d.Biography, d.ConsultationFee, "
                + "u.FullName, u.Email, u.PhoneNumber "
                + "FROM dbo.Doctors d "
                + "INNER JOIN dbo.Users u ON d.UserID = u.UserID "
                + "ORDER BY d.DoctorID";
        List<Doctor> doctors = new ArrayList<>();

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));
                    doctor.setSpecialization(rs.getString("Specialization"));
                    doctor.setLicenseNumber(rs.getString("LicenseNumber"));
                    doctor.setYearsOfExperience(rs.getInt("YearsOfExperience"));
                    doctor.setEducation(rs.getString("Education"));
                    doctor.setBiography(rs.getString("Biography"));
                    doctor.setConsultationFee(rs.getBigDecimal("ConsultationFee"));

                    Users user = new Users();
                    user.setUserId(rs.getInt("UserID"));
                    user.setFullName(rs.getString("FullName"));
                    user.setEmail(rs.getString("Email"));
                    user.setPhoneNumber(rs.getString("PhoneNumber"));
                    doctor.setUserId(user);

                    doctors.add(doctor);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctors;
    }

    public Integer getUserIdByDoctorId(int doctorId) {
        String sql = "SELECT UserID FROM Doctors WHERE DoctorID=?";
        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Integer> getDoctorIdsByServiceId(int serviceId) {
        List<Integer> doctorIds = new ArrayList<>();
        String sql = "SELECT DoctorID FROM Doctor_Service WHERE ServiceID = ?";

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setInt(1, serviceId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    doctorIds.add(rs.getInt("DoctorID"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return doctorIds;
    }

}
