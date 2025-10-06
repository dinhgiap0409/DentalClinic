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

    //ham lay lich trinh theo id
    public Schedules getScheduleById(int scheduleId) {
        //cau lenh sql lay du lieu tu bang schedule
        String sql = """
            SELECT ScheduleID, DoctorID, DayOfWeek, StartTime, EndTime,
                   IsAvailable, MaxAppointments, ValidFrom, ValidTo,
                   RequiresApproval, IsApproved, ApprovedBy, ApprovedDate, CreatedDate
            FROM dbo.Schedules
            WHERE ScheduleID = ?
        """;
        //mo ket noi
        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql)) {
            //set gia tri scheduleId
            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //map du lieu tu resultset sang object schedules
                    Schedules s = new Schedules();
                    s.setScheduleId(rs.getInt("ScheduleID"));
                    Object doctorIdObj = rs.getObject("DoctorID");
                    if (doctorIdObj != null) {
                        Doctor d = new Doctor();
                        //ep object kieu ve int
                        d.setDoctorID((Integer) doctorIdObj);
                        s.setDoctorId(d);
                    } else {
                        s.setDoctorId(null);
                    }

                    s.setDayOfWeek(rs.getInt("DayOfWeek"));
                    s.setStartTime(rs.getTime("StartTime"));
                    s.setEndTime(rs.getTime("EndTime"));
                    s.setIsAvailable(rs.getBoolean("IsAvailable"));
                    s.setMaxAppointments(rs.getInt("MaxAppointments"));
                    s.setValidFrom(rs.getDate("ValidFrom"));
                    s.setValidTo(rs.getDate("ValidTo"));
                    s.setRequiresApproval(rs.getBoolean("RequiresApproval"));
                    s.setIsApproved(rs.getBoolean("IsApproved"));
                    Object apByObj = rs.getObject("ApprovedBy");
                    if (apByObj != null) {
                        Users u = new Users();
                        //ep object kieu ve int
                        u.setUserId((Integer) apByObj);
                        s.setApprovedBy(u);
                    } else {
                        s.setApprovedBy(null);
                    }
                    s.setApprovedDate(rs.getTimestamp("ApprovedDate"));
                    s.setCreatedDate(rs.getTimestamp("CreatedDate"));
                    return s;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //ko tim thay tra ve null
        return null;
    }

    public List<Schedules> filterSchedules(ScheduleDto f) {
        List<Schedules> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(("""
        SELECT ScheduleID, DoctorID, DayOfWeek, StartTime, EndTime,
               IsAvailable, MaxAppointments, ValidFrom, ValidTo,
               RequiresApproval, IsApproved, ApprovedBy, ApprovedDate, CreatedDate FROM dbo.Schedules WHERE 1=1"""));
        if (f.getDoctorId() != null) {
            sql.append(" AND DoctorID = ?\n");
        }
        if (f.getDayOfWeek() != null) {
            sql.append(" AND DayOfWeek = ?\n");
        }
        if (f.getStartTime() != null) {
            sql.append(" AND StartTime >= ?\n");
        }
        if (f.getEndTime() != null) {
            sql.append(" AND EndTime <= ?\n");
        }
        if (f.getIsAvailable() != null) {
            sql.append(" AND IsAvailable = ?\n");
        }
        if (f.getMaxAppointment() != null) {
            sql.append(" AND MaxAppointments >= ?\n");
        }
        if (f.getValidFrom() != null) {
            sql.append(" AND ValidFrom >= ?\n");
        }
        if (f.getValidTo() != null) {
            sql.append(" AND ValidTo <= ?\n");
        }
        if (f.getRequiresApproval() != null) {
            sql.append(" AND RequiresApproval = ?\n");
        }
        if (f.getIsApproved() != null) {
            sql.append(" AND IsApproved = ?\n");
        }
        if (f.getApprovedBy() != null) {
            sql.append(" AND ApprovedBy = ?\n");
        }
        if (f.getApprovedDate() != null) {
            sql.append(" AND ApprovedDate >= ?\n");
        }
        if (f.getCreatedDate() != null) {
            sql.append(" AND CreatedDate >= ?\n");
        }
        if (f.isSortMode()) {
            sql.append(" ORDER BY ScheduleID ASC\n");
        }
        if (f.isPaginationMode()) {
            if (!f.isSortMode()) {
                sql.append(" ORDER BY ScheduleID ASC\n");
            }
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        }
        try (Connection c = new DBContext().connection; PreparedStatement ps = c.prepareStatement(sql.toString())) {

            int i = 1;
            if (f.getDoctorId() != null) {
                ps.setInt(i++, f.getDoctorId()); 
            }
            if (f.getDayOfWeek() != null) {
                ps.setInt(i++, f.getDayOfWeek());
            }
            if (f.getStartTime() != null) {
                ps.setTime(i++, f.getStartTime());
            }
            if (f.getEndTime() != null) {
                ps.setTime(i++, f.getEndTime());
            }
            if (f.getIsAvailable() != null) {
                ps.setBoolean(i++, f.getIsAvailable());
            }
            if (f.getMaxAppointment() != null) {
                ps.setInt(i++, f.getMaxAppointment());
            }
            if (f.getValidFrom() != null) {
                ps.setDate(i++, new java.sql.Date(f.getValidFrom().getTime()));
            }
            if (f.getValidTo() != null) {
                ps.setDate(i++, new java.sql.Date(f.getValidTo().getTime()));
            }
            if (f.getRequiresApproval() != null) {
                ps.setBoolean(i++, f.getRequiresApproval());
            }
            if (f.getIsApproved() != null) {
                ps.setBoolean(i++, f.getIsApproved());
            }
            if (f.getApprovedBy() != null) {
                ps.setInt(i++, f.getApprovedBy());
            }
            if (f.getApprovedDate() != null) {
                ps.setTimestamp(i++, f.getApprovedDate());
            }
            if (f.getCreatedDate() != null) {
                ps.setTimestamp(i++, f.getCreatedDate());
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

    private Schedules mapRow(ResultSet rs) throws SQLException {
        Schedules s = new Schedules();
        s.setScheduleId(rs.getInt("ScheduleID"));
        Object docIdObj = rs.getObject("DoctorID");
        if (docIdObj != null) {
            Doctor d = new Doctor();
            d.setDoctorID((Integer) docIdObj);
            s.setDoctorId(d);
        }
        s.setDayOfWeek(rs.getInt("DayOfWeek"));
        s.setStartTime(rs.getTime("StartTime"));
        s.setEndTime(rs.getTime("EndTime"));
        s.setIsAvailable(rs.getBoolean("IsAvailable"));
        s.setMaxAppointments(rs.getInt("MaxAppointments"));
        s.setValidFrom(rs.getDate("ValidFrom"));
        s.setValidTo(rs.getDate("ValidTo"));
        s.setRequiresApproval(rs.getBoolean("RequiresApproval"));
        s.setIsApproved(rs.getBoolean("IsApproved"));

        Object apByObj = rs.getObject("ApprovedBy");
        if (apByObj != null) {
            Users u = new Users();
            u.setUserId((Integer) apByObj);
            s.setApprovedBy(u);
        }

        s.setApprovedDate(rs.getTimestamp("ApprovedDate"));
        s.setCreatedDate(rs.getTimestamp("CreatedDate"));
        return s;
    }

}
