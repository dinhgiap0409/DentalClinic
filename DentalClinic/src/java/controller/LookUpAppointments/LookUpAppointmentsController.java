/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.LookUpAppointments;

import dal.AppointmentsDao;
import dto.AppointmentDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Appointments;
import java.sql.*;

/**
 *
 * @author TNO
 */
@WebServlet(name = "LookUpAppointmentsController", urlPatterns = {"/lookUpAppointments"})
public class LookUpAppointmentsController extends HttpServlet {

    private final AppointmentsDao appointmentsDAO = new AppointmentsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String errorMessage = request.getParameter("errorMessage");
        String successMessage = request.getParameter("successMessage");
        request.setAttribute("errorMessage", errorMessage);
        request.setAttribute("successMessage", successMessage);
        try {
            request.setCharacterEncoding("UTF-8");

            // ===== Lấy tham số trang =====
            int page = 1;
            int size = 10;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            // ===== Lấy các filter từ query string =====
            AppointmentDto filter = new AppointmentDto();
            if (request.getParameter("patientId") != null && !request.getParameter("patientId").isEmpty()) {
                filter.setPatientId(Integer.parseInt(request.getParameter("patientId")));
            }
            if (request.getParameter("doctorId") != null && !request.getParameter("doctorId").isEmpty()) {
                filter.setDoctorId(Integer.parseInt(request.getParameter("doctorId")));
            }
            if (request.getParameter("status") != null && !request.getParameter("status").isEmpty()) {
                filter.setStatus(request.getParameter("status"));
            }
            if (request.getParameter("patientName") != null && !request.getParameter("patientName").isEmpty()) {
                filter.setPatientName(request.getParameter("patientName").trim());
            }
            if (request.getParameter("phoneNumber") != null && !request.getParameter("phoneNumber").isEmpty()) {
                filter.setPhoneNumber(request.getParameter("phoneNumber").trim());
            }
            if (request.getParameter("doctorName") != null && !request.getParameter("doctorName").isEmpty()) {
                filter.setDoctorName(request.getParameter("doctorName").trim());
            }
            if (request.getParameter("serviceName") != null && !request.getParameter("serviceName").isEmpty()) {
                filter.setServiceName(request.getParameter("serviceName").trim());
            }

            if (request.getParameter("fromDate") != null && !request.getParameter("fromDate").isEmpty()) {
                filter.setAppointmentDateFrom(Date.valueOf(request.getParameter("fromDate")));
            }
            if (request.getParameter("toDate") != null && !request.getParameter("toDate").isEmpty()) {
                filter.setAppointmentDateTo(Date.valueOf(request.getParameter("toDate")));
            }

            // ===== Thiết lập phân trang =====
            filter.setPaginationMode(true);
            filter.setPage(page);
            filter.setSize(size);

            // ===== Gọi DAO =====
            List<Appointments> appointments = appointmentsDAO.filterAppointment(filter);
            int totalRecords = appointmentsDAO.countAppointments(filter);
            int totalPages = (int) Math.ceil((double) totalRecords / size);

            // ===== Truyền dữ liệu cho JSP =====
            request.setAttribute("appointments", appointments);
            request.setAttribute("filter", filter);
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("baseUrl","/lookUpAppointments");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách lịch hẹn.");
        }

        request.getRequestDispatcher("views/receptionist/lookup.jsp").forward(request, response);
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
