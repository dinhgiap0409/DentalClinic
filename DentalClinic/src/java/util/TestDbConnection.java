/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestDbConnection {
    public static void main(String[] args) {
        /*String jdbcUrl = "jdbc:sqlserver://localhost:1433;databaseName=DentalClinic;encrypt=true;trustServerCertificate=true";
        String dbUser = "sa";
        String dbPass = "123";*/
        String jdbcUrl = "jdbc:sqlserver://DESKTOP-4MP3LIQ\\SQLDENTALCLINIC:1433;databaseName=TestDB;encrypt=false;";
        String dbUser = "tempadmin";
        String dbPass = "abc123";
        System.out.println("Connecting to: " + jdbcUrl);
        try (Connection conn = DriverManager.getConnection(jdbcUrl, dbUser, dbPass)) {
            System.out.println("KẾT NỐI THÀNH CÔNG");
            System.out.println("AutoCommit: " + conn.getAutoCommit());
        } catch (SQLException ex) {
            System.out.println("KẾT NỐI THẤT BẠI: " + ex.getMessage());
            ex.printStackTrace(System.out);
        }
    }
}

