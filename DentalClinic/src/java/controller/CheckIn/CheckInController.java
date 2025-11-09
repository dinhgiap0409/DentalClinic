/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.CheckIn;

import dal.AppointmentsDao;
import dal.ScheduleDao;
import dto.ScheduleDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import model.Appointments;
import model.Schedules;

/**
 *
 * @author TNO
 */
@WebServlet(name = "CheckInController", urlPatterns = {"/checkin"})
public class CheckInController extends HttpServlet {

    private final AppointmentsDao appointmentsDao = new AppointmentsDao();
    private final ScheduleDao scheduleDao = new ScheduleDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));
            boolean force = "true".equalsIgnoreCase(request.getParameter("force"));

            // Lấy thông tin appointment hiện tại
            Appointments ap = appointmentsDao.getAppointmentsById(appointmentId);
            if (ap == null) {
                String msg = "Không tìm thấy lịch hẹn #" + appointmentId;
                response.sendRedirect(request.getContextPath() + "/lookUpAppointments?errorMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }

            // Kiểm tra trạng thái hợp lệ
            if (!"Scheduled".equalsIgnoreCase(ap.getStatus()) && !"Rescheduled".equalsIgnoreCase(ap.getStatus())) {
                String msg = "Chỉ những lịch 'Scheduled' hoặc 'Rescheduled' mới được check-in. Trạng thái hiện tại: " + ap.getStatus();
                response.sendRedirect(request.getContextPath() + "/lookUpAppointments?errorMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }

            // Kiểm tra thời gian check-in hợp lệ
            LocalDate apDate = ap.getAppointmentDate().toLocalDate();
            LocalTime start = ap.getStartTime().toLocalTime();
            LocalTime end = ap.getEndTime().toLocalTime();
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime earliest = LocalDateTime.of(apDate, start).minusMinutes(30);
            LocalDateTime latest = LocalDateTime.of(apDate, end);

            if (!(now.isAfter(earliest) && now.isBefore(latest)) && !force) {
                String msg;
                if (now.isBefore(earliest)) {
                    long minutes = java.time.Duration.between(now, earliest).toMinutes();
                    msg = "Bệnh nhân đến quá sớm (" + minutes + " phút nữa mới đến giờ check-in).";
                } else {
                    long late = java.time.Duration.between(LocalDateTime.of(apDate, start), now).toMinutes();
                    msg = "Bệnh nhân đến muộn " + late + " phút (>30 phút: nên đánh dấu No-Show).";
                }
                response.sendRedirect("lookUpAppointments?errorMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }
            // Kiểm tra bác sĩ có làm việc trong khung giờ đó không
            ScheduleDto schFilter = new ScheduleDto();
            schFilter.setDoctorId(ap.getDoctorId().getDoctorID());
            schFilter.setStartTime(ap.getStartTime());
            schFilter.setEndTime(ap.getEndTime());
            schFilter.setIsAvailable(true);
            schFilter.setPaginationMode(false);

            List<Schedules> schedules = scheduleDao.filterSchedules(schFilter);
            if (schedules.isEmpty() && !force) {
                String msg = "Bác sĩ chưa có lịch làm việc chính thức hôm nay. Vẫn muốn check-in?";
                response.sendRedirect("lookUpAppointments?errorMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
                return;
            }

            // Nếu hợp lệ → cập nhật status
            ap.setStatus("Confirmed");
            ap.setNotes((ap.getNotes() != null ? ap.getNotes() + " | " : "") + "Check-in lúc " + Timestamp.valueOf(now));
            boolean updated = appointmentsDao.updateAppointment(ap);

            if (updated) {
                String msg = ap.getPatientId().getUserID().getFullName()
                        + " đã check-in thành công lúc "
                        + now.getHour() + ":" + String.format("%02d", now.getMinute());
                response.sendRedirect("lookUpAppointments?successMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
            } else {
                String msg = "Không thể cập nhật trạng thái check-in.";
                response.sendRedirect("lookUpAppointments?errorMessage="
                        + java.net.URLEncoder.encode(msg, "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            String msg = "Lỗi khi xử lý check-in: " + e.getMessage();
            response.sendRedirect("lookUpAppointments?errorMessage="
                    + java.net.URLEncoder.encode(msg, "UTF-8"));
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
