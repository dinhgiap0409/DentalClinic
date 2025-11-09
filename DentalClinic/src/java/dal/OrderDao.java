/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import dto.OrderDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Orders;
import model.Patients;
import model.Prescriptions;
import model.Users;
import java.sql.*;
import model.Doctor;
import model.Medications;
import model.OrderDetails;

/**
 *
 * @author TNO
 */
public class OrderDao {

    public int insertOrderWithDetails(Orders order, List<OrderDetails> detailsList) {
        String sqlOrder = """
            INSERT INTO [dbo].[Orders]
                ([PatientID], [PrescriptionID], [OrderDate], [TotalAmount], [Status], [PaymentMethod], [PaymentStatus])
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        String sqlDetails = """
            INSERT INTO [dbo].[OrderDetails]
                ([OrderID], [MedicationID], [Quantity], [Price])
            VALUES (?, ?, ?, ?)
        """;

        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psDetails = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().connection;
            conn.setAutoCommit(false); // Bắt đầu transaction

            // 1. Chèn order
            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, order.getPatients().getPatientID());
            psOrder.setInt(2, order.getPrescriptions().getPrescriptionId());
            psOrder.setTimestamp(3, order.getOrderDate() != null ? order.getOrderDate() : new Timestamp(System.currentTimeMillis()));
            psOrder.setBigDecimal(4, order.getTotalAmount());
            psOrder.setString(5, order.getStatus());
            psOrder.setString(6, order.getPaymentMethod());
            psOrder.setString(7, order.getPaymentStatus());

            int affectedRows = psOrder.executeUpdate();
            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Creating order failed, no rows affected.");
            }

            rs = psOrder.getGeneratedKeys();
            int orderId;
            if (rs.next()) {
                orderId = rs.getInt(1);
            } else {
                conn.rollback();
                throw new SQLException("Creating order failed, no ID obtained.");
            }

            // 2. Chèn batch order details
            psDetails = conn.prepareStatement(sqlDetails);
            for (OrderDetails detail : detailsList) {
                psDetails.setInt(1, orderId);
                psDetails.setInt(2, detail.getMedicationId().getMedicationId());
                psDetails.setInt(3, detail.getQuantity());
                psDetails.setBigDecimal(4, detail.getPrice());
                psDetails.addBatch();
            }

            psDetails.executeBatch();

            conn.commit(); // commit transaction nếu tất cả OK
            return orderId;

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (psOrder != null) {
                    psOrder.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (psDetails != null) {
                    psDetails.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1; // Nếu thất bại
    }

    public Orders getOrderById(int orderId) {
        Orders order = null;

        String sql = """
        SELECT 
            o.OrderID,
            o.PatientID,
            o.PrescriptionID,
            o.OrderDate,
            o.TotalAmount,
            o.Status,
            o.PaymentMethod,
            o.PaymentStatus,
            
            -- Patient User
            u.UserID AS PatientUserID,
            u.FullName AS PatientName,
            u.PhoneNumber AS PatientPhone,
            
            -- Prescription
            p.PrescriptionID AS PrescID,
            p.RecordID AS PrescRecordID,
            p.DoctorID AS PrescDoctorID,
            p.IssueDate AS PrescIssueDate,
            p.Instructions AS PrescInstructions,
            
            -- Doctor
            d.DoctorID AS DoctorID,
            d.ConsultationFee AS DoctorFee,
            
            -- Doctor User
            du.UserID AS DoctorUserID,
            du.FullName AS DoctorFullName,
            du.PhoneNumber AS DoctorPhone
        FROM dbo.Orders o
        JOIN dbo.Patients pa ON o.PatientID = pa.PatientID
        JOIN dbo.Users u ON pa.UserID = u.UserID
        LEFT JOIN dbo.Prescriptions p ON o.PrescriptionID = p.PrescriptionID
        LEFT JOIN dbo.Doctors d ON p.DoctorID = d.DoctorID
        LEFT JOIN dbo.Users du ON d.UserID = du.UserID
        WHERE o.OrderID = ?
    """;

        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // Dùng lại hàm mapRow đã viết
                    order = mapRow(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    public List<Orders> filterOrders(OrderDto o) {
        List<Orders> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
        SELECT 
            o.OrderID,
            o.PatientID,
            o.PrescriptionID,
            o.OrderDate,
            o.TotalAmount,
            o.Status,
            o.PaymentMethod,
            o.PaymentStatus,
            
            -- Patient User
            u.UserID AS PatientUserID,
            u.FullName AS PatientName,
            u.PhoneNumber AS PatientPhone,
            
            -- Prescription
            p.PrescriptionID AS PrescID,
            p.RecordID AS PrescRecordID,
            p.DoctorID AS PrescDoctorID,
            p.IssueDate AS PrescIssueDate,
            p.Instructions AS PrescInstructions,
            
            -- Doctor
            d.DoctorID AS DoctorID,
            d.ConsultationFee AS DoctorFee,
            
            -- Doctor User
            du.UserID AS DoctorUserID,
            du.FullName AS DoctorFullName,
            du.PhoneNumber AS DoctorPhone
        FROM dbo.Orders o
        JOIN dbo.Patients pa ON o.PatientID = pa.PatientID
        JOIN dbo.Users u ON pa.UserID = u.UserID
        LEFT JOIN dbo.Prescriptions p ON o.PrescriptionID = p.PrescriptionID
        LEFT JOIN dbo.Doctors d ON p.DoctorID = d.DoctorID
        LEFT JOIN dbo.Users du ON d.UserID = du.UserID
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // --- Dynamic WHERE conditions ---
        if (o.getPatientId() != null) {
            sql.append(" AND o.PatientID = ? ");
            params.add(o.getPatientId());
        }
        if (o.getPatientName() != null && !o.getPatientName().isBlank()) {
            sql.append(" AND u.FullName LIKE ? ");
            params.add("%" + o.getPatientName().trim() + "%");
        }
        if (o.getPrescriptionId() != null) {
            sql.append(" AND o.PrescriptionID = ? ");
            params.add(o.getPrescriptionId());
        }
        if (o.getOrderDateFrom() != null) {
            sql.append(" AND CAST(o.OrderDate AS DATE) >= ? ");
            params.add(o.getOrderDateFrom());
        }
        if (o.getOrderDateTo() != null) {
            sql.append(" AND CAST(o.OrderDate AS DATE) <= ? ");
            params.add(o.getOrderDateTo());
        }
        if (o.getTotalAmountFrom() != null) {
            sql.append(" AND o.TotalAmount >= ? ");
            params.add(o.getTotalAmountFrom());
        }
        if (o.getTotalAmountTo() != null) {
            sql.append(" AND o.TotalAmount <= ? ");
            params.add(o.getTotalAmountTo());
        }
        if (o.getStatus() != null && !o.getStatus().isBlank()) {
            sql.append(" AND o.Status = ? ");
            params.add(o.getStatus().trim());
        }
        if (o.getPaymentStatus() != null && !o.getPaymentStatus().isBlank()) {
            sql.append(" AND o.PaymentStatus = ? ");
            params.add(o.getPaymentStatus().trim());
        }

        // --- ORDER BY ---
        sql.append(" ORDER BY o.OrderDate ");
        sql.append(o.isSortMode() ? "ASC" : "DESC");

        // --- Pagination ---
        if (o.isPaginationMode()) {
            sql.append(" OFFSET ? ROWS FETCH NEXT ? ROWS ONLY ");
        }

        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int i = 1;
            // Set WHERE parameters
            for (Object param : params) {
                if (param instanceof Integer) {
                    ps.setInt(i++, (Integer) param);
                } else if (param instanceof String) {
                    ps.setString(i++, (String) param);
                } else if (param instanceof Date) {
                    ps.setDate(i++, (Date) param);
                } else if (param instanceof BigDecimal) {
                    ps.setBigDecimal(i++, (BigDecimal) param);
                }
            }

            // Set pagination
            if (o.isPaginationMode()) {
                int offset = (o.getPage() - 1) * o.getSize();
                int limit = o.getSize();
                ps.setInt(i++, offset);
                ps.setInt(i++, limit);
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

    public int countFilter(OrderDto o) {
        int count = 0;

        StringBuilder sql = new StringBuilder("""
        SELECT COUNT(*) AS Total
        FROM dbo.Orders o
        JOIN dbo.Patients pa ON o.PatientID = pa.PatientID
        JOIN dbo.Users u ON pa.UserID = u.UserID
        LEFT JOIN dbo.Prescriptions p ON o.PrescriptionID = p.PrescriptionID
        LEFT JOIN dbo.Doctors d ON p.DoctorID = d.DoctorID
        LEFT JOIN dbo.Users du ON d.UserID = du.UserID
        WHERE 1=1
    """);

        List<Object> params = new ArrayList<>();

        // --- Dynamic WHERE conditions (giống filterOrders) ---
        if (o.getPatientId() != null) {
            sql.append(" AND o.PatientID = ? ");
            params.add(o.getPatientId());
        }
        if (o.getPatientName() != null && !o.getPatientName().isBlank()) {
            sql.append(" AND u.FullName LIKE ? ");
            params.add("%" + o.getPatientName().trim() + "%");
        }
        if (o.getPrescriptionId() != null) {
            sql.append(" AND o.PrescriptionID = ? ");
            params.add(o.getPrescriptionId());
        }
        if (o.getOrderDateFrom() != null) {
            sql.append(" AND CAST(o.OrderDate AS DATE) >= ? ");
            params.add(o.getOrderDateFrom());
        }
        if (o.getOrderDateTo() != null) {
            sql.append(" AND CAST(o.OrderDate AS DATE) <= ? ");
            params.add(o.getOrderDateTo());
        }
        if (o.getTotalAmountFrom() != null) {
            sql.append(" AND o.TotalAmount >= ? ");
            params.add(o.getTotalAmountFrom());
        }
        if (o.getTotalAmountTo() != null) {
            sql.append(" AND o.TotalAmount <= ? ");
            params.add(o.getTotalAmountTo());
        }
        if (o.getStatus() != null && !o.getStatus().isBlank()) {
            sql.append(" AND o.Status = ? ");
            params.add(o.getStatus().trim());
        }
        if (o.getPaymentStatus() != null && !o.getPaymentStatus().isBlank()) {
            sql.append(" AND o.PaymentStatus = ? ");
            params.add(o.getPaymentStatus().trim());
        }

        try (Connection connection = new DBContext().connection; PreparedStatement ps = connection.prepareStatement(sql.toString())) {

            int i = 1;
            for (Object param : params) {
                if (param instanceof Integer) {
                    ps.setInt(i++, (Integer) param);
                } else if (param instanceof String) {
                    ps.setString(i++, (String) param);
                } else if (param instanceof Date) {
                    ps.setDate(i++, (Date) param);
                } else if (param instanceof BigDecimal) {
                    ps.setBigDecimal(i++, (BigDecimal) param);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("Total");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    private Orders mapRow(ResultSet rs) throws SQLException {
        // --- Patient User ---
        Users patientUser = new Users();
        patientUser.setUserId(rs.getInt("PatientUserID"));
        patientUser.setFullName(rs.getString("PatientName"));
        patientUser.setPhoneNumber(rs.getString("PatientPhone"));

        // --- Patient ---
        Patients patient = new Patients();
        patient.setPatientID(rs.getInt("PatientID"));
        patient.setUserID(patientUser);

        // --- Doctor User ---
        Users doctorUser = new Users();
        doctorUser.setUserId(rs.getInt("DoctorUserID"));
        doctorUser.setFullName(rs.getString("DoctorFullName"));
        doctorUser.setPhoneNumber(rs.getString("DoctorPhone"));

        // --- Doctor ---
        Doctor doctor = new Doctor();
        doctor.setDoctorID(rs.getInt("DoctorID"));
        doctor.setUserId(doctorUser);
        doctor.setConsultationFee(rs.getBigDecimal("DoctorFee"));

        // --- Prescription ---
        Prescriptions prescription = new Prescriptions();
        prescription.setPrescriptionId(rs.getInt("PrescID"));
        prescription.setDoctorId(doctor);
        prescription.setIssueDate(rs.getTimestamp("PrescIssueDate"));
        prescription.setInstructions(rs.getString("PrescInstructions"));

        // --- Order ---
        Orders order = new Orders();
        order.setOrderId(rs.getInt("OrderID"));
        order.setOrderDate(rs.getTimestamp("OrderDate"));
        order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        order.setStatus(rs.getString("Status"));
        order.setPaymentMethod(rs.getString("PaymentMethod"));
        order.setPaymentStatus(rs.getString("PaymentStatus"));

        order.setPatients(patient);
        order.setPrescriptions(prescription);

        return order;
    }

    public List<OrderDetails> getOrderDetailsByOrderId(int orderId) {
        List<OrderDetails> list = new ArrayList<>();

        String sql = """
        SELECT 
            od.OrderDetailID,
            od.OrderID,
            od.MedicationID,
            od.Quantity,
            od.Price,
            
            -- Thông tin thuốc
            m.MedicationName,
            m.Description,
            m.Price AS MedPrice,
            m.StockQuantity,
            m.ExpiryDate,
            m.Manufacturer,
            m.IsActive
        FROM dbo.OrderDetails od
        JOIN dbo.Medications m ON od.MedicationID = m.MedicationID
        WHERE od.OrderID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderDetails od = new OrderDetails();

                    // Ánh xạ Order
                    Orders order = new Orders();
                    order.setOrderId(rs.getInt("OrderID"));
                    od.setOrderId(order);

                    // Ánh xạ Medication
                    Medications med = new Medications();
                    med.setMedicationId(rs.getInt("MedicationID"));
                    med.setMedicationName(rs.getString("MedicationName"));
                    med.setDescription(rs.getString("Description"));
                    med.setPrice(rs.getBigDecimal("MedPrice"));
                    med.setStockQuantity(rs.getInt("StockQuantity"));
                    med.setExpiryDate(rs.getDate("ExpiryDate"));
                    med.setManufacturer(rs.getString("Manufacturer"));
                    med.setIsActive(rs.getBoolean("IsActive"));
                    od.setMedicationId(med);

                    // Các trường OrderDetails
                    od.setOrderDetailId(rs.getInt("OrderDetailID"));
                    od.setQuantity(rs.getInt("Quantity"));
                    od.setPrice(rs.getBigDecimal("Price"));

                    list.add(od);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean updateOrder(Orders order) {
        String sql = """
        UPDATE [dbo].[Orders]
        SET PatientID = ?, 
            PrescriptionID = ?, 
            OrderDate = ?, 
            TotalAmount = ?, 
            Status = ?, 
            PaymentMethod = ?, 
            PaymentStatus = ?
        WHERE OrderID = ?
    """;

        try (Connection conn = new DBContext().connection; PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, order.getPatients().getPatientID());
            ps.setInt(2, order.getPrescriptions().getPrescriptionId());
            ps.setTimestamp(3, order.getOrderDate() != null ? order.getOrderDate() : new Timestamp(System.currentTimeMillis()));
            ps.setBigDecimal(4, order.getTotalAmount());
            ps.setString(5, order.getStatus());
            ps.setString(6, order.getPaymentMethod());
            ps.setString(7, order.getPaymentStatus());
            ps.setInt(8, order.getOrderId());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

}
