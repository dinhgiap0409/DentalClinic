/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.ScheduleExceptionsDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;
import model.ScheduleExceptions;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ScheduleExceptionsDao extends DBContext {

    public ScheduleExceptions getScheduleExceptionById(int exceptionId) {
        String sql = """
        SELECT ExceptionID, DoctorID, ExceptionDate, IsWorkingDay,
               StartTime, EndTime, MaxAppointments, CreatedDate
        FROM dbo.ScheduleExceptions
        WHERE ExceptionID = ?
    """;

        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, exceptionId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ScheduleExceptions> filterScheduleExceptions(ScheduleExceptionsDto f) {
        List<ScheduleExceptions> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT ExceptionID, DoctorID, ExceptionDate, IsWorkingDay,
               StartTime, EndTime, MaxAppointments
        FROM dbo.ScheduleExceptions
        WHERE 1=1
    """);

        if (f.getDoctorId()!= null) {
            sql.append(" AND DoctorID = ?\n");
        }
        if (f.getExceptionDate() != null) {
            sql.append(" AND ExceptionDate = ?\n");
        }
        if (f.getIsWorkingDay() != null) {
            sql.append(" AND IsWorkingDay = ?\n");
        }
        if (f.getStartTime() != null) {
            sql.append(" AND StartTime >= ?\n");
        }
        if (f.getEndTime() != null) {
            sql.append(" AND EndTime <= ?\n");
        }
        if (f.getMaxAppointment()!= null) {
            sql.append(" AND MaxAppointments >= ?\n");
        }
        if (f.isSortMode()) {
            sql.append(" ORDER BY ExceptionID ASC\n");
        }
        if (f.isPaginationMode()) {
            if (!f.isSortMode()) {
                sql.append(" ORDER BY ExceptionID ASC\n");
            }
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }

        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int i = 1;
            if (f.getDoctorId() != null) {
                ps.setInt(i++, f.getDoctorId());
            }
            if (f.getExceptionDate() != null) {
                ps.setDate(i++, f.getExceptionDate());
            }
            if (f.getIsWorkingDay() != null) {
                ps.setBoolean(i++, f.getIsWorkingDay());
            }
            if (f.getStartTime() != null) {
                ps.setTime(i++, f.getStartTime());
            }
            if (f.getEndTime() != null) {
                ps.setTime(i++, f.getEndTime());
            }
            if (f.getMaxAppointment()!= null) {
                ps.setInt(i++, f.getMaxAppointment());
            }

            if (f.isPaginationMode()) {
                int page = Math.max(1, f.getPage());
                int size = Math.max(1, f.getSize());
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

    private ScheduleExceptions mapRow(ResultSet rs) throws SQLException {
        ScheduleExceptions e = new ScheduleExceptions();
        e.setExceptionId(rs.getInt("ExceptionID"));
        Object docIdObj = rs.getObject("DoctorID");
        if (docIdObj != null) {
            Doctor d = new Doctor();
            d.setDoctorID((Integer) docIdObj);
            e.setDoctorId(d);
        }
        e.setExceptionDate(rs.getDate("ExceptionDate"));
        e.setIsWorkingDay(rs.getBoolean("IsWorkingDay"));
        e.setStartTime(rs.getTime("StartTime"));
        e.setEndTime(rs.getTime("EndTime"));
        Object max = rs.getObject("MaxAppointments");
        if (max != null) {
            e.setMaxAppointments((Integer) max);
        }
        return e;
    }
}
