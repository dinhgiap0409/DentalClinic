/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.MedicalExaminationForm;

import dal.AppointmentsDao;
import dal.MedicalRecordDao;
import dal.OrderDao;
import dal.PrescriptionDao;
import dto.MedicalRecordDto;
import dto.OrderDto;
import dto.PrescriptionDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Appointments;
import model.MedicalRecords;
import model.OrderDetails;
import model.Orders;
import model.Prescriptions;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PrintInvoiceController", urlPatterns = {"/printInvoice"})
public class PrintInvoiceController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã lịch hẹn.");
            return;
        }

        int appointmentId = Integer.parseInt(idParam);

        //Lấy thông tin lịch hẹn
        AppointmentsDao appointmentDao = new AppointmentsDao();
        Appointments appointment = appointmentDao.getAppointmentsById(appointmentId);

        if (appointment == null) {
            request.setAttribute("error", "Không tìm thấy lịch hẹn.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        if (!"Completed".equalsIgnoreCase(appointment.getStatus())) {
            request.setAttribute("error", "Chỉ có thể in hóa đơn cho lịch hẹn đã hoàn tất (Completed).");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        //Tìm MedicalRecord theo appointmentId
        MedicalRecordDao recordDao = new MedicalRecordDao();
        MedicalRecordDto recordDto = new MedicalRecordDto();
        recordDto.setAppointmentId(appointmentId);
        recordDto.setPaginationMode(false);

        List<MedicalRecords> recordList = recordDao.filterMedicalRecord(recordDto);
        if (recordList.isEmpty()) {
            request.setAttribute("error", "Không tìm thấy hồ sơ bệnh án cho lịch hẹn này.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        MedicalRecords record = recordList.get(0);

        //Tìm Prescription theo recordId
        PrescriptionDao prescriptionDao = new PrescriptionDao();
        PrescriptionDto presDto = new PrescriptionDto();
        presDto.setRecordId(record.getRecordId());
        presDto.setPaginationMode(false);

        List<Prescriptions> presList = prescriptionDao.filterPrescription(presDto);
        Prescriptions prescription = presList.isEmpty() ? null : presList.get(0);

        //Tìm Orders theo prescriptionId
        Orders order = null;
        List<OrderDetails> orderDetails = null;
        if (prescription != null) {
            OrderDao orderDao = new OrderDao();
            OrderDto orderDto = new OrderDto();
            orderDto.setPrescriptionId(prescription.getPrescriptionId());
            orderDto.setPaginationMode(false);

            List<Orders> orders = orderDao.filterOrders(orderDto);
            if (!orders.isEmpty()) {
                order = orders.get(0);
            }

            orderDetails = orderDao.getOrderDetailsByOrderId(order.getOrderId());
        }

        if (order == null) {
            request.setAttribute("error", "Không tìm thấy hóa đơn hoặc bệnh nhân chưa thanh toán.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        //Gửi dữ liệu sang JSP để hiển thị / in PDF
        request.setAttribute("appointment", appointment);
        request.setAttribute("record", record);
        request.setAttribute("prescription", prescription);
        request.setAttribute("order", order);
        request.setAttribute("orderDetails", orderDetails);

        request.getRequestDispatcher("/views/manage/invoice-print.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
