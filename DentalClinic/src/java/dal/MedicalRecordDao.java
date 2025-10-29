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
 * @author Nguyen Dang Khang
 */
public class MedicalRecordDao {

    public List<MedicalRecords> filterMedicalRecord(MedicalRecordDto dto) {
        List<MedicalRecords> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT [RecordID], [AppointmentID], [Diagnosis],
                   [Symptoms], [TreatmentPlan], [FollowUpDate], [CreatedDate]
            FROM [dbo].[MedicalRecords]
            WHERE 1 = 1
        """);

        // Thêm các điều kiện lọc động
        if (dto.getAppointmentId() != null) sql.append(" AND AppointmentID = ?\n");
        if (dto.getDiagnosis() != null && !dto.getDiagnosis().isBlank()) sql.append(" AND Diagnosis LIKE ?\n");
        if (dto.getSymptoms() != null && !dto.getSymptoms().isBlank()) sql.append(" AND Symptoms LIKE ?\n");
        if (dto.getTreatmentPlan() != null && !dto.getTreatmentPlan().isBlank()) sql.append(" AND TreatmentPlan LIKE ?\n");
        if (dto.getFollowUpDate() != null) sql.append(" AND FollowUpDate = ?\n");
        if (dto.getCreatedDateFrom() != null) sql.append(" AND CreatedDate >= ?\n");
        if (dto.getCreatedDateTo() != null) sql.append(" AND CreatedDate <= ?\n");

        // Sắp xếp & phân trang
        if (dto.isSortMode()) {
            sql.append(" ORDER BY RecordID ASC\n");
        }
        if (dto.isPaginationMode()) {
            if (!dto.isSortMode()) sql.append(" ORDER BY RecordID ASC\n");
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }

        try (Connection conn = new DBContext().connection;
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (dto.getAppointmentId() != null) ps.setInt(i++, dto.getAppointmentId());
            if (dto.getDiagnosis() != null && !dto.getDiagnosis().isBlank()) ps.setString(i++, "%" + dto.getDiagnosis().trim() + "%");
            if (dto.getSymptoms() != null && !dto.getSymptoms().isBlank()) ps.setString(i++, "%" + dto.getSymptoms().trim() + "%");
            if (dto.getTreatmentPlan() != null && !dto.getTreatmentPlan().isBlank()) ps.setString(i++, "%" + dto.getTreatmentPlan().trim() + "%");
            if (dto.getFollowUpDate() != null) ps.setDate(i++, dto.getFollowUpDate());
            if (dto.getCreatedDateFrom() != null) ps.setDate(i++, dto.getCreatedDateFrom());
            if (dto.getCreatedDateTo() != null) ps.setDate(i++, dto.getCreatedDateTo());

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

    // Map dữ liệu từ ResultSet sang đối tượng MedicalRecords
    private MedicalRecords mapRow(ResultSet rs) throws SQLException {
        MedicalRecords record = new MedicalRecords();
        record.setRecordId(rs.getInt("RecordID"));

        // Gán Appointment object (chỉ gán ID, có thể join thêm nếu cần)
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
}