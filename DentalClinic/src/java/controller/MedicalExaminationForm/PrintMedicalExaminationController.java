/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.MedicalExaminationForm;

import dal.AppointmentsDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Appointments;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PrintMedicalExaminationController", urlPatterns = {"/printMedicalExamination"})
public class PrintMedicalExaminationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("appointmentId");
        if (idParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu mã lịch hẹn.");
            return;
        }

        int appointmentId = Integer.parseInt(idParam);

        // Gọi DAO lấy lịch hẹn
        AppointmentsDao dao = new AppointmentsDao();
        Appointments appointment = dao.getAppointmentsById(appointmentId);

        // Kiểm tra tồn tại
        if (appointment == null) {
            request.setAttribute("error", "Không tìm thấy lịch hẹn!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Kiểm tra trạng thái hợp lệ
        if (!"Confirmed".equalsIgnoreCase(appointment.getStatus())) {
            request.setAttribute("error", "Chỉ in phiếu khám cho lịch hẹn đã xác nhận (Confirmed).");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        // Gửi dữ liệu sang JSP
        request.setAttribute("appointment", appointment);
        request.getRequestDispatcher("/views/receptionist/appointment-print.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
