/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Order;

import dal.AppointmentsDao;
import dal.MedicalRecordDao;
import dal.MedicationsDao;
import dal.OrderDao;
import dal.PrescriptionDao;
import dto.MedicalRecordDto;
import dto.PrescriptionDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import model.Appointments;
import model.MedicalRecords;
import model.Medications;
import model.OrderDetails;
import model.Orders;
import model.Patients;
import model.PrescriptionDetails;
import model.Prescriptions;
import java.sql.Timestamp;
import model.Doctor;

/**
 *
 * @author TNO
 */
@WebServlet(name = "CreateOrderController", urlPatterns = {"/createOrder"})
public class CreateOrderController extends HttpServlet {

    private AppointmentsDao appointmentDao = new AppointmentsDao();
    private MedicalRecordDao recordDao = new MedicalRecordDao();
    private PrescriptionDao prescriptionDao = new PrescriptionDao();
    private MedicationsDao medicationDao = new MedicationsDao();
    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("appointmentId");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã lịch hẹn.");
            return;
        }

        int appointmentId = Integer.parseInt(idParam);
        Appointments appointment = appointmentDao.getAppointmentsById(appointmentId);
        if (appointment == null) {
            request.setAttribute("error", "Không tìm thấy lịch hẹn.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        if (!"Completed".equalsIgnoreCase(appointment.getStatus())) {
            request.setAttribute("error", "Chỉ có thể tạo order cho lịch hẹn đã hoàn tất.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Lấy MedicalRecord
        MedicalRecordDto recordDto = new MedicalRecordDto();
        recordDto.setAppointmentId(appointmentId);
        recordDto.setPaginationMode(false);

        List<MedicalRecords> recordList = recordDao.filterMedicalRecord(recordDto);
        if (recordList.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy hồ sơ bệnh án.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        MedicalRecords record = recordList.get(0);

        // Lấy Prescription
        PrescriptionDto presDto = new PrescriptionDto();
        presDto.setRecordId(record.getRecordId());
        presDto.setPaginationMode(false);

        List<Prescriptions> presList = prescriptionDao.filterPrescription(presDto);
        Prescriptions prescription = presList.isEmpty() ? null : presList.get(0);

        // Lấy PrescriptionDetails
        List<PrescriptionDetails> details = new ArrayList<>();
        if (prescription != null) {
            details = prescriptionDao.findByPrescriptionId(prescription.getPrescriptionId());
        }

        request.setAttribute("appointment", appointment);
        request.setAttribute("record", record);
        request.setAttribute("prescription", prescription);
        request.setAttribute("details", details);

        request.getRequestDispatcher("/views/receptionist/create-order.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            int patientId = Integer.parseInt(request.getParameter("patientId"));

            String[] medIds = request.getParameterValues("medicationId");
            String[] quantities = request.getParameterValues("quantity");

            if (medIds == null || quantities == null || medIds.length != quantities.length) {
                request.setAttribute("error", "Dữ liệu thuốc không hợp lệ.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            BigDecimal totalAmount = BigDecimal.ZERO;
            List<OrderDetails> orderDetailsList = new ArrayList<>();

            // Tính tổng tiền thuốc
            for (int i = 0; i < medIds.length; i++) {
                int medId = Integer.parseInt(medIds[i]);
                int qty = Integer.parseInt(quantities[i]);
                if (qty <= 0) {
                    continue;
                }

                Medications med = medicationDao.getMedicationById(medId);
                if (med == null) {
                    continue;
                }

                BigDecimal lineTotal = med.getPrice().multiply(BigDecimal.valueOf(qty));
                totalAmount = totalAmount.add(lineTotal);

                OrderDetails detail = new OrderDetails();
                detail.setMedicationId(med);
                detail.setQuantity(qty);
                detail.setPrice(med.getPrice());
                orderDetailsList.add(detail);
            }

            if (orderDetailsList.isEmpty()) {
                request.setAttribute("error", "Chưa chọn thuốc để tạo đơn.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Lấy phí khám bác sĩ
            Appointments appointment = appointmentDao.getAppointmentsById(appointmentId);
            Doctor doctor = appointment.getDoctorId();
            if (doctor != null && doctor.getConsultationFee() != null) {
                totalAmount = totalAmount.add(doctor.getConsultationFee());
            }

            // Lấy Prescription từ hồ sơ 
            PrescriptionDto presDto = new PrescriptionDto();
            presDto.setAppointmentId(appointment.getAppointmentId());
            presDto.setPaginationMode(false);
            List<Prescriptions> presList = prescriptionDao.filterPrescription(presDto);
            Prescriptions prescription = presList.isEmpty() ? null : presList.get(0);

            // Tạo order
            Orders order = new Orders();
            Patients patient = new Patients();
            patient.setPatientID(patientId);
            order.setPatients(patient);
            if (prescription != null) {
                order.setPrescriptions(prescription); // gắn Prescription
            }

            order.setOrderDate(new Timestamp(System.currentTimeMillis()));
            order.setTotalAmount(totalAmount);
            order.setStatus("Pending");
            order.setPaymentMethod("Cash");
            order.setPaymentStatus("Unpaid");

            int orderId = orderDao.insertOrderWithDetails(order, orderDetailsList);
            if (orderId <= 0) {
                request.setAttribute("error", "Không thể tạo order.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/checkout?orderId=" + orderId);

        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Dữ liệu số không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Có lỗi khi tạo order.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
