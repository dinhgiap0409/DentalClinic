/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.UserDto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class UsersDao extends DBContext {

    public UsersDao() {
    }

    public List<Users> filterUser(UserDto u) {
        List<Users> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            UserID, Username, PasswordHash, Email, FullName, PhoneNumber,
            DateOfBirth, Gender, Address, Role, IsActive, CreatedDate, Image
        FROM dbo.Users
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // ===== Lọc mềm (LIKE) =====
        if (u.getFullName() != null && !u.getFullName().isBlank()) {
            sql.append(" AND FullName LIKE ? ");
            params.add("%" + u.getFullName().trim() + "%");
        }

        if (u.getPhoneNumber() != null && !u.getPhoneNumber().isBlank()) {
            sql.append(" AND PhoneNumber LIKE ? ");
            params.add("%" + u.getPhoneNumber().trim() + "%");
        }

        if (u.getAddress() != null && !u.getAddress().isBlank()) {
            sql.append(" AND Address LIKE ? ");
            params.add("%" + u.getAddress().trim() + "%");
        }

        // ===== Lọc chính xác =====
        if (u.getDateOfBirth() != null) {
            sql.append(" AND DateOfBirth = ? ");
            params.add(u.getDateOfBirth());
        }

        if (u.getGender() != null && !u.getGender().isBlank()) {
            sql.append(" AND Gender = ? ");
            params.add(u.getGender());
        }

        if (u.getRole() != null && !u.getRole().isBlank()) {
            sql.append(" AND Role = ? ");
            params.add(u.getRole());
        }

        if (u.getIsActive() != null) {
            sql.append(" AND IsActive = ? ");
            params.add(u.getIsActive());
        }

        // ===== Sắp xếp =====
        if (u.isSortMode()) {
            sql.append(" ORDER BY CreatedDate DESC ");
        } else {
            sql.append(" ORDER BY UserID ASC "); // BẮT BUỘC để OFFSET hoạt động
        }

        // ===== Phân trang =====
        if (u.isPaginationMode()) {
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
            int offset = (u.getPage() - 1) * u.getSize();
            params.add(offset);
            params.add(u.getSize());
        }

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
                user.setCreateDate(rs.getDate("CreatedDate"));
                user.setImage(rs.getString("Image"));
                list.add(user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // =====================================================
    // Đếm tổng số bản ghi (phục vụ phân trang)
    // =====================================================
    public int countFilterUser(UserDto u) {
        int count = 0;

        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*) AS total
            FROM dbo.Users
            WHERE 1=1
        """);

        List<Object> params = new ArrayList<>();

        // Điều kiện giống filterUser
        if (u.getFullName() != null && !u.getFullName().isBlank()) {
            sql.append(" AND FullName LIKE ? ");
            params.add("%" + u.getFullName().trim() + "%");
        }

        if (u.getPhoneNumber() != null && !u.getPhoneNumber().isBlank()) {
            sql.append(" AND PhoneNumber LIKE ? ");
            params.add("%" + u.getPhoneNumber().trim() + "%");
        }

        if (u.getAddress() != null && !u.getAddress().isBlank()) {
            sql.append(" AND Address LIKE ? ");
            params.add("%" + u.getAddress().trim() + "%");
        }

        if (u.getDateOfBirth() != null) {
            sql.append(" AND DateOfBirth = ? ");
            params.add(u.getDateOfBirth());
        }

        if (u.getGender() != null && !u.getGender().isBlank()) {
            sql.append(" AND Gender = ? ");
            params.add(u.getGender());
        }

        if (u.getRole() != null && !u.getRole().isBlank()) {
            sql.append(" AND Role = ? ");
            params.add(u.getRole());
        }

        if (u.getIsActive() != null) {
            sql.append(" AND IsActive = ? ");
            params.add(u.getIsActive());
        }

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt("total");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    public int insertUser(Users user) {
        String sql = """
        INSERT INTO [dbo].[Users]
               ([Username]
               ,[PasswordHash]
               ,[Email]
               ,[FullName]
               ,[PhoneNumber]
               ,[DateOfBirth]
               ,[Gender]
               ,[Address]
               ,[Role]
               ,[IsActive]
               ,[Image]
               ,[CreatedDate])
         VALUES
               (?,?,?,?,?,?,?,?,?,?,?,GETDATE());
    """;

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhoneNumber());

            if (user.getDateOfBirth() != null) {
                ps.setDate(6, user.getDateOfBirth());
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, user.getGender());
            ps.setString(8, user.getAddress());
            ps.setString(9, user.getRole());
            ps.setBoolean(10, user.getIsActive());

            // Hình ảnh
            if (user.getImage() != null && !user.getImage().isBlank()) {
                ps.setString(11, user.getImage());
            } else {
                ps.setNull(11, Types.VARCHAR);
            }

            int row = ps.executeUpdate();
            if (row == 0) {
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("⚠️ Username hoặc Email đã tồn tại: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Users login(String email, String passWord) {
        String sql = "SELECT [UserId], [Username], [PasswordHash], [Email], [FullName], "
                + "       [PhoneNumber], [DateOfBirth], [Gender], [Address], [Role], "
                + "       [IsActive], [CreatedDate] "
                + "FROM [dbo].[Users] "
                + "WHERE [Email] = ? AND [PasswordHash] = ? AND [IsActive] = 1";
        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, passWord);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserId"));
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
                    Timestamp ts = rs.getTimestamp("CreatedDate");
                    user.setCreateDate(ts == null ? null : new java.sql.Date(ts.getTime()));
                    return user;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Users getUserById(int userId) {
        String sql = """
        SELECT [UserId], [Username], [PasswordHash], [Email], [FullName],
               [PhoneNumber], [DateOfBirth], [Gender], [Address],
               [Role], [IsActive], [CreatedDate], [Image]
        FROM [dbo].[Users]
        WHERE [UserId] = ?
    """;

        try (Connection con = new DBContext().connection; PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Users user = new Users();
                    user.setUserId(rs.getInt("UserId"));
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
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(Users user) {
        String sql = """
        UPDATE [dbo].[Users]
        SET [Username] = ?,
            [PasswordHash] = ?,
            [Email] = ?,
            [FullName] = ?,
            [PhoneNumber] = ?,
            [DateOfBirth] = ?,
            [Gender] = ?,
            [Address] = ?,
            [Role] = ?,
            [IsActive] = ?,
            [Image] = ?
        WHERE [UserId] = ?
    """;

        try (Connection connect = new DBContext().connection; PreparedStatement ps = connect.prepareStatement(sql)) {

            ps.setString(1, user.getUserName());
            ps.setString(2, user.getPassWord());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getPhoneNumber());

            if (user.getDateOfBirth() != null) {
                ps.setDate(6, user.getDateOfBirth());
            } else {
                ps.setNull(6, Types.DATE);
            }

            ps.setString(7, user.getGender());
            ps.setString(8, user.getAddress());
            ps.setString(9, user.getRole());
            ps.setBoolean(10, user.getIsActive());

            // Image
            if (user.getImage() != null && !user.getImage().isBlank()) {
                ps.setString(11, user.getImage());
            } else {
                ps.setNull(11, Types.VARCHAR);
            }

            ps.setInt(12, user.getUserId());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
