package controller;

import dal.AppointmentsDao;
import dal.DoctorDao;
import dal.NotificationsDao;
import dal.PatientDao;
import dal.ServiceDao;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

import model.Appointments;
import model.Doctor;
import model.Patients;
import model.Service;
import model.Users;

@WebServlet(name = "AppointmentController", urlPatterns = {"/appointmentbooking"})
public class AppointmentController extends HttpServlet {

    private final AppointmentsDao apptDao = new AppointmentsDao();
    private final DoctorDao doctorDao = new DoctorDao();
    private final PatientDao patientDao = new PatientDao();
    private final ServiceDao serviceDao = new ServiceDao();
    private final NotificationsDao notiDao = new NotificationsDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("services", serviceDao.getAllServices());
            req.setAttribute("doctors", doctorDao.getAllDoctors());
        } catch (Exception e) {
            System.err.println("Error loading services/doctors: " + e.getMessage());
            req.setAttribute("error", "Không thể tải danh sách dịch vụ hoặc bác sĩ.");
        }
        req.getRequestDispatcher("/views/appointment/booking.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        Users currentUser = (session != null) ? (Users) session.getAttribute("user") : null;
        if (currentUser == null) {
            resp.sendRedirect("login");
            return;
        }

        try {
            // Lấy patient từ user hiện tại
            Patients patient = patientDao.getPatientByUserId(currentUser.getUserId());
            if (patient == null) {
                req.setAttribute("error", "Không tìm thấy thông tin bệnh nhân cho tài khoản này.");
                loadFormWithData(req, resp);
                return;
            }
            // Lấy dữ liệu từ form
            int doctorId = Integer.parseInt(req.getParameter("doctorId"));
            int serviceId = Integer.parseInt(req.getParameter("serviceId"));
            Date date = Date.valueOf(req.getParameter("date"));
            Time start = Time.valueOf(req.getParameter("startTime") + ":00");

            // Lấy dịch vụ để biết duration
            Service svc = serviceDao.getServiceById(serviceId);
            if (svc == null) {
                req.setAttribute("error", "Dịch vụ không tồn tại hoặc không hoạt động.");
                loadFormWithData(req, resp);
                return;
            }
            int duration = (svc.getDuration() > 0) ? svc.getDuration() : 30;
            Time end = new Time(start.getTime() + duration * 60L * 1000L);

            //  CHECK TRÙNG LỊCH bằng existsDoctorTimeConflict trước khi insert
            boolean conflict = apptDao.existsDoctorTimeConflict(doctorId, date, start, end);
            if (conflict) {
                req.setAttribute("error", "Khung giờ này đã có lịch với bác sĩ. Vui lòng chọn thời gian khác.");
                // giữ lại lựa chọn đã nhập để user không phải chọn lại
                req.setAttribute("selDoctorId", doctorId);
                req.setAttribute("selServiceId", serviceId);
                req.setAttribute("selDate", req.getParameter("date"));
                loadFormWithData(req, resp);
                return;
            }

            // Build Appointments và insert
            Appointments a = new Appointments();
            a.setPatientId(patient);

            Doctor d = new Doctor();
            d.setDoctorID(doctorId);
            a.setDoctorId(d);

            a.setServiceId(svc);
            a.setAppointmentDate(date);
            a.setStartTime(start);
            a.setEndTime(end);
            a.setStatus("Scheduled");
            a.setNotes(req.getParameter("notes"));

            Integer newId = apptDao.insertAppointment(a);

            if (newId != null) {
                req.setAttribute("successMessage", "Bạn đã đặt lịch hẹn thành công!");
                req.setAttribute("apptId", newId);
                // Lấy UserID của bệnh nhân (đã đăng nhập)
                Integer patientUserId = currentUser.getUserId();

                // Lấy UserID của bác sĩ từ DoctorID 
                Integer doctorUserId = null;
                try {
                    doctorUserId = doctorDao.getUserIdByDoctorId(doctorId);
                } catch (Exception ignored) {
                }

                // Chuẩn bị nội dung
                String msg = req.getParameter("startTime");
                String msgForPatient = "Bạn đã đặt lịch với bác sĩ ID=" + doctorId + " vào ngày " + date + " lúc " + msg + ".";
                String msgForDoctor = "Có lịch hẹn mới của bệnh nhân ID=" + patient.getPatientID()
                        + " vào " + date + " lúc " + msg + " (ServiceID=" + serviceId + ").";

                if (patientUserId != null) {
                    notiDao.insert(patientUserId, "Đặt lịch hẹn", msgForPatient, "Appointment");
                }
                if (doctorUserId != null) {
                    notiDao.insert(doctorUserId, "Lịch hẹn mới", msgForDoctor, "Appointment");
                }
                req.getRequestDispatcher("/views/appointment/confirm.jsp").forward(req, resp);
            } else {
                req.setAttribute("error", "Không thể đặt lịch. Vui lòng thử thời gian khác.");
                // giữ lại lựa chọn
                req.setAttribute("selDoctorId", doctorId);
                req.setAttribute("selServiceId", serviceId);
                req.setAttribute("selDate", req.getParameter("date"));
                loadFormWithData(req, resp);
            }

        } catch (NumberFormatException e) {
            req.setAttribute("error", "Dữ liệu không hợp lệ. Vui lòng kiểm tra lại.");
            loadFormWithData(req, resp);
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", "Ngày hoặc giờ không hợp lệ.");
            loadFormWithData(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi hệ thống. Vui lòng thử lại sau.");
            loadFormWithData(req, resp);
        }
    }

    private void loadFormWithData(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("services", serviceDao.getAllServices());
            req.setAttribute("doctors", doctorDao.getAllDoctors());
        } catch (Exception e) {
            System.err.println("Error reloading form data: " + e.getMessage());
        }
        req.getRequestDispatcher("/views/appointment/booking.jsp").forward(req, resp);
    }
}
