/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AppointmentsDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author vuhieu
 */
@WebServlet(name = "UpdateAppointmentStatusController", urlPatterns = { "/update-appointment-status" })
public class UpdateAppointmentStatusController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String appointmentIdStr = request.getParameter("id");
        String newStatus = request.getParameter("status");
        String phoneNumber = request.getParameter("phone");
        String source = request.getParameter("source"); // Thêm tham số để biết nguồn gốc request

        HttpSession session = request.getSession();

        try {
            int appointmentId = Integer.parseInt(appointmentIdStr);

            // Validate newStatus to prevent arbitrary updates
            if (!"Confirmed".equals(newStatus) && !"Cancelled".equals(newStatus)) {
                throw new IllegalArgumentException("Trạng thái cập nhật không hợp lệ.");
            }

            AppointmentsDao appointmentsDao = new AppointmentsDao();
            boolean success = appointmentsDao.updateAppointmentStatus(appointmentId, newStatus);

            if (success) {
                session.setAttribute("successMessage",
                        "Cập nhật trạng thái lịch hẹn #" + appointmentId + " thành công!");
            } else {
                session.setAttribute("errorMessage", "Cập nhật trạng thái thất bại. Vui lòng thử lại.");
            }

        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Mã lịch hẹn không hợp lệ.");
        } catch (IllegalArgumentException e) {
            session.setAttribute("errorMessage", e.getMessage());
        }

        // Dựa vào 'source' để chuyển hướng về đúng trang
        if ("daily".equals(source)) {
            // Nếu đến từ trang daily-appointments, quay về trang đó
            response.sendRedirect(request.getContextPath() + "/receptionist/daily-appointments");
        } else {
            // Mặc định quay về trang tìm kiếm
            response.sendRedirect(request.getContextPath() + "/receptionist-searchandcheckin?phoneNumber="
                    + (phoneNumber != null ? phoneNumber : ""));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}