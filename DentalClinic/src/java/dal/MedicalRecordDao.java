/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.MedicalRecordDto;
import java.util.ArrayList;
import java.util.List;
import model.MedicalRecords;
import java.sql.*;
import model.Appointments;

/**
 *
 * @author TNO
 */
public class MedicalRecordDao {

    public List<MedicalRecords> filterMedicalRecord(MedicalRecordDto dto) {
        List<MedicalRecords> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT mr.[RecordID],
               mr.[AppointmentID],
               mr.[Diagnosis],
               mr.[Symptoms],
               mr.[TreatmentPlan],
               mr.[FollowUpDate],
               mr.[CreatedDate]
        FROM [dbo].[MedicalRecords] mr
        JOIN [dbo].[Appointments] ap ON mr.AppointmentID = ap.AppointmentID
        WHERE 1 = 1
    """);

        // ----- Điều kiện lọc -----
        if (dto.getRecordId() != null) {
            sql.append(" AND mr.RecordID = ? \n");
        }
        if (dto.getAppointmentId() != null) {
            sql.append(" AND mr.AppointmentID = ? \n");
        }

        // THÊM MỚI: lọc theo bệnh nhân/bác sĩ từ bảng Appointments
        if (dto.getPatientId() != null) {
            sql.append(" AND ap.PatientID = ? \n");
        }
        if (dto.getDoctorId() != null) {
            sql.append(" AND ap.DoctorID = ? \n");
        }

        if (dto.getDiagnosis() != null && !dto.getDiagnosis().isBlank()) {
            sql.append(" AND mr.Diagnosis LIKE ? \n");
        }
        if (dto.getSymptoms() != null && !dto.getSymptoms().isBlank()) {
            sql.append(" AND mr.Symptoms LIKE ? \n");
        }
        if (dto.getTreatmentPlan() != null && !dto.getTreatmentPlan().isBlank()) {
            sql.append(" AND mr.TreatmentPlan LIKE ? \n");
        }
        if (dto.getFollowUpDate() != null) {
            sql.append(" AND mr.FollowUpDate = ? \n");
        }
        if (dto.getCreatedDateFrom() != null) {
            sql.append(" AND mr.CreatedDate >= ? \n");
        }
        if (dto.getCreatedDateTo() != null) {
            sql.append(" AND mr.CreatedDate <= ? \n");
        }

        // ----- Sort & Pagination -----
        if (dto.isSortMode()) {
            sql.append(" ORDER BY mr.RecordID ASC \n");
        }
        if (dto.isPaginationMode()) {
            if (!dto.isSortMode()) {
                sql.append(" ORDER BY mr.RecordID ASC \n");
            }
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        }

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (dto.getRecordId() != null) {
                ps.setInt(i++, dto.getRecordId());
            }
            if (dto.getAppointmentId() != null) {
                ps.setInt(i++, dto.getAppointmentId());
            }

            if (dto.getPatientId() != null) {
                ps.setInt(i++, dto.getPatientId());  // THÊM
            }
            if (dto.getDoctorId() != null) {
                ps.setInt(i++, dto.getDoctorId());   // THÊM
            }
            if (dto.getDiagnosis() != null && !dto.getDiagnosis().isBlank()) {
                ps.setString(i++, "%" + dto.getDiagnosis().trim() + "%");
            }
            if (dto.getSymptoms() != null && !dto.getSymptoms().isBlank()) {
                ps.setString(i++, "%" + dto.getSymptoms().trim() + "%");
            }
            if (dto.getTreatmentPlan() != null && !dto.getTreatmentPlan().isBlank()) {
                ps.setString(i++, "%" + dto.getTreatmentPlan().trim() + "%");
            }
            if (dto.getFollowUpDate() != null) {
                ps.setDate(i++, dto.getFollowUpDate());
            }
            if (dto.getCreatedDateFrom() != null) {
                ps.setDate(i++, dto.getCreatedDateFrom());
            }
            if (dto.getCreatedDateTo() != null) {
                ps.setDate(i++, dto.getCreatedDateTo());
            }

            if (dto.isPaginationMode()) {
                int page = Math.max(1, dto.getPage());
                int size = Math.max(1, dto.getSize());
                ps.setInt(i++, (page - 1) * size);
                ps.setInt(i++, size);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private MedicalRecords mapRow(ResultSet rs) throws SQLException {
        MedicalRecords record = new MedicalRecords();
        record.setRecordId(rs.getInt("RecordID"));

        Appointments appointment = new Appointments();
        appointment.setAppointmentId(rs.getInt("AppointmentID"));
        record.setAppointmentId(appointment);

        record.setDiagnosis(rs.getString("Diagnosis"));
        record.setSymptoms(rs.getString("Symptoms"));
        record.setTreatmentPlan(rs.getString("TreatmentPlan"));
        record.setFollowUpDate(rs.getDate("FollowUpDate"));
        record.setCreatedDate(rs.getTimestamp("CreatedDate"));
        return record;
    }

    public int insertMedicalRecord(MedicalRecords record) {
        String sql = """
        INSERT INTO [dbo].[MedicalRecords]
            ([AppointmentID],
             [Diagnosis],
             [Symptoms],
             [TreatmentPlan],
             [FollowUpDate],
             [CreatedDate])
        VALUES (?, ?, ?, ?, ?, GETDATE());
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, record.getAppointmentId().getAppointmentId()); // lấy ID trong object Appointments
            ps.setString(2, record.getDiagnosis());
            ps.setString(3, record.getSymptoms());
            ps.setString(4, record.getTreatmentPlan());
            if (record.getFollowUpDate() != null) {
                ps.setDate(5, record.getFollowUpDate());
            } else {
                ps.setNull(5, Types.DATE);
            }

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // trả về RecordID vừa insert
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1; // trả về -1 nếu lỗi hoặc không insert được
    }

}
