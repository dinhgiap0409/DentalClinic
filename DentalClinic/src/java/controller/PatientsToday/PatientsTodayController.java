/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.PatientsToday;

import dal.AppointmentsDao;
import dto.AppointmentDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import model.Appointments;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PatientsTodayController", urlPatterns = {"/patientsToday"})
public class PatientsTodayController extends HttpServlet {

    private final AppointmentsDao appointmentsDAO = new AppointmentsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");

            // ===== Phân trang =====
            int page = 1;
            int size = 10;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }

            // ===== Lấy các bộ lọc =====
            AppointmentDto filter = new AppointmentDto();

            if (request.getParameter("patientName") != null && !request.getParameter("patientName").isEmpty()) {
                filter.setPatientName(request.getParameter("patientName").trim());
            }
            if (request.getParameter("phoneNumber") != null && !request.getParameter("phoneNumber").isEmpty()) {
                filter.setPhoneNumber(request.getParameter("phoneNumber").trim());
            }
            if (request.getParameter("serviceName") != null && !request.getParameter("serviceName").isEmpty()) {
                filter.setServiceName(request.getParameter("serviceName").trim());
            }

            // ===== Chỉ lấy lịch hẹn hôm nay =====
            Date today = Date.valueOf(LocalDate.now());
            filter.setAppointmentDate(today);

            // ===== Phân trang =====
            filter.setPaginationMode(true);
            filter.setPage(page);
            filter.setSize(size);

            // ===== Gọi DAO =====
            List<Appointments> appointments = appointmentsDAO.filterAppointment(filter);
            int totalRecords = appointmentsDAO.countAppointments(filter);
            int totalPages = (int) Math.ceil((double) totalRecords / size);

            // ===== Truyền dữ liệu ra JSP =====
            request.setAttribute("appointments", appointments);
            request.setAttribute("filter", filter);
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("baseUrl", "/patientsToday");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách bệnh nhân hôm nay.");
        }

        request.getRequestDispatcher("views/manage/patients_today.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        String aid = request.getParameter("appointmentId");

        if ("complete".equals(action) && aid != null) {
            try {
                int appointmentId = Integer.parseInt(aid);

                Appointments a = appointmentsDAO.getAppointmentsById(appointmentId);

                if (a != null) {
                    // 2. Set status mới
                    a.setStatus("Completed");

                    // 3. Cập nhật
                    boolean updated = appointmentsDAO.updateAppointment(a);

                    if (updated) {
                        request.getSession().setAttribute("successMessage", "Hoàn thành lịch hẹn thành công.");
                    } else {
                        request.getSession().setAttribute("errorMessage", "Cập nhật thất bại.");
                    }
                } else {
                    request.getSession().setAttribute("errorMessage", "Không tìm thấy lịch hẹn.");
                }
            } catch (NumberFormatException ex) {
                request.getSession().setAttribute("errorMessage", "ID lịch hẹn không hợp lệ.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/patientsToday");

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
