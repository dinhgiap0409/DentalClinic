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
        String sql = "SELECT DoctorID, UserID, Specialization, LicenseNumber, YearsOfExperience, Education, Biography, ConsultationFee "
                + "FROM dbo.Doctors WHERE DoctorID = ?";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setInt(1, doctorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Doctor doctor = new Doctor();
                    doctor.setDoctorID(rs.getInt("DoctorID"));
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserID"));
                    doctor.setUserId(user);
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
