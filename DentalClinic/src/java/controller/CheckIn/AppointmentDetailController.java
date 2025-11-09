/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.CheckIn;

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
@WebServlet(name = "AppointmentDetailController", urlPatterns = {"/appointmentDetail"})
public class AppointmentDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String idRaw = request.getParameter("appointmentId");
            if (idRaw == null || idRaw.trim().isEmpty()) {
                request.getSession().setAttribute("errorMessage", "Không tìm thấy lịch hẹn hợp lệ.");
                response.sendRedirect(request.getContextPath() + "/lookUpAppointments");
                return;
            }

            int appointmentId = Integer.parseInt(idRaw);
            AppointmentsDao dao = new AppointmentsDao();
            Appointments appointment = dao.getAppointmentsById(appointmentId);

            if (appointment == null) {
                request.getSession().setAttribute("errorMessage", "Lịch hẹn không tồn tại hoặc đã bị xóa.");
                response.sendRedirect(request.getContextPath() + "/lookUpAppointments");
                return;
            }

            request.setAttribute("appointment", appointment);
            request.getRequestDispatcher("/views/receptionist/appointmentDetail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("errorMessage", "Đã xảy ra lỗi khi tải chi tiết lịch hẹn.");
            response.sendRedirect(request.getContextPath() + "/lookUpAppointments");
        }

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
