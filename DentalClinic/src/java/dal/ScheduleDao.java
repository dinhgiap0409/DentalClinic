/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.ScheduleDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;
import model.Schedules;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class ScheduleDao extends DBContext {

    public Schedules getScheduleById(int scheduleId) {
        String sql = """
        SELECT s.ScheduleID, s.DoctorID, s.DayOfWeek, s.StartTime, s.EndTime,
               s.IsAvailable, s.MaxAppointments, s.ValidFrom, s.ValidTo,
               s.RequiresApproval, s.IsApproved, s.ApprovedBy, s.ApprovedDate, s.CreatedDate,
               d.DoctorID AS DoctorID, d.Specialization,
               u.UserID AS DoctorUserID, u.UserName AS DoctorUserName, u.FullName AS DoctorFullName,
               ab.UserID AS ApprovedUserID, ab.FullName AS ApprovedByName
        FROM dbo.Schedules s
        LEFT JOIN dbo.Doctors d ON s.DoctorID = d.DoctorID
        LEFT JOIN dbo.Users u ON d.UserID = u.UserID
        LEFT JOIN dbo.Users ab ON s.ApprovedBy = ab.UserID
        WHERE s.ScheduleID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Schedules s = new Schedules();
                    s.setScheduleId(rs.getInt("ScheduleID"));
                    s.setDayOfWeek(rs.getInt("DayOfWeek"));
                    s.setStartTime(rs.getTime("StartTime"));
                    s.setEndTime(rs.getTime("EndTime"));
                    s.setIsAvailable(rs.getBoolean("IsAvailable"));
                    s.setMaxAppointments(rs.getInt("MaxAppointments"));
                    s.setValidFrom(rs.getDate("ValidFrom"));
                    s.setValidTo(rs.getDate("ValidTo"));
                    s.setRequiresApproval(rs.getBoolean("RequiresApproval"));
                    s.setIsApproved(rs.getBoolean("IsApproved"));
                    s.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    s.setCreatedDate(rs.getTimestamp("CreatedDate"));

                    // ----- Doctor -----
                    int doctorId = rs.getInt("DoctorID");
                    if (!rs.wasNull()) {
                        Doctor doctor = new Doctor();
                        doctor.setDoctorID(doctorId);
                        doctor.setSpecialization(rs.getString("Specialization"));

                        Users doctorUser = new Users();
                        doctorUser.setUserId(rs.getInt("DoctorUserID"));
                        doctorUser.setUserName(rs.getString("DoctorUserName"));
                        doctorUser.setFullName(rs.getString("DoctorFullName"));

                        doctor.setUserId(doctorUser);
                        s.setDoctorId(doctor);
                    }

                    // ----- ApprovedBy -----
                    int approvedById = rs.getInt("ApprovedUserID");
                    if (!rs.wasNull()) {
                        Users approvedUser = new Users();
                        approvedUser.setUserId(approvedById);
                        approvedUser.setFullName(rs.getString("ApprovedByName"));
                        s.setApprovedBy(approvedUser);
                    }

                    return s;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Schedules> filterSchedules(ScheduleDto f) {
        List<Schedules> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT s.[ScheduleID], s.[DoctorID], s.[DayOfWeek], s.[StartTime], s.[EndTime],
               s.[IsAvailable], s.[MaxAppointments], s.[ValidFrom], s.[ValidTo],
               s.[RequiresApproval], s.[IsApproved], s.[ApprovedBy],
               s.[ApprovedDate], s.[CreatedDate],
               u.[UserID], u.[UserName], u.[FullName]
        FROM [dbo].[Schedules] s
        INNER JOIN [dbo].[Doctors] d ON s.DoctorID = d.DoctorID
        INNER JOIN [dbo].[Users] u ON d.UserID = u.UserID
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // ====== Điều kiện lọc linh hoạt ======
        if (f.getDoctorId() != null) {
            sql.append(" AND s.DoctorID = ?");
            params.add(f.getDoctorId());
        }

        if (f.getDoctorName() != null && !f.getDoctorName().isEmpty()) {
            sql.append(" AND (u.UserName LIKE ? OR u.FullName LIKE ?)");
            String namePattern = "%" + f.getDoctorName() + "%";
            params.add(namePattern);
            params.add(namePattern);
        }

        if (f.getDayOfWeek() != null) {
            sql.append(" AND s.DayOfWeek = ?");
            params.add(f.getDayOfWeek());
        }

        if (f.getStartTime() != null) {
            sql.append(" AND s.StartTime >= CAST(? AS time(0))");
            params.add(f.getStartTime());
        }

        if (f.getEndTime() != null) {
            sql.append(" AND s.EndTime <= CAST(? AS time(0))");
            params.add(f.getEndTime());
        }

        if (f.getIsAvailable() != null) {
            sql.append(" AND s.IsAvailable = ?");
            params.add(f.getIsAvailable());
        }

        if (f.getRequiresApproval() != null) {
            sql.append(" AND s.RequiresApproval = ?");
            params.add(f.getRequiresApproval());
        }

        if (f.getIsApproved() != null) {
            sql.append(" AND s.IsApproved = ?");
            params.add(f.getIsApproved());
        }

        // ====== Sắp xếp ======
        sql.append(" ORDER BY s.ScheduleID ");
        sql.append(f.isSortMode() ? "ASC" : "DESC");

        // ====== Phân trang ======
        if (f.isPaginationMode()) {
            int offset = (f.getPage() - 1) * f.getSize();
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
            params.add(offset);
            params.add(f.getSize());
        }

        // ====== Thực thi ======
        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Schedules s = new Schedules();
                    s.setScheduleId(rs.getInt("ScheduleID"));

                    Doctor d = new Doctor();
                    d.setDoctorID(rs.getInt("DoctorID"));

                    Users u = new Users();
                    u.setUserId(rs.getInt("UserID"));
                    u.setUserName(rs.getString("UserName"));
                    u.setFullName(rs.getString("FullName"));
                    d.setUserId(u);

                    s.setDoctorId(d);
                    s.setDayOfWeek(rs.getInt("DayOfWeek"));
                    s.setStartTime(rs.getTime("StartTime"));
                    s.setEndTime(rs.getTime("EndTime"));
                    s.setIsAvailable(rs.getBoolean("IsAvailable"));
                    s.setMaxAppointments(rs.getInt("MaxAppointments"));
                    s.setValidFrom(rs.getDate("ValidFrom"));
                    s.setValidTo(rs.getDate("ValidTo"));
                    s.setRequiresApproval(rs.getBoolean("RequiresApproval"));
                    s.setIsApproved(rs.getBoolean("IsApproved"));

                    Users approvedBy = new Users();
                    approvedBy.setUserId(rs.getInt("ApprovedBy"));
                    s.setApprovedBy(approvedBy);

                    s.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    s.setCreatedDate(rs.getTimestamp("CreatedDate"));

                    list.add(s);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public int countFilter(ScheduleDto f) {
        int count = 0;

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) AS total
        FROM [dbo].[Schedules] s
        INNER JOIN [dbo].[Doctors] d ON s.DoctorID = d.DoctorID
        INNER JOIN [dbo].[Users] u ON d.UserID = u.UserID
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // ====== Điều kiện lọc linh hoạt ======
        if (f.getDoctorId() != null) {
            sql.append(" AND s.DoctorID = ?");
            params.add(f.getDoctorId());
        }

        if (f.getDoctorName() != null && !f.getDoctorName().isEmpty()) {
            sql.append(" AND (u.UserName LIKE ? OR u.FullName LIKE ?)");
            String namePattern = "%" + f.getDoctorName() + "%";
            params.add(namePattern);
            params.add(namePattern);
        }

        if (f.getDayOfWeek() != null) {
            sql.append(" AND s.DayOfWeek = ?");
            params.add(f.getDayOfWeek());
        }

        if (f.getStartTime() != null) {
            sql.append(" AND s.StartTime >= CAST(? AS time(0))");
            params.add(f.getStartTime());
        }

        if (f.getEndTime() != null) {
            sql.append(" AND s.EndTime <= CAST(? AS time(0))");
            params.add(f.getEndTime());
        }

        if (f.getIsAvailable() != null) {
            sql.append(" AND s.IsAvailable = ?");
            params.add(f.getIsAvailable());
        }

        if (f.getRequiresApproval() != null) {
            sql.append(" AND s.RequiresApproval = ?");
            params.add(f.getRequiresApproval());
        }

        if (f.getIsApproved() != null) {
            sql.append(" AND s.IsApproved = ?");
            params.add(f.getIsApproved());
        }

        // ====== Thực thi ======
        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }

    public int insertSchedule(Schedules schedule) {
        String sql = """
        INSERT INTO dbo.Schedules
            (DoctorID, DayOfWeek, StartTime, EndTime, IsAvailable,
             MaxAppointments, ValidFrom, ValidTo, RequiresApproval,
             IsApproved, ApprovedBy, ApprovedDate, CreatedDate)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // DoctorID
            if (schedule.getDoctorId() != null) {
                ps.setInt(1, schedule.getDoctorId().getDoctorID());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, schedule.getDayOfWeek());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.setBoolean(5, schedule.isIsAvailable());
            ps.setInt(6, schedule.getMaxAppointments());
            ps.setDate(7, schedule.getValidFrom());
            if (schedule.getValidTo() != null) {
                ps.setDate(8, schedule.getValidTo());
            } else {
                ps.setNull(8, Types.DATE);
            }
            ps.setBoolean(9, schedule.isRequiresApproval());
            ps.setBoolean(10, schedule.isIsApproved());

            // ApprovedBy
            if (schedule.getApprovedBy() != null) {
                ps.setInt(11, schedule.getApprovedBy().getUserId());
            } else {
                ps.setNull(11, Types.INTEGER);
            }

            if (schedule.getApprovedDate() != null) {
                ps.setTimestamp(12, schedule.getApprovedDate());
            } else {
                ps.setNull(12, Types.TIMESTAMP);
            }

            if (schedule.getCreatedDate() != null) {
                ps.setTimestamp(13, schedule.getCreatedDate());
            } else {
                ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
            }

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int newId = rs.getInt(1);
                        schedule.setScheduleId(newId);
                        return newId;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public boolean updateSchedule(Schedules schedule) {
        if (schedule.getScheduleId() == 0) {
            return false;
        }

        String sql = """
        UPDATE dbo.Schedules
        SET DoctorID = ?, DayOfWeek = ?, StartTime = ?, EndTime = ?, IsAvailable = ?,
            MaxAppointments = ?, ValidFrom = ?, ValidTo = ?, RequiresApproval = ?,
            IsApproved = ?, ApprovedBy = ?, ApprovedDate = ?, CreatedDate = ?
        WHERE ScheduleID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            // DoctorID
            if (schedule.getDoctorId() != null) {
                ps.setInt(1, schedule.getDoctorId().getDoctorID());
            } else {
                ps.setNull(1, Types.INTEGER);
            }

            ps.setInt(2, schedule.getDayOfWeek());
            ps.setTime(3, schedule.getStartTime());
            ps.setTime(4, schedule.getEndTime());
            ps.setBoolean(5, schedule.isIsAvailable());
            ps.setInt(6, schedule.getMaxAppointments());
            ps.setDate(7, schedule.getValidFrom());

            if (schedule.getValidTo() != null) {
                ps.setDate(8, schedule.getValidTo());
            } else {
                ps.setNull(8, Types.DATE);
            }

            ps.setBoolean(9, schedule.isRequiresApproval());
            ps.setBoolean(10, schedule.isIsApproved());

            // ApprovedBy
            if (schedule.getApprovedBy() != null) {
                ps.setInt(11, schedule.getApprovedBy().getUserId());
            } else {
                ps.setNull(11, Types.INTEGER);
            }

            if (schedule.getApprovedDate() != null) {
                ps.setTimestamp(12, schedule.getApprovedDate());
            } else {
                ps.setNull(12, Types.TIMESTAMP);
            }

            if (schedule.getCreatedDate() != null) {
                ps.setTimestamp(13, schedule.getCreatedDate());
            } else {
                ps.setTimestamp(13, new Timestamp(System.currentTimeMillis()));
            }

            // WHERE ScheduleID
            ps.setInt(14, schedule.getScheduleId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
