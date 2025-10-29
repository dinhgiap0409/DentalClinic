/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.PrescriptionDto;
import java.util.ArrayList;
import java.util.List;
import model.Prescriptions;
import java.sql.*;
import model.Doctor;
import model.MedicalRecords;
/**
 *
 * @author Nguyen Dang Khang
 */
public class PrescriptionDao {

    public List<Prescriptions> filterPrescription(PrescriptionDto dto) {
        List<Prescriptions> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT [PrescriptionID], [RecordID], [DoctorID],
                   [IssueDate], [Instructions]
            FROM [dbo].[Prescriptions]
            WHERE 1 = 1
        """);

        // Điều kiện lọc động
        if (dto.getRecordId() != null) sql.append(" AND RecordID = ?\n");
        if (dto.getDoctorId() != null) sql.append(" AND DoctorID = ?\n");
        if (dto.getIssueDateFrom() != null) sql.append(" AND IssueDate >= ?\n");
        if (dto.getIssueDateTo() != null) sql.append(" AND IssueDate <= ?\n");
        if (dto.getInstructions() != null && !dto.getInstructions().isBlank()) sql.append(" AND Instructions LIKE ?\n");

        // Sắp xếp & phân trang
        if (dto.isSortMode()) {
            sql.append(" ORDER BY PrescriptionID ASC\n");
        }
        if (dto.isPaginationMode()) {
            if (!dto.isSortMode()) sql.append(" ORDER BY PrescriptionID ASC\n");
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }

        try (Connection conn = new DBContext().connection;
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (dto.getRecordId() != null) ps.setInt(i++, dto.getRecordId());
            if (dto.getDoctorId() != null) ps.setInt(i++, dto.getDoctorId());
            if (dto.getIssueDateFrom() != null) ps.setTimestamp(i++, dto.getIssueDateFrom());
            if (dto.getIssueDateTo() != null) ps.setTimestamp(i++, dto.getIssueDateTo());
            if (dto.getInstructions() != null && !dto.getInstructions().isBlank())
                ps.setString(i++, "%" + dto.getInstructions().trim() + "%");

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

    // Hàm ánh xạ kết quả ResultSet sang model Prescriptions
    private Prescriptions mapRow(ResultSet rs) throws SQLException {
        Prescriptions p = new Prescriptions();
        p.setPrescriptionId(rs.getInt("PrescriptionID"));

        // Gắn MedicalRecord (chỉ gán ID)
        MedicalRecords record = new MedicalRecords();
        record.setRecordId(rs.getInt("RecordID"));
        p.setRecordId(record);

        // Gắn Doctor (chỉ gán ID)
        Doctor doctor = new Doctor();
        doctor.setDoctorID(rs.getInt("DoctorID"));
        p.setDoctorId(doctor);

        p.setIssueDate(rs.getTimestamp("IssueDate"));
        p.setInstructions(rs.getString("Instructions"));

        return p;
    }
}