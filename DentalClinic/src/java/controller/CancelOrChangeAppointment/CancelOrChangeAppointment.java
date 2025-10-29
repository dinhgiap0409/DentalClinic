/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.CancelOrChangeAppointment;

import dal.AppointmentsDao;
import dal.NotificationsDao;
import dal.ScheduleDao;
import dal.ScheduleExceptionsDao;
import dto.AppointmentDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Appointments;
import java.sql.Timestamp;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author Nguyen Dang Khang
 */
@WebServlet(name = "CancelOrChangeAppointment", urlPatterns = {"/cancelOrChangeAppointment"})
public class CancelOrChangeAppointment extends HttpServlet {

    private AppointmentsDao appointmentsDao = new AppointmentsDao();
    private NotificationsDao notificationsDao = new NotificationsDao();
    private ScheduleDao scheduleDao = new ScheduleDao();
    private ScheduleExceptionsDao exceptionsDao = new ScheduleExceptionsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action"); // "cancel" hoặc "reschedule"
        int appointmentId = Integer.parseInt(req.getParameter("appointmentId"));

        // Lấy lịch hẹn hiện tại
        Appointments appointment = appointmentsDao.getAppointmentsById(appointmentId);
        if (appointment == null) {
            req.setAttribute("error", "Không tìm thấy lịch hẹn!");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        try {
            if ("cancel".equalsIgnoreCase(action)) {
                handleCancel(req, resp, appointment);
            } else if ("reschedule".equalsIgnoreCase(action)) {
                handleReschedule(req, resp, appointment);
            } else {
                req.setAttribute("error", "Hành động không hợp lệ!");
                req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Lỗi xử lý: " + e.getMessage());
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
        }

    }

    /**
     * Hủy lịch hẹn
     */
    private void handleCancel(HttpServletRequest req, HttpServletResponse resp, Appointments appointment)
            throws ServletException, IOException {

        String currentStatus = appointment.getStatus();
        if (!"Scheduled".equalsIgnoreCase(currentStatus)
                && !"Rescheduled".equalsIgnoreCase(currentStatus)) {
            req.setAttribute("error", "Chỉ có thể hủy lịch hẹn đang ở trạng thái 'Scheduled' hoặc 'Rescheduled'.");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        // Update status = Cancelled
        appointment.setStatus("Cancelled");
        appointment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        boolean success = appointmentsDao.updateAppointment(appointment);
        if (!success) {
            req.setAttribute("error", "Không thể hủy lịch hẹn!");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        // Gửi thông báo
        notificationsDao.insert(
                appointment.getDoctorId().getDoctorID(),
                "Lịch hẹn bị hủy",
                "Lịch hẹn ngày " + appointment.getAppointmentDate() + " đã bị hủy.",
                "Appointment"
        );
        notificationsDao.insert(
                appointment.getPatientId().getPatientID(),
                "Bạn đã hủy lịch hẹn",
                "Lịch hẹn ngày " + appointment.getAppointmentDate() + " đã được hủy thành công.",
                "Appointment"
        );

        req.setAttribute("message", "Hủy lịch hẹn thành công!");
        req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
    }

    /**
     * 🧱 Thay đổi lịch hẹn
     */
    private void handleReschedule(HttpServletRequest req, HttpServletResponse resp, Appointments appointment)
            throws ServletException, IOException {

        Date newDate = Date.valueOf(req.getParameter("newDate"));
        Time newStart = Time.valueOf(req.getParameter("newStart") + ":00");
        Time newEnd = Time.valueOf(req.getParameter("newEnd") + ":00");

        // Kiểm tra hợp lệ thời gian
        if (!newEnd.after(newStart)) {
            req.setAttribute("error", "Giờ kết thúc phải sau giờ bắt đầu!");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        // === Sử dụng filterAppointment để kiểm tra trùng lịch ===
        AppointmentDto filter = new AppointmentDto();
        filter.setDoctorId(appointment.getDoctorId().getDoctorID());
        filter.setAppointmentDate(newDate);
        filter.setStartTime(newEnd);   // end trước để so sánh StartTime < newEnd
        filter.setEndTime(newStart);   // start sau để so sánh EndTime > newStart
        filter.setStatus("Scheduled"); // chỉ xét các lịch còn hiệu lực
        filter.setPaginationMode(false);

        // Gọi filterAppointment
        boolean isFree = true;
        var overlaps = appointmentsDao.filterAppointment(filter);
        if (overlaps != null && !overlaps.isEmpty()) {
            // Loại trừ chính lịch hẹn hiện tại ra khỏi danh sách kiểm tra
            for (Appointments ap : overlaps) {
                if (ap.getAppointmentId() != appointment.getAppointmentId()) {
                    isFree = false;
                    break;
                }
            }
        }

        if (!isFree) {
            req.setAttribute("error", "Bác sĩ không trống trong khung giờ này!");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        // Cập nhật lịch hẹn
        appointment.setAppointmentDate(newDate);
        appointment.setStartTime(newStart);
        appointment.setEndTime(newEnd);
        appointment.setStatus("Rescheduled");
        appointment.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        boolean success = appointmentsDao.updateAppointment(appointment);
        if (!success) {
            req.setAttribute("error", "Không thể thay đổi lịch hẹn!");
            req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
            return;
        }

        // Gửi thông báo
        notificationsDao.insert(
                appointment.getDoctorId().getDoctorID(),
                "Lịch hẹn được dời lại",
                "Lịch hẹn với bệnh nhân ID " + appointment.getPatientId().getPatientID()
                + " đã được dời sang ngày " + newDate + ".",
                "Appointment"
        );
        notificationsDao.insert(
                appointment.getPatientId().getPatientID(),
                "Thay đổi lịch hẹn thành công",
                "Lịch hẹn với bác sĩ ID " + appointment.getDoctorId().getDoctorID()
                + " đã được dời sang ngày " + newDate + ".",
                "Appointment"
        );

        req.setAttribute("message", "Thay đổi lịch hẹn thành công!");
        req.getRequestDispatcher("/appointments/manage.jsp").forward(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
