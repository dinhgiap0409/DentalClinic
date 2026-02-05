/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.*;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
public class UsersDao extends DBContext {

    public UsersDao() {
    }

    public int insertUser(Users user) {
        String sql = "INSERT INTO [dbo].[Users]\n"
                + "           ([Username]\n"
                + "           ,[PasswordHash]\n"
                + "           ,[Email]\n"
                + "           ,[FullName]\n"
                + "           ,[PhoneNumber]\n"
                + "           ,[DateOfBirth]\n"
                + "           ,[Gender]\n"
                + "           ,[Address]\n"
                + "           ,[Role]\n"
                + "           ,[IsActive]\n"
                + "           ,[CreatedDate])\n"
                + "     VALUES\n"
                + "           (?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,?\n"
                + "           ,GETDATE());";
        try (Connection connect = new DBContext().connection;
                PreparedStatement ps = connect.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            ps.setBoolean(10, user.isIsActive());
            int row = ps.executeUpdate();
            if (row == 0) {
                return -1;
            }
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                System.err.println("User Name / Email is exsited" + e.getMessage());
            }
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
        String sql = "SELECT [UserId], [Username], [PasswordHash], [Email], [FullName], "
                + "       [PhoneNumber], [DateOfBirth], [Gender], [Address], [Role], "
                + "       [IsActive], [CreatedDate] "
                + "FROM [dbo].[Users] "
                + "WHERE [UserId] = ?";
        try (Connection conection = new DBContext().connection; PreparedStatement ps = conection.prepareStatement(sql)) {
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

    public boolean updateUser(Users user) {
        String sql = "UPDATE [dbo].[Users] "
                + "SET [Username] = ?, "
                + "    [PasswordHash] = ?, "
                + "    [Email] = ?, "
                + "    [FullName] = ?, "
                + "    [PhoneNumber] = ?, "
                + "    [DateOfBirth] = ?, "
                + "    [Gender] = ?, "
                + "    [Address] = ?, "
                + "    [Role] = ?, "
                + "    [IsActive] = ? "
                + "WHERE [UserId] = ?";
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
            ps.setBoolean(10, user.isIsActive());
            ps.setInt(11, user.getUserId());
            int row = ps.executeUpdate();
            return row > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
